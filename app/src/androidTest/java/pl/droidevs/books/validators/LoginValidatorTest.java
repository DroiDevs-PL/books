package pl.droidevs.books.validators;

import org.junit.Test;
import static org.junit.Assert.*;
import static pl.droidevs.books.validators.LoginValidator.*;

/**
 *  A Test class LoginValidator class
 */

public class LoginValidatorTest {

    @Test
    public void EmptyLogin() {
        assertEquals(false, isLoginValid(""));
    }
    @Test
    public void TooLittleCharactersLogin() {
        assertEquals(false, isLoginValid("ml"));
    }
    @Test
    public void LoginCanProceed() {
        assertEquals(true, isLoginValid("Maria"));
    }
}