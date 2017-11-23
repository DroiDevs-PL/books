package pl.droidevs.books.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import pl.droidevs.books.R;
import pl.droidevs.books.library.LibraryActivity;

public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginService loginService;

    @BindView(R.id.textHello)
    TextView textHello;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidInjection.inject(this);
        this.unbinder = ButterKnife.bind(this);

        textHello.setText(loginService.getUserName());
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        startActivity(new Intent(this, LibraryActivity.class));
    }

    @Override
    protected void onDestroy() {
        this.unbinder.unbind();

        super.onDestroy();
    }
}
