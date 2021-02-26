package helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParsedQuantityTest {

  @Test
  void testFromStringSimple() {
    String value = "75";
    String unit = "mg";
    String toTest = getValueStr(value) + getUnitStr(unit);
    ParsedQuantity parsed = ParsedQuantity.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringEmpty() {
    ParsedQuantity parsed = ParsedQuantity.fromString(null);
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
    parsed = ParsedQuantity.fromString("");
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
    parsed = ParsedQuantity.fromString("   ");
    assertEquals("", parsed.getValue());
    assertEquals("", parsed.getUnit());
  }

  @Test
  void testFromStringSpaces() {
    String value = "75";
    String unit = "mg";
    String toTest = "  " + getValueStr(value) + "   " + getUnitStr(unit) + " ";
    ParsedQuantity parsed = ParsedQuantity.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringFloat() {
    String value = "12.7";
    String unit = "g/ml";
    String toTest = getValueStr(value) + getUnitStr(unit);
    ParsedQuantity parsed = ParsedQuantity.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals(unit, parsed.getUnit());
  }

  @Test
  void testFromStringNoUnit() {
    String value = "123";
    String toTest = getValueStr(value);
    ParsedQuantity parsed = ParsedQuantity.fromString(toTest);
    assertEquals(value, parsed.getValue());
    assertEquals("", parsed.getUnit());
  }

  @Test
  void TestFromStringNoValue() {
    String unit = "g";
    String toTest = getUnitStr(unit);
    ParsedQuantity parsed = ParsedQuantity.fromString(toTest);
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
