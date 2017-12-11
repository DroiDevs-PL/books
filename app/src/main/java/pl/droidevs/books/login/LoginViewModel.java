package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import pl.droidevs.books.validators.LoginValidator;

public class LoginViewModel extends ViewModel {

    @Inject
    public LoginViewModel(){

    }

    public boolean isInputValid(String login) {
        return LoginValidator.isLoginValid(login);
    }
}
