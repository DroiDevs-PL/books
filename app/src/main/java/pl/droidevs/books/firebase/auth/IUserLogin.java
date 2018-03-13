package pl.droidevs.books.firebase.auth;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.responses.Status;

public interface IUserLogin {

    // TODO: 2018-03-12 maybe login methods should return obserbables?
    Single<FirebaseUser> loginWithGoogle(Activity activity);

    Single<FirebaseUser> loginWithFacebook(Activity activity);

    Single<FirebaseUser> loginWithPhone(Activity activity, String phoneNumber);

    Single<FirebaseUser> loginWithEmail(String email, String password);

    Single<FirebaseUser> loginAnonymously();

    Single<Status> createAccount(String email, String password);

    FirebaseUser getUser();

    void checkVerificationCode(String code);

    void logout();

    void handleActivityResult(int requestCode, int resultCode, Intent data);

    Single<Status> resetPassword(String email);

    Single<Status> resendVerificationEmail();
}
