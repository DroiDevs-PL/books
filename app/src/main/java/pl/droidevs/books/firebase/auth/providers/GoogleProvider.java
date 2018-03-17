package pl.droidevs.books.firebase.auth.providers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import pl.droidevs.books.firebase.auth.responses.FirebaseException;
import timber.log.Timber;

public class GoogleProvider extends BaseProvider {

    private static final int GOOGLE_LOGIN_CODE = 123;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private SingleEmitter<FirebaseUser> emitter;

    @Override
    public void init(Context context, String defaultWebClientId) {
        Timber.d("GoogleAuth defaultWebClientId %s", defaultWebClientId);
        googleSignInOptions = new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestEmail()
            .build();

        googleApiClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .addOnConnectionFailedListener(error -> Timber.e("Google login error: %s", error))
            .build();

        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Timber.d("GoogleAuth onConnected");
            }

            @Override
            public void onConnectionSuspended(int i) {
                Timber.d("GoogleAuth onConnectionSuspended");
            }
        });
    }

    public Single<FirebaseUser> login(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE);
        return Single.create(emitter ->
            this.emitter = emitter
        );
    }

    @NonNull
    @Override
    String getProviderId() {
        return GoogleAuthProvider.PROVIDER_ID;
    }

    public void handleLogin(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount account = result.getSignInAccount();
        Timber.d("Google account %s", account);
        if (account == null) {
            Timber.e("Google login failed");
            if (result.getStatus().getStatusCode() == 10) {
                emitter.onError(new FirebaseException.DeveloperErrorException());
            } else {
                emitter.onError(new FirebaseException.UserNotLoggedInException());
            }
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Timber.d("Google credential %s", credential);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                signIn(credential).subscribe(emitter::onSuccess, emitter::onError);
            } else {
                emitter.onError(new FirebaseException.UserNotLoggedInException());
            }
        });
    }
}
