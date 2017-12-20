package pl.droidevs.books.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.droidevs.books.app.BookApplication;

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
}
