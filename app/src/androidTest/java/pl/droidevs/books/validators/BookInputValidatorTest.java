package pl.droidevs.books.validators;

import org.junit.Test;
import static org.junit.Assert.*;
import static pl.droidevs.books.validators.BookInputValidator.*;

/**
 *  A Test class for all methods in BookInputValidator class
 */
public class BookInputValidatorTest {

    /** Tests for method isCoverUrlValid(String imageURl) */
    @Test
    public void isCoverUrlValidMustReturnTrue() {
        assertEquals(true, isCoverUrlValid("https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882"));
    }
    @Test
    public void isCoverUrlValidMustReturnFalse() {
        assertEquals(false, isCoverUrlValid("0132350882"));
    }


    /** Tests for method isYearValid(String year) */
    @Test
    public void isYearValidMustReturnTrue() {
        assertEquals(true, isYearValid("1997"));
    }
    @Test
    public void isYearValidMustReturnFalse() {
        assertEquals(false, isYearValid("50000"));
    }

    /** Tests for method isTitleValid(String title) */
    @Test
    public void isTitleValidMustReturnTrue() {
        assertEquals(true, isTitleValid("Clean Code"));
    }
    @Test
    public void isTitleValidMustReturnFalse() {
        assertEquals(false, isTitleValid("cl"));
    }

    /** Tests for method isAuthorValid(String author) */
    @Test
    public void isAuthorValidMustReturnTrue() {
        assertEquals(true, isAuthorValid("Robert C. Martin"));
    }
    @Test
    public void isAuthorValidMustReturnFalse() {
        assertEquals(false, isAuthorValid("RM"));
    }



}