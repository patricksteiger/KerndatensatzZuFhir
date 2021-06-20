package helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParsedCodeTest {
  @Test
  public void fromStringEmptyCode() {
    runTestCase(null, "", "", "");
    runTestCase("", "", "", "");
  }

  @Test
  public void fromStringSimpleFormat() {
    String noQuotes = "abc1234";
    runTestCase(noQuotes, noQuotes, "", "");
    String withQuotes = "\"" + noQuotes + "\"";
    runTestCase(withQuotes, noQuotes, "", "");
    String withSpaces = "1 2a3 4";
    runTestCase(withSpaces, withSpaces, "", "");
  }

  @Test
  public void fromStringComplexFormat() {
    String code = "final";
    String system = "http://hl7.org/fhir/ValueSet/diagnostic-report-status";
    String display = "Final";
    String codeCode = getCode("code=", code);
    String systemCode = getCode("system=", system);
    String displayCode = getCode("display=", display);
    runTestCase(codeCode, code, "", "");
    runTestCase(systemCode, "", system, "");
    runTestCase(displayCode, "", "", display);
    runTestCase(systemCode + " " + codeCode, code, system, "");
    runTestCase(codeCode + "  " + systemCode, code, system, "");
    runTestCase(systemCode + " " + displayCode, "", system, display);
    runTestCase(
        "  " + codeCode + "\n" + displayCode + "\t " + systemCode + " \n", code, system, display);
  }

  @Test
  public void hasDisplay() {
    ParsedCode parsedCode = ParsedCode.fromString("");
    assertFalse(parsedCode.hasDisplay());
    parsedCode = ParsedCode.fromString("display=\"test\"");
    assertTrue(parsedCode.hasDisplay());
  }

  private void runTestCase(
      String input, String expectedCode, String expectedSystem, String expectedDisplay) {
    ParsedCode parsedCode = ParsedCode.fromString(input);
    assertNotNull(parsedCode);
    assertEquals(expectedCode, parsedCode.getCode());
    assertEquals(expectedSystem, parsedCode.getSystem());
    assertEquals(expectedDisplay, parsedCode.getDisplay());
  }

  private String getCode(String prefix, String code) {
    return prefix + "\"" + code + "\"";
  }
}
