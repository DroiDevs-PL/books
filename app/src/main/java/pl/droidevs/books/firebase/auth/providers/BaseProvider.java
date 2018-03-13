package pl.droidevs.books.firebase.auth.providers;

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

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import pl.droidevs.books.firebase.auth.responses.Status;

abstract class BaseProvider {

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();

    abstract void init(Context context, String defaultWebClientId);

    @NonNull
    abstract String getProviderId();

    Single<FirebaseUser> loginTask(@NonNull Task<AuthResult> task, @Nullable AuthCredential newCredential) {
        return Single.create(emitter -> {
            if (task.isSuccessful()) {
                emitter.onSuccess(auth.getCurrentUser());
            } else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException && newCredential != null) {
                    handleCreateEmailAccountError(newCredential)
                        .subscribe(
                            status -> emitter.onSuccess(auth.getCurrentUser()),
                            emitter::onError);
                }
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    emitter.onError(new FirebaseException.WrongPasswordException());
                }
            }
        });
    }

    Single<Status> handleCreateEmailAccountError(AuthCredential newCredential) {
        return Single.create(emitter -> {
            @Nullable FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null && currentUser.getProviders().contains(getProviderId())) {
                emitter.onError(new FirebaseException.EmailAlreadyUsedException());
            } else if (currentUser != null) {
                currentUser.linkWithCredential(newCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginTask(task, newCredential)
                            .subscribe(
                                user -> emitter.onSuccess(Status.USER_LOGGED),
                                emitter::onError);
                    } else {
                        emitter.onError(new FirebaseException.UnknownException());
                    }
                });
            }
        });
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
