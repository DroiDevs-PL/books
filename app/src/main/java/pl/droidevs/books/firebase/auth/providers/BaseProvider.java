package pl.droidevs.books.firebase.auth.providers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.subjects.PublishSubject;
import pl.droidevs.books.firebase.auth.UserCredentials;
import pl.droidevs.books.firebase.auth.responses.Error;
import pl.droidevs.books.firebase.auth.responses.Status;

abstract class BaseProvider {

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();
    protected final PublishSubject<Error> errorPublishSubject;
    protected final PublishSubject<Status> statusPublishSubject;

    public BaseProvider(PublishSubject<Error> errorPublishSubject, PublishSubject<Status> statusPublishSubject) {
        this.errorPublishSubject = errorPublishSubject;
        this.statusPublishSubject = statusPublishSubject;
    }

    abstract void init(Context context, String defaultWebClientId);

    abstract void login(Activity activity, UserCredentials userCredentials);

    @NonNull
    abstract String getProviderId();

    protected void loginTask(@NonNull Task<AuthResult> task, @Nullable AuthCredential newCredential) {
        if (task.isSuccessful()) {
            statusPublishSubject.onNext(Status.USER_LOGGED);
        } else {
            if (task.getException() instanceof FirebaseAuthUserCollisionException && newCredential != null) {
                handleCreateEmailAccountError(newCredential);
            }
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                errorPublishSubject.onNext(Error.WRONG_PASSWORD);
            }
        }
    }

    private void handleCreateEmailAccountError(AuthCredential newCredential) {
        @Nullable FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.getProviders().contains(getProviderId())) {
            errorPublishSubject.onNext(Error.EMAIL_ALREADY_USED);
        } else if (currentUser != null) {
            currentUser.linkWithCredential(newCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    loginTask(task, newCredential);
                } else {
                    errorPublishSubject.onNext(Error.EMAIL_ALREADY_USED);
                }
            });
        }
    }

    protected void signIn(AuthCredential credential) {
        @Nullable FirebaseUser currentUser = auth.getCurrentUser();
        if ((currentUser != null && (currentUser.isAnonymous()) || currentUser.getProviders().contains(credential.getProvider()))) {
            auth.signInWithCredential(credential).addOnCompleteListener(task -> loginTask(task, credential));
        } else {
            currentUser.linkWithCredential(credential).addOnCompleteListener(task -> loginTask(task, credential));
        }
    }
}
