package pl.droidevs.books.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.library.LibraryActivity;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_edit_text)
    EditText loginEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    private Unbinder unbinder;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidInjection.inject(this);
        this.unbinder = ButterKnife.bind(this);

        setupViewModel();
        manageLoginButton();
    }

    private void setupViewModel() {
        this.loginViewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(LoginViewModel.class);
    }

    private void manageLoginButton() {

        if (this.loginViewModel.isInputValid(this.loginEditText.getText().toString())) {
            this.loginButton.setEnabled(true);

            return;
        }

        this.loginButton.setEnabled(false);
    }

    @OnTextChanged(R.id.login_edit_text)
    public void onInputChanged() {
        manageLoginButton();
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        this.loginViewModel.saveLogin(this.loginEditText.getText().toString());
        startActivity(new Intent(this, LibraryActivity.class));
    }

    @Override
    protected void onDestroy() {
        this.unbinder.unbind();

        super.onDestroy();
    }
}
