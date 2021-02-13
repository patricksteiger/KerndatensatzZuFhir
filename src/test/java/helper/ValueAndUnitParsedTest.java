package helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueAndUnitParsedTest {

  @Test
  void testFromStringSimple() {
    String value = "75";
    String unit = "mg";
    String toTest = value + unit;
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringEmpty() {
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(null);
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
    parsed = ValueAndUnitParsed.fromString("");
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
    parsed = ValueAndUnitParsed.fromString("   ");
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
  }

  @Test
  void testFromStringSpaces() {
    String value = "75";
    String unit = "mg";
    String toTest = "   " + value + "  " + unit + "  ";
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringFloat() {
    String value = "12.7";
    String unit = "g/ml";
    String toTest = value + unit;
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringNoUnit() {
    String value = "123";
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(value);
    assertEquals(value, parsed.getValue());
    assertEquals("", parsed.getUnit());
  }

  @Test
  void TestFromStringNoValue() {
    String unit = "g";
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(unit);
    assertEquals("", parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }
}
