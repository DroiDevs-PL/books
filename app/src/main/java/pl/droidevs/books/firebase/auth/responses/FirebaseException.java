package pl.droidevs.books.firebase.auth.responses;

public class FirebaseException {

    public static class EmptyEmailException extends Exception {
    }

    public static class WrongPasswordException extends Exception {
    }

    public static class EmailAlreadyUsedException extends Throwable {
    }

    public static class WeakPasswordException extends Throwable {
    }

    public static class LoginFailedException extends Throwable {
    }

    public static class ResetPasswordException extends Throwable {
    }

    public static class UserNotLoggedInException extends Throwable {
    }

    public static class VerificationEmailSendingException extends Throwable {
    }

    public static class EmptyPhoneNumberException extends Throwable {
    }

    public static class UnknownException extends Throwable {
    }
}