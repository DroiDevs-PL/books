package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModel;
import android.text.Editable;
import android.text.TextUtils;

public class LoginViewModel extends ViewModel {

    public boolean isInputValid(Editable loginText) {
        return !TextUtils.isEmpty(loginText);
    }
}
