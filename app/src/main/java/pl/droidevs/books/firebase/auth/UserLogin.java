package pl.droidevs.books.firebase.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.providers.AnonymousProvider;
import pl.droidevs.books.firebase.auth.providers.EmailProvider;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import pl.droidevs.books.firebase.auth.responses.Status;

public class UserLogin implements IUserLogin {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final EmailProvider emailProvider = new EmailProvider();
    private final AnonymousProvider anonymousProvider = new AnonymousProvider();

    @Inject
    public UserLogin(Context context, String defaultWebClientId) {

    }

    @Override
    public Single<FirebaseUser> loginWithGoogle(Activity activity) {
        return null;
    }

    @Override
    public Single<FirebaseUser> loginWithFacebook(Activity activity) {
        return null;
    }

    @Override
    public Single<FirebaseUser> loginWithPhone(Activity activity, String phoneNumber) {
        return null;
    }

    @Override
    public Single<FirebaseUser> loginWithEmail(String email, String password) {
        return emailProvider.login(new UserCredentials(email, password));
    }

    @Override
    public Single<FirebaseUser> loginAnonymously() {
        return anonymousProvider.login();
    }

    @Override
    public Single<Status> createAccount(String email, String password) {
        return emailProvider.createAccount(email, password);
    }

    @Override
    public FirebaseUser getUser() {
        return auth.getCurrentUser();
    }

    @Override
    public void checkVerificationCode(String code) {
        auth.checkActionCode(code);
    }

    @Override
    public void logout() {
        auth.signOut();
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public Single<Status> resetPassword(String email) {
        return Single.create(emitter ->
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onSuccess(Status.RESET_PASSWORD_EMAIL_SEND);
                } else {
                    emitter.onError(new FirebaseException.ResetPasswordException());
                }
            })
        );
    }

    @Override
    public Single<Status> resendVerificationEmail() {
        return Single.create(emitter -> {
            if (getUser() != null) {
                getUser().sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        emitter.onSuccess(Status.VERIFICATION_EMAIL_SEND);
                    } else {
                        emitter.onError(new FirebaseException.VerificationEmailSendingException());
                    }
                });
            } else {
                emitter.onError(new FirebaseException.UserNotLoggedInException());
            }
        });
    }
}
