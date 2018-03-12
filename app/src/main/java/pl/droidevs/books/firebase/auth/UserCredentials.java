package pl.droidevs.books.firebase.auth;

import javax.annotation.Nullable;

public class UserCredentials {

    @Nullable
    private final String email;
    @Nullable
    private final String password;
    @Nullable
    private final String phone;

    public UserCredentials(@Nullable String email, @Nullable String password, @Nullable String phone) {
        this.email = email;
        this.password = password;
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
