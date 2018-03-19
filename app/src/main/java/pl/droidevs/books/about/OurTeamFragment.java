package pl.droidevs.books.about;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;

public class OurTeamFragment extends Fragment implements OurTeamRVAdapter.teamClickListener {

    private OurTeamRVAdapter adapter;
    private AboutViewModel viewModel;

    @BindView(R.id.rv_team)
    RecyclerView rvTeam;

    @BindView(R.id.cl_card)
    ConstraintLayout clCard;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_nick)
    TextView tvNick;

    @BindView(R.id.ll_linkedin)
    LinearLayout llLinkedIn;

    @BindView(R.id.ll_google_play_store)
    LinearLayout llGooglePlayStore;

    @BindView(R.id.ll_www)
    LinearLayout llWww;

    public static OurTeamFragment newInstance() {
        return new OurTeamFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_team, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(AboutViewModel.class);

        adapter = new OurTeamRVAdapter(getContext(), this);
        rvTeam.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTeam.setAdapter(adapter);

        viewModel.getSelectedContributorIndex().observe(this, index -> {
            Contributor contributor = Contributor.getTeam().get(index);
            setCardData(contributor);
        });

        return view;
    }

    @Override
    public void onItemClick(ImageView imageView, int index) {
        viewModel.selectContributor(index);
        ((AboutActivity) getActivity()).showContributorDetails(imageView, clCard, index);
    }

    private void setCardData(Contributor contributor) {
        tvName.setText(contributor.getName());
        tvNick.setText(contributor.getNick());

        llLinkedIn.setVisibility(contributor.getLinkedInUrl() != null ? View.VISIBLE : View.GONE);
        llGooglePlayStore.setVisibility(contributor.getGooglePlayStoreUrl() != null ? View.VISIBLE : View.GONE);
        llWww.setVisibility(contributor.getWebsiteUrl() != null ? View.VISIBLE : View.GONE);
    }
}
