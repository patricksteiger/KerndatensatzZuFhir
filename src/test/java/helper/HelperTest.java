package helper;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HelperTest {
  @Test
  public void testExtractCode() {
    String code = "code=";
    List<String> words = new ArrayList<>();
    assertEquals("", Helper.extractCode(words, code));
    words.add("BadStart");
    assertEquals("", Helper.extractCode(words, code));
    words.add("code=\"20570-8\"");
    assertEquals("20570-8", Helper.extractCode(words, code));
    words.add(0, "code=123");
    assertEquals("123", Helper.extractCode(words, code));
  }

  @Test
  public void testSplitCode() {
    String nullStr = null;
    assertEquals(0, Helper.splitCode(nullStr).size());
    String emptyStr = "";
    assertEquals(0, Helper.splitCode(emptyStr).size());
    String simpleCode = "1234";
    List<String> values = Helper.splitCode(simpleCode);
    assertEquals(1, values.size());
    assertEquals("1234", values.get(0));
    String codeSystem = "  system=\"http://loinc.org\"";
    String codeCode = "code=\"20570-8\"  ";
    String code = codeSystem + "\t \n " + codeCode;
    values = Helper.splitCode(code);
    assertEquals(2, values.size());
    assertEquals(codeSystem.trim(), values.get(0));
    assertEquals(codeCode.trim(), values.get(1));
  }

  @Test
  public void testTrimQuotes() {
    String onlyQuotes = "\"\"";
    assertEquals("", Helper.trimQuotes(onlyQuotes));
    String normalQuotes = "\"abc\"";
    assertEquals("abc", Helper.trimQuotes(normalQuotes));
  }

  @Test
  public void testTrimCharacter() {
    String emptySpaces = "     ";
    assertEquals("", Helper.trimCharacter(emptySpaces, ' '));
    String wrappingSpaces = "  abc   ";
    assertEquals("abc", Helper.trimCharacter(wrappingSpaces, ' '));
    String onlyRightSideSpaces = "ab ";
    assertEquals("ab", Helper.trimCharacter(onlyRightSideSpaces, ' '));
    String onlyLeftSideSpaces = " abc";
    assertEquals("abc", Helper.trimCharacter(onlyLeftSideSpaces, ' '));
    String noSpaces = "abc";
    assertEquals("abc", Helper.trimCharacter(noSpaces, ' '));
    String empty = "";
    assertEquals("", Helper.trimCharacter(empty, ' '));
    assertNull(Helper.trimCharacter(null, ' '));
    String interspersedSpaces = " a b c ";
    assertEquals("a b c", Helper.trimCharacter(interspersedSpaces, ' '));
  }

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
