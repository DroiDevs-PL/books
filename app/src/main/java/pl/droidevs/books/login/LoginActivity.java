package pl.droidevs.books.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidevs.books.R;
import pl.droidevs.books.library.LibraryActivity;

public class LoginActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.unbinder = ButterKnife.bind(this);
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
