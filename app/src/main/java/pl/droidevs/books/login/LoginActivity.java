package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import pl.droidevs.books.firebase.auth.responses.Status;
import pl.droidevs.books.library.LibraryActivity;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_text)
    EditText emailEditText;

    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    @BindView(R.id.login_anonymously)
    Button loginAnonymously;

    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.create_account_button)
    Button createAccountButton;

    @BindView(R.id.google_login)
    Button googleLoginButton;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidInjection.inject(this);
        ButterKnife.bind(this);

        setupViewModel();

        if (loginViewModel.isUserLogged()) {
            showLibraryActivity(loginViewModel.getUser());
        }

        manageLoginButtonState();
    }

    private void setupViewModel() {
        this.loginViewModel = ViewModelProviders
            .of(this, this.viewModelFactory)
            .get(LoginViewModel.class);
    }

    private void showLibraryActivity(FirebaseUser user) {
        hideProgressBar();
        Toast.makeText(this, getString(R.string.logged_user_greeting) + " " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LibraryActivity.class));
        finish();
    }

    private void manageLoginButtonState() {
        String email = this.emailEditText.getText().toString();
        String password = this.passwordEditText.getText().toString();
        boolean shouldBeEnabled = this.loginViewModel.isEmailValid(email)
            && this.loginViewModel.isPasswordValid(password);

        this.loginButton.setEnabled(shouldBeEnabled);
        this.createAccountButton.setEnabled(shouldBeEnabled);
    }

    @OnTextChanged({R.id.email_edit_text, R.id.password_edit_text})
    public void onInputChanged() {
        manageLoginButtonState();
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        showProgressBar();
        this.loginViewModel.loginWithEmail(emailEditText.getText().toString(), passwordEditText.getText().toString())
            .subscribe(this::showLibraryActivity, this::handleError);
    }

    private void handleError(Throwable error) {
        hideProgressBar();
        Timber.e(error);
        if (error instanceof FirebaseException.WeakPasswordException) {
            passwordEditText.setError(getString(R.string.weak_password_error));
        } else if (error instanceof FirebaseException.EmailAlreadyUsedException) {
            emailEditText.setError(getString(R.string.email_used_error));
        } else if (error instanceof FirebaseException.WrongPasswordException) {
            passwordEditText.setError(getString(R.string.wrong_password_error));
        } else if (error instanceof FirebaseException.InvalidUserException) {
            emailEditText.setError(getString(R.string.invalid_user_error));
        } else {
            Toast.makeText(this, R.string.basic_error_text, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.create_account_button)
    public void onCreateAccountButtonClicked() {
        showProgressBar();
        this.loginViewModel.createAccount(emailEditText.getText().toString(), passwordEditText.getText().toString())
            .subscribe(this::handleCreatedUser, this::handleError);
    }

    private void handleCreatedUser(Status status) {
        if (status.equals(Status.ACCOUNT_CREATED)) {
            Toast.makeText(this, R.string.account_created_please_log_in, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.login_anonymously)
    public void loginAnonymously() {
        showProgressBar();
        loginViewModel.loginAnonymously().subscribe(this::showLibraryActivity, this::handleError);
    }

    @OnClick(R.id.google_login)
    public void loginWithGoogle() {
        showProgressBar();
        loginViewModel.loginWithGoogle(this).subscribe(this::showLibraryActivity, this::handleError);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.loginViewModel.handleActivityResult(requestCode, resultCode, data);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
