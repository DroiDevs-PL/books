package pl.droidevs.books.firebase.auth.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import pl.droidevs.books.firebase.auth.responses.Status;
import timber.log.Timber;

abstract class BaseProvider {

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();

    abstract void init(Context context, String defaultWebClientId);

    @NonNull
    abstract String getProviderId();

    Single<FirebaseUser> loginTask(@NonNull Task<AuthResult> task, @Nullable AuthCredential newCredential) {
        return Single.create(emitter -> {
            if (task.isSuccessful()) {
                emitter.onSuccess(auth.getCurrentUser());
                return;
            } else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException && newCredential != null) {
                    handleCreateEmailAccountError(newCredential)
                        .subscribe(
                            status -> emitter.onSuccess(auth.getCurrentUser()),
                            emitter::onError);
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    emitter.onError(new FirebaseException.WrongPasswordException());
                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    emitter.onError(new FirebaseException.InvalidUserException());
                } else {
                    emitter.onError(new FirebaseException.UnknownException());
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

    Single<FirebaseUser> signIn(AuthCredential credential) {
        @Nullable FirebaseUser currentUser = auth.getCurrentUser();
        Timber.d("current user: %s", currentUser);
        return Single.create(emmiter -> {
            if (currentUser == null) throw new FirebaseException.UserNotLoggedInException();
            if (currentUser.isAnonymous() || currentUser.getProviders().contains(credential.getProvider())) {
                auth.signInWithCredential(credential).addOnCompleteListener(task -> loginTask(task, credential)
                    .subscribe(emmiter::onSuccess, emmiter::onError));
            } else {
                currentUser.linkWithCredential(credential).addOnCompleteListener(task -> loginTask(task, credential)
                    .subscribe(emmiter::onSuccess, emmiter::onError));
            }
        });
    }
}
