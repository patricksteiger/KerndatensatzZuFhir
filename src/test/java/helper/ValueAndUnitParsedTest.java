package helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueAndUnitParsedTest {

  @Test
  void testFromStringSimple() {
    String value = "75";
    String unit = "mg";
    String toTest = getValueStr(value) + getUnitStr(unit);
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
    String toTest = "  " + getValueStr(value) + "   " + getUnitStr(unit) + " ";
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringFloat() {
    String value = "12.7";
    String unit = "g/ml";
    String toTest = getValueStr(value) + getUnitStr(unit);
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringNoUnit() {
    String value = "123";
    String toTest = getValueStr(value);
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals("", parsed.getUnit());
  }

  @Test
  void TestFromStringNoValue() {
    String unit = "g";
    String toTest = getUnitStr(unit);
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(toTest);
    assertEquals("", parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  private String getValueStr(String value) {
    return "value=\"" + value + "\"";
  }

  private String getUnitStr(String unit) {
    return "unit=\"" + unit + "\"";
  }
}
