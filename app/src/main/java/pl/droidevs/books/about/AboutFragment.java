package pl.droidevs.books.about;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.droidevs.books.R;

/**
 * Created by micha on 09.03.2018.
 */

public class AboutFragment extends Fragment {
    public static final String TAG = AboutFragment.class.getSimpleName();

    private AboutViewModel viewModel;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_about, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(AboutViewModel.class);

        ((TextView) view.findViewById(R.id.tv_for_who_description)).setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
