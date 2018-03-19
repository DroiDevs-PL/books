package pl.droidevs.books.about;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;

/**
 * Created by micha on 16.03.2018.
 */

public class ContributorFragment extends Fragment {
    public static final String TAG = ContributorFragment.class.getSimpleName();

    private AboutViewModel viewModel;


    @BindView(R.id.cl_card)
    ConstraintLayout clCard;

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_nick)
    TextView tvNick;

    @BindView(R.id.tv_github)
    TextView tvGithub;

    @BindView(R.id.tv_linkedin)
    TextView tvLinkedIn;

    @BindView(R.id.tv_google_play_store)
    TextView tvGooglePlayStore;

    @BindView(R.id.tv_www)
    TextView tvWww;

    @BindView(R.id.ll_github)
    LinearLayout llGitHub;

    @BindView(R.id.ll_linkedin)
    LinearLayout llLinkedIn;

    @BindView(R.id.ll_google_play_store)
    LinearLayout llGooglePlayStore;

    @BindView(R.id.ll_www)
    LinearLayout llWww;

    public static ContributorFragment newInstance() {
        ContributorFragment fragment = new ContributorFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contributor, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(AboutViewModel.class);

        viewModel.getSelectedContributorIndex().observe(this, index -> {
            Contributor contributor = Contributor.getTeam().get(index);
            setData(contributor, index);
        });

        if (savedInstanceState == null) {
            postponeEnterTransition();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llGitHub.postDelayed(() -> startPostponedEnterTransition(), 100);
    }

    private void setData(Contributor contributor, int index) {
        tvName.setText(contributor.getName());
        tvNick.setText(contributor.getNick());
        ivAvatar.setTransitionName(getContext().getResources().getString(R.string.iv_avatar_transition_name) + "_" + index);

        llGitHub.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contributor.getGithubUrl()));
            startActivity(browserIntent);
        });

        if (contributor.getLinkedInUrl() != null) {
            llLinkedIn.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contributor.getLinkedInUrl()));
                startActivity(browserIntent);
            });
        } else {
            llLinkedIn.setVisibility(View.GONE);
        }

        if (contributor.getGooglePlayStoreUrl() != null) {
            llGooglePlayStore.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contributor.getGooglePlayStoreUrl()));
                startActivity(browserIntent);
            });
        } else {
            llGooglePlayStore.setVisibility(View.GONE);
        }

        if (contributor.getWebsiteUrl() != null) {
            llWww.setOnClickListener(view -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contributor.getWebsiteUrl()));
                startActivity(browserIntent);
            });
        } else {
            llWww.setVisibility(View.GONE);
        }

        Glide.with(this).load(contributor.getResourceImage()).into(ivAvatar);
    }
}
