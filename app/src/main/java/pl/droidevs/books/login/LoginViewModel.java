package pl.droidevs.books.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import io.reactivex.Single;
import pl.droidevs.books.firebase.auth.UserAuth;
import pl.droidevs.books.firebase.auth.responses.Status;
import pl.droidevs.books.validators.EmailValidator;
import pl.droidevs.books.validators.PasswordValidator;

public class LoginViewModel extends ViewModel {

    @Inject
    UserAuth userAuth;

    @Inject
    public LoginViewModel() {
    }

    public boolean isUserLogged() {
        return userAuth.getUser() != null;
    }

    public FirebaseUser getUser() {
        return userAuth.getUser();
    }

    public boolean isEmailValid(String email) {
        return EmailValidator.isValid(email);
    }

    public boolean isPasswordValid(String password) {
        return PasswordValidator.isValid(password);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        userAuth.handleActivityResult(requestCode, resultCode, data);
    }

    public Single<FirebaseUser> loginWithEmail(String email, String password) {
        return userAuth.loginWithEmail(email, password);
    }

    public Single<Status> createAccount(String email, String password) {
        return userAuth.createAccount(email, password);
    }

    public Single<FirebaseUser> loginAnonymously() {
        return userAuth.loginAnonymously();
    }

    public  Single<FirebaseUser> loginWithGoogle(Activity activity) {
        return userAuth.loginWithGoogle(activity);
    }
}
