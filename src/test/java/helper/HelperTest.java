package helper;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {
    @Test
    public void testGetDateFromGermanTime() {
        String germanTime = "10.1.2020";
        Date received = Helper.getDateFromGermanTime(germanTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        String formatted = dateFormat.format(received);
        // Take substring, because actual return is: "10.01.20 20:00"
        assertEquals("10.01.20", formatted.substring(0, 8));
    }

    @Test
    public void testCheckNonEmptyString() {
        String s = null;
        assertFalse(Helper.checkNonEmptyString(s));
        s = "";
        assertFalse(Helper.checkNonEmptyString(s));
        s = " ";
        assertTrue(Helper.checkNonEmptyString(s));
        s = "test";
        assertTrue(Helper.checkNonEmptyString(s));
    }
}
