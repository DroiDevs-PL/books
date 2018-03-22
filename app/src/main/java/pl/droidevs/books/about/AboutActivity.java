package pl.droidevs.books.about;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.IntegerRes;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.List;

import pl.droidevs.books.R;

public class AboutActivity extends AppCompatActivity {
    private static final String EXTRAS_SELECTED_CONTRIBUTOR_INDEX = "EXTRAS_SELECTED_CONTRIBUTOR_INDEX";

    private AboutViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.about);
        setContentView(R.layout.activity_about);

        viewModel = ViewModelProviders.of(this).get(AboutViewModel.class);

        if (savedInstanceState != null) {
            int selectedContributorIndex = savedInstanceState.getInt(EXTRAS_SELECTED_CONTRIBUTOR_INDEX, 0);
            viewModel.selectContributor(selectedContributorIndex);
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
        outState.putInt(EXTRAS_SELECTED_CONTRIBUTOR_INDEX, viewModel.getSelectedContributorIndex().getValue());
    }

    public void showContributorDetails(ImageView imageView, ConstraintLayout constraintLayout, int index) {

        int FADE_DEFAULT_TIME = 250;
        int MOVE_DEFAULT_TIME = 500;

        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.fl_about);
        Fragment nextFragment = ContributorFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()/*.setReorderingAllowed(true)*/;

        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previousFragment.setExitTransition(exitFade);

        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.explode));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        Fade enterFade = new Fade();
//        android.support.transition.
//        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        nextFragment.setEnterTransition(enterFade);

        transaction.addSharedElement(imageView, imageView.getTransitionName());
        transaction.addSharedElement(constraintLayout, constraintLayout.getTransitionName());
        transaction.replace(R.id.fl_about, nextFragment, ContributorFragment.TAG);
        transaction.addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        ContributorFragment myFragment = (ContributorFragment) getSupportFragmentManager().findFragmentByTag(ContributorFragment.TAG);
        if (myFragment != null && myFragment.isVisible()) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof AboutFragment || fragment instanceof OurTeamFragment/* || fragment instanceof AboutVPFragment*/) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }

            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
