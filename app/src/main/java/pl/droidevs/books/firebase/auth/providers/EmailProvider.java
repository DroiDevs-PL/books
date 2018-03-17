package pl.droidevs.books.firebase.auth.providers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.UserCredentials;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import pl.droidevs.books.firebase.auth.responses.Status;

public class EmailProvider extends BaseProvider {

    @Override
    void init(Context context, String defaultWebClientId) {

    }

    public Single<FirebaseUser> login(@NonNull UserCredentials userCredentials) {
        return Single.create(emitter -> {
            String email = userCredentials.getEmail();
            String password = userCredentials.getPassword();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                emitter.onError(new FirebaseException.EmptyEmailException());
                return;
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task ->
                    loginTask(task, EmailAuthProvider.getCredential(email, password))
                        .subscribe(emitter::onSuccess, emitter::onError));
        });
    }

    public Single<Status> createAccount(String email, String password) {
        return Single.create(emitter -> {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                emitter.onError(new FirebaseException.EmptyEmailException());
                return;
            }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) emitter.onSuccess(Status.ACCOUNT_CREATED);
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        handleCreateEmailAccountError(EmailAuthProvider.getCredential(email, password))
                            .subscribe(emitter::onSuccess, emitter::onError);
                    } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        emitter.onError(new FirebaseException.WeakPasswordException());
                    } else {
                        emitter.onError(new FirebaseException.UnknownException());
                    }
                }
            });
        });
    }

    @NonNull
    @Override
    String getProviderId() {
        return EmailAuthProvider.PROVIDER_ID;
    }
}
