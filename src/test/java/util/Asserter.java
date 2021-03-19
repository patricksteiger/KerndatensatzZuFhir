package util;

import constants.Constants;
import interfaces.Code;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.getCodeStr;

public class Asserter {
  private Asserter() {}

  public static <T> void assertEmptyCodeValue(Consumer<String> setter, Supplier<T> getter) {
    setter.accept("");
    assertEmptyValue(getter.get());
    setter.accept(null);
    assertEmptyValue(getter.get());
    setter.accept(getCodeStr(""));
    assertEmptyValue(getter.get());
  }

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

  public static void assertCoding(
      String expectedCode, String expectedSystem, String expectedDisplay, List<Coding> codings) {
    assertNotNull(codings);
    for (Coding coding : codings) {
      if (expectedCode.equals(coding.getCode())) {
        assertCoding(expectedCode, expectedSystem, expectedDisplay, coding);
        return;
      }
    }
    fail("Codings does not contain code: " + expectedCode);
  }

  public static void assertPeriod(Date expectedStart, Date expectedEnd, Period period) {
    if (expectedStart == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getStart());
    } else {
      assertEquals(expectedStart, period.getStart());
    }
    if (expectedEnd == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getEnd());
    } else {
      assertEquals(expectedEnd, period.getEnd());
    }
  }

  public static void assertExtensionWithCoding(
      String expectedCode,
      String expectedSystem,
      String expectedDisplay,
      String expectedUrl,
      Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl());
    assertTrue(extension.hasValue());
    assertTrue(extension.getValue() instanceof Coding);
    assertCoding(expectedCode, expectedSystem, expectedDisplay, (Coding) extension.getValue());
  }

  public static void assertExtensionWithCoding(
      Code expectedCode, String expectedUrl, Extension extension) {
    assertExtensionWithCoding(
        expectedCode.getCode(),
        expectedCode.getSystem(),
        expectedCode.getDisplay(),
        expectedUrl,
        extension);
  }

  public static void assertExtensionWithCodeableConcept(
      Code expectedCode, String expectedUrl, Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl());
    assertTrue(extension.hasValue());
    assertTrue(extension.getValue() instanceof CodeableConcept);
    CodeableConcept value = (CodeableConcept) extension.getValue();
    assertCodeableConcept(expectedCode, value);
  }

  public static void assertDateTimeType(Date date, DateTimeType dateTimeType) {
    assertNonEmptyValue(dateTimeType);
    assertEquals(date, dateTimeType.getValue());
  }

  public static <T> void assertEmptyValue(T value) {
    assertNull(value);
  }

  public static <T> void assertNonEmptyValue(T value) {
    assertNotNull(value);
  }
}
