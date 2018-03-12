package pl.droidevs.books.firebase.auth.providers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.subjects.PublishSubject;
import pl.droidevs.books.firebase.auth.UserCredentials;
import pl.droidevs.books.firebase.auth.responses.Status;

public class AnonymousProvider extends BaseProvider {

    public AnonymousProvider(PublishSubject<Error> errorPublishSubject, PublishSubject<Status> statusPublishSubject) {
        super(errorPublishSubject, statusPublishSubject);
    }

    @Override
    void init(Context context, String defaultWebClientId) {
        // NOOP
    }

    @Override
    void login(Activity activity, UserCredentials userCredentials) {
        auth.signInAnonymously().addOnCompleteListener(task -> loginTask(task, null));
    }

    @NonNull
    @Override
    String getProviderId() {
        return "";
    }
}
