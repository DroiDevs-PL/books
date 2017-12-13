package pl.droidevs.books.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import pl.droidevs.books.app.BookApplication;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class,
        DataSourceModule.class,
        ViewModelModule.class})
public interface AppComponent {

    void inject(BookApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(BookApplication application);

        AppComponent build();
    }
}
