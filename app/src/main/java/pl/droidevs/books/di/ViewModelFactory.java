package pl.droidevs.books.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<? extends ViewModel> creator = this.creators.get(modelClass);

        if (creator == null) {
            creator = getCreatorFromCreatorSet(modelClass);
        }

        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Provider<? extends ViewModel> getCreatorFromCreatorSet(Class<?> modelClass) {

        for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {

            if (modelClass.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
