package pl.droidevs.books.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pl.droidevs.books.app.BookApplication;
import pl.droidevs.books.reactive.Schedulers;

/**
 * Module for application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context context(BookApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    Schedulers shedulers() {
        return new Schedulers() {
            @Override
            public Scheduler getSubscriber() {
                return io.reactivex.schedulers.Schedulers.io();
            }

            @Override
            public Scheduler getObserver() {
                return AndroidSchedulers.mainThread();
            }
        };
    }
}
