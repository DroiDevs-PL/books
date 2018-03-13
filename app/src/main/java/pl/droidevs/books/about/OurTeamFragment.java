package pl.droidevs.books.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;

public class OurTeamFragment extends Fragment {

    private OurTeamRVAdapter adapter;

    @BindView(R.id.rv_team)
    RecyclerView rvTeam;

    public static OurTeamFragment newInstance() {
        return new OurTeamFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_team, container, false);
        ButterKnife.bind(this, view);

        adapter = new OurTeamRVAdapter(getContext());
        rvTeam.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTeam.setAdapter(adapter);

        return view;
    }
}
