package pl.droidevs.books.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;

/**
 * Created by micha on 16.03.2018.
 */

public class ContributorFragment extends Fragment {
    public static final String TAG = ContributorFragment.class.getSimpleName();

    private static final String CONTRIBUTOR_INDEX = "CONTRIBUTOR_INDEX";

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_nick)
    TextView tvNick;

    @BindView(R.id.tv_github)
    TextView tvGithub;

    public static ContributorFragment newInstance(int contributorIndex) {
        ContributorFragment fragment = new ContributorFragment();
        Bundle args = new Bundle();
        args.putInt(CONTRIBUTOR_INDEX, contributorIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contributor, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            int contributorIndex = args.getInt(CONTRIBUTOR_INDEX);
            Contributor contributor = Contributor.getTeam().get(contributorIndex);
            tvName.setText(contributor.getName());
            tvGithub.setText(contributor.getNick());
            Glide.with(this).load(contributor.getResourceImage()).into(ivAvatar);
        }

        return view;
    }

    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contributor);
        ButterKnife.bind(this);
    }*/

   /* @Override
    public void init(Bundle savedInstanceState) {
        setTitle("Activity Title");

        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );

        setContentView(R.layout.fragment_contributor);
        ButterKnife.bind(this);
    }*/
}
