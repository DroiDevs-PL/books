package pl.droidevs.books.firebase.auth.responses;

public class FirebaseException {

    public static class EmptyEmailException extends Exception {
    }

    public static class WrongPasswordException extends Exception {
    }

    public static class EmailAlreadyUsedException extends Exception {
    }

    public static class WeakPasswordException extends Exception {
    }

    public static class LoginFailedException extends Exception {
    }

    public static class ResetPasswordException extends Exception {
    }

    public static class UserNotLoggedInException extends Exception {
    }

    public static class VerificationEmailSendingException extends Exception {
    }

    public static class EmptyPhoneNumberException extends Exception {
    }

    public static class InvalidUserException extends Exception {
    }

    public static class UnknownException extends Throwable {
    }
}