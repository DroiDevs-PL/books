package pl.droidevs.books.about;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by micha on 09.03.2018.
 */

public class AboutViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedContributorIndex;

    public LiveData<Integer> getSelectedContributorIndex() {
        if (selectedContributorIndex == null) {
            selectedContributorIndex = new MutableLiveData<>();
            selectedContributorIndex.setValue(0);
        }
        return selectedContributorIndex;
    }

    public void selectContributor(int index) {
        if (selectedContributorIndex == null) {
            selectedContributorIndex = new MutableLiveData<>();
        }
        selectedContributorIndex.postValue(index);
    }
}
