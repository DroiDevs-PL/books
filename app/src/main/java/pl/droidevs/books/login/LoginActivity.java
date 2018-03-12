package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.firebase.auth.UserLogin;
import pl.droidevs.books.library.LibraryActivity;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text)
    EditText loginEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.create_account_button)
    Button createAccountButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    UserLogin userLogin;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupViewModel();

        if (this.loginViewModel.isLoggedIn()) {
            showLibraryActivity();
        }

        manageLoginButtonState();
    }

    private void setupViewModel() {
        this.loginViewModel = ViewModelProviders
            .of(this, this.viewModelFactory)
            .get(LoginViewModel.class);
    }

    private void showLibraryActivity() {
        startActivity(new Intent(this, LibraryActivity.class));
        finish();
    }

    private void manageLoginButtonState() {
        String login = this.loginEditText.getText().toString();
        boolean shouldBeEnabled = this.loginViewModel.isInputValid(login);

        this.loginButton.setEnabled(shouldBeEnabled);
    }

    @OnTextChanged(R.id.email_edit_text)
    public void onInputChanged() {
        manageLoginButtonState();
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        this.loginViewModel.saveLogin(this.loginEditText.getText().toString());
        showLibraryActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
