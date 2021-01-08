package helper;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {
  @Test
  public void testGetDateFromISO() throws ParseException {
    assertThrows(NullPointerException.class, () -> Helper.getDateFromISO(null));
    assertThrows(DateTimeParseException.class, () -> Helper.getDateFromISO(""));
    assertThrows(DateTimeParseException.class, () -> Helper.getDateFromISO("20.10.2020"));
    assertThrows(DateTimeParseException.class, () -> Helper.getDateFromISO("2020-21-13"));
    assertThrows(DateTimeParseException.class, () -> Helper.getDateFromISO("2020-1-13"));
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    String simpleDate = "2020-07-21";
    Date actual = Helper.getDateFromISO(simpleDate);
    assertEquals(simpleDateFormat.parse(simpleDate + "T00:00:00"), actual);
    String date = simpleDate + "T13:34:15";
    actual = Helper.getDateFromISO(date);
    assertEquals(simpleDateFormat.parse(date), actual);
    actual = Helper.getDateFromISO(date + "+01:00");
    assertEquals(simpleDateFormat.parse(date), actual);
  }

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
