package pl.droidevs.books.firebase.auth.providers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Single;

public class AnonymousProvider extends BaseProvider {

    @Override
    void init(Context context, String defaultWebClientId) {
        // NOOP
    }

    public Single<FirebaseUser> login() {
        return Single.create(emitter ->
            auth.signInAnonymously().addOnCompleteListener(task -> loginTask(task, null)
                .subscribe(
                    emitter::onSuccess,
                    emitter::onError)
            )
        );
    }

    @NonNull
    @Override
    String getProviderId() {
        return "";
    }
}
