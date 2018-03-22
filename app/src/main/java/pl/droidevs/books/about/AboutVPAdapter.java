package pl.droidevs.books.about;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import pl.droidevs.books.R;

/**
 * Created by micha on 13.03.2018.
 */

public class AboutVPAdapter extends FragmentPagerAdapter {

    private final FragmentManager fragmentManager;
    private final Context context;
    public AboutVPAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AboutFragment.newInstance();
            case 1:
                return OurTeamFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.about);
            case 1:
                return context.getString(R.string.about_our_team_title);
        }
        return "null";
    }
}
