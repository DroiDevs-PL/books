package pl.droidevs.books.about;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.droidevs.books.R;

public class AboutActivity extends AppCompatActivity {
    private static final String EXTRAS_LAST_MAIN_POSITION = "EXTRAS_LAST_MAIN_POSITION";

    private AboutViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.about);
        setContentView(R.layout.activity_about);

        viewModel = ViewModelProviders.of(this).get(AboutViewModel.class);

        if (savedInstanceState != null) {
            viewModel.lastMainPosition = savedInstanceState.getInt(EXTRAS_LAST_MAIN_POSITION, 0);
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_about, AboutVPFragment.newInstance(), AboutFragment.TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRAS_LAST_MAIN_POSITION, viewModel.lastMainPosition);
    }
}
