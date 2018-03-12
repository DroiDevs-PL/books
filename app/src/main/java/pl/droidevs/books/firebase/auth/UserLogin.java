package pl.droidevs.books.firebase.auth;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;
import pl.droidevs.books.firebase.auth.responses.Status;

public class UserLogin {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final PublishSubject<Status> statusObservable = PublishSubject.create();
    private final PublishSubject<Error> errorObservable = PublishSubject.create();

    @Inject
    public UserLogin(Context context, String defaultWebClientId) {

    }

    public void test() {
        Log.d("TEST", "TEST");
    }
}
