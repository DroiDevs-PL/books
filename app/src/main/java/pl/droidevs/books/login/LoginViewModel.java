package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModel;
import android.text.Editable;
import android.text.TextUtils;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    @Inject
    public LoginViewModel(){

    }

    public boolean isInputValid(Editable loginText) {
        return !TextUtils.isEmpty(loginText);
    }
}
