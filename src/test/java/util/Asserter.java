package util;

import constants.Constants;
import interfaces.Code;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;

import static org.junit.jupiter.api.Assertions.*;

public class Asserter {
  private Asserter() {}

  public static void assertIdentifier(
      String expectedValue, String expectedSystem, Code expectedCode, Identifier identifier) {
    assertNonEmptyValue(identifier);
    assertEquals(expectedValue, identifier.getValue());
    assertEquals(expectedSystem, identifier.getSystem());
    assertEquals(1, identifier.getType().getCoding().size());
    assertCoding(expectedCode, identifier.getType().getCoding().get(0));
  }

  public static void assertCodeableConcept(
      String expectedCode,
      String expectedSystem,
      String expectedDisplay,
      CodeableConcept codeableConcept) {
    assertNonEmptyValue(codeableConcept);
    assertEquals(1, codeableConcept.getCoding().size());
    assertCoding(expectedCode, expectedSystem, expectedDisplay, codeableConcept.getCoding().get(0));
  }

  public static void assertCodeableConcept(Code expectedCode, CodeableConcept codeableConcept) {
    assertNonEmptyValue(codeableConcept);
    assertEquals(1, codeableConcept.getCoding().size());
    assertCoding(expectedCode, codeableConcept.getCoding().get(0));
  }

  public static void assertCoding(Code expectedCode, Coding coding) {
    assertEquals(expectedCode.getCode(), coding.getCode());
    assertEquals(expectedCode.getSystem(), coding.getSystem());
    assertEquals(expectedCode.getDisplay(), coding.getDisplay());
  }

  public static void assertCoding(
      String expectedCode, String expectedSystem, String expectedDisplay, Coding coding) {
    assertEquals(expectedCode, coding.getCode());
    assertEquals(expectedSystem, coding.getSystem());
    assertEquals(expectedDisplay, coding.getDisplay());
  }

  public static void assertPeriod(String expectedStart, String expectedEnd, Period period) {
    if (expectedStart == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getStart());
    } else {
      assertEquals(expectedStart, period.getStart().toLocaleString());
    }
    if (expectedEnd == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getEnd());
    } else {
      assertEquals(expectedEnd, period.getEnd().toLocaleString());
    }
  }

  public static <T> void assertEmptyValue(T value) {
    assertNull(value);
  }

  public static <T> void assertNonEmptyValue(T value) {
    assertNotNull(value);
  }
}
