package pl.droidevs.books.firebase.auth;

import android.support.annotation.NonNull;

import javax.annotation.Nullable;

public class UserCredentials {

    @Nullable
    private final String email;
    @Nullable
    private final String password;
    @Nullable
    private final String phone;


    // TODO: 2018-03-12 use builder maybe?
    public UserCredentials(@Nullable String email, @Nullable String password, @Nullable String phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public UserCredentials(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
        phone = null;
    }

    public UserCredentials(@NonNull String phone) {
        this.email = null;
        this.password = null;
        this.phone = phone;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }
}
