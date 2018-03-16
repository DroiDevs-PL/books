package pl.droidevs.books.about;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidevs.books.R;

public class AboutVPFragment extends Fragment {
    public static final String TAG = AboutVPFragment.class.getSimpleName();

    @BindView(R.id.vp_about)
    ViewPager vpAbout;
    @BindView(R.id.tab_layout_about)
    TabLayout tabLayoutAbout;

    private AboutVPAdapter adapter;


    public static AboutVPFragment newInstance() {
        return new AboutVPFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_vp, container, false);
        ButterKnife.bind(this, view);
        adapter = new AboutVPAdapter(getFragmentManager(), getContext());
        vpAbout.setAdapter(adapter);
        tabLayoutAbout.setupWithViewPager(vpAbout);
        tabLayoutAbout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayoutAbout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        return view;
    }
}
