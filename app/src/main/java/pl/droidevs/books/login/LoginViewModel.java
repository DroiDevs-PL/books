package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;

import javax.inject.Inject;

import pl.droidevs.books.validators.LoginValidator;

public class LoginViewModel extends ViewModel {

    private static final String SHARED_PREFERENCES_LOGIN_KEY = "login";

    SharedPreferences sharedPreferences;

    @Inject
    public LoginViewModel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    //TODO in future should check if user is logged in
    //TODO now is checking if user provided login
    public boolean isLoggedIn() {
        return this.sharedPreferences.getString(SHARED_PREFERENCES_LOGIN_KEY, null) != null;
    }

    public boolean isInputValid(String login) {
        return LoginValidator.isLoginValid(login);
    }

    public void saveLogin(String login) {
        this.sharedPreferences
            .edit()
            .putString(SHARED_PREFERENCES_LOGIN_KEY, login)
            .apply();
    }
}
