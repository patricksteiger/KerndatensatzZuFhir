package util;

import constants.Constants;
import helper.Helper;
import interfaces.Code;
import org.hl7.fhir.r4.model.*;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  public static void assertIdentifier(
      String expectedValue,
      String expectedSystem,
      Identifier.IdentifierUse expectedUse,
      Identifier identifier) {
    assertNonEmptyValue(identifier);
    assertEquals(expectedValue, identifier.getValue());
    assertEquals(expectedSystem, identifier.getSystem());
    assertEquals(expectedUse, identifier.getUse());
  }

  public static void assertIdentifier(
      String expectedValue, String expectedSystem, Identifier identifier) {
    assertNonEmptyValue(identifier);
    assertEquals(expectedValue, identifier.getValue());
    assertEquals(expectedSystem, identifier.getSystem());
  }

  public static void assertReference(
      String expectedType, String expectedValue, String expectedSystem, Reference reference) {
    assertNonEmptyValue(reference);
    assertEquals(expectedType, reference.getType());
    assertTrue(reference.hasIdentifier());
    assertIdentifier(expectedValue, expectedSystem, reference.getIdentifier());
  }

  public static void assertReference(String expectedReference, Reference reference) {
    assertNonEmptyValue(reference);
    assertEquals(expectedReference, reference.getReference());
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

  public static void assertCodeableConcept(
      String expectedCode, String expectedSystem, CodeableConcept codeableConcept) {
    assertCodeableConcept(expectedCode, expectedSystem, null, codeableConcept);
  }

  public static void assertCodeableConcept(Code expectedCode, CodeableConcept codeableConcept) {
    assertNonEmptyValue(codeableConcept);
    assertEquals(1, codeableConcept.getCoding().size());
    assertCoding(expectedCode, codeableConcept.getCoding().get(0));
  }

  public static void assertCoding(Code expectedCode, Coding coding) {
    assertNonEmptyValue(coding);
    assertEquals(expectedCode.getCode(), coding.getCode());
    assertEquals(expectedCode.getSystem(), coding.getSystem());
    assertEquals(expectedCode.getDisplay(), coding.getDisplay());
  }

  public static void assertCoding(
      String expectedCode, String expectedSystem, String expectedDisplay, Coding coding) {
    assertNonEmptyValue(coding);
    assertEquals(expectedCode, coding.getCode());
    assertEquals(expectedSystem, coding.getSystem());
    assertEquals(expectedDisplay, coding.getDisplay());
  }

  public static void assertCoding(String expectedCode, String expectedSystem, Coding coding) {
    assertNonEmptyValue(coding);
    assertEquals(expectedCode, coding.getCode());
    assertEquals(expectedSystem, coding.getSystem());
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

  public static void assertExtensionWithStringType(
      String expectedValue, String expectedUrl, Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl());
    assertTrue(extension.hasValue());
    assertTrue(extension.getValue() instanceof StringType);
    StringType value = (StringType) extension.getValue();
    assertEquals(expectedValue, value.getValue());
  }

  public static void assertDateTimeType(Date date, DateTimeType dateTimeType) {
    assertNonEmptyValue(dateTimeType);
    assertEquals(date, dateTimeType.getValue());
  }

  public static void assertHumanName(
      String expectedFamily,
      List<String> expectedGiven,
      String expectedPrefix,
      HumanName.NameUse expectedUse,
      HumanName humanName) {
    assertNonEmptyValue(humanName);
    assertEquals(expectedFamily, humanName.getFamily());
    if (expectedGiven == Constants.getEmptyValue()) {
      assertEquals(Helper.listOf(), humanName.getGiven());
    } else {
      List<StringType> given = humanName.getGiven();
      assertNonEmptyValue(given);
      assertEquals(expectedGiven, Helper.listMap(given, StringType::getValue));
    }
    if (expectedPrefix == Constants.getEmptyValue()) {
      assertEquals(Helper.listOf(), humanName.getPrefix());
    } else {
      List<StringType> prefix = humanName.getPrefix();
      assertNonEmptyValue(prefix);
      assertEquals(Helper.listOf(expectedPrefix), Helper.listMap(prefix, StringType::getValue));
    }
    assertEquals(expectedUse, humanName.getUse());
  }

  public static void assertHumanName(
      String expectedFamily, HumanName.NameUse expectedUse, HumanName humanName) {
    assertHumanName(
        expectedFamily,
        Constants.getEmptyValue(),
        Constants.getEmptyValue(),
        expectedUse,
        humanName);
  }

  public static void assertQuantity(
      BigDecimal expectedValue,
      String expectedCode,
      String expectedSystem,
      String expectedUnit,
      Quantity quantity) {
    assertNonEmptyValue(quantity);
    assertEquals(expectedValue, quantity.getValue());
    assertEquals(expectedCode, quantity.getCode());
    assertEquals(expectedSystem, quantity.getSystem());
    assertEquals(expectedUnit, quantity.getUnit());
  }

  public static void assertRatio(
      BigDecimal expectedNumeratorValue,
      String expectedNumeratorUnit,
      BigDecimal expectedDenominatorValue,
      String expectedDenominatorUnit,
      Ratio ratio) {
    assertNonEmptyValue(ratio);
    assertQuantity(
        expectedNumeratorValue,
        expectedNumeratorUnit,
        Constants.QUANTITY_SYSTEM,
        Ucum.formalRepresentation(expectedNumeratorUnit).get(),
        ratio.getNumerator());
    assertQuantity(
        expectedDenominatorValue,
        expectedDenominatorUnit,
        Constants.QUANTITY_SYSTEM,
        Ucum.formalRepresentation(expectedDenominatorUnit).get(),
        ratio.getDenominator());
  }

  /*public static void assertLoggerHasCalledError3(Logger logger, int times) {
    verify(logger, times(times)).error(anyString(), anyString(), anyString());
  }

  public static void assertLoggerHasCalledError2(Logger logger, int times) {
    verify(logger, times(times)).error(anyString(), anyString());
  }

  public static void assertLoggerHasCalledEmptyValue(Logger logger, int times) {
    verify(logger, times(times)).emptyValue(anyString(), anyString());
  }

  public static void assertLoggerHasCalledWarning(Logger logger, int times) {
    verify(logger, times(times)).warning(any(), anyString(), anyString());
  }*/

  public static void assertAdministrativeGender(
      String code, Optional<Enumeration<Enumerations.AdministrativeGender>> expectedGender) {
    assertTrue(expectedGender.isPresent());
    assertEquals(code, expectedGender.get().getCode());
  }

  public static void assertAdministrativeGenderUnbestimmt(
      Optional<Enumeration<Enumerations.AdministrativeGender>> expectedGender) {
    assertTrue(expectedGender.isPresent());
    Enumeration<Enumerations.AdministrativeGender> result = expectedGender.get();
    assertEquals("other", result.getCode());
    String EXTENSION_URL = "http://fhir.de/StructureDefinition/gender-amtlich-de";
    assertTrue(result.hasExtension(EXTENSION_URL));
    Extension extension = result.getExtensionByUrl(EXTENSION_URL);
    String CODING_SYSTEM = "http://fhir.de/CodeSystem/gender-amtlich-de";
    assertExtensionWithCoding("X", CODING_SYSTEM, "unbestimmt", EXTENSION_URL, extension);
  }

  public static void assertAdministrativeGenderDivers(
      Optional<Enumeration<Enumerations.AdministrativeGender>> expectedGender) {
    assertTrue(expectedGender.isPresent());
    Enumeration<Enumerations.AdministrativeGender> result = expectedGender.get();
    assertEquals("other", result.getCode());
    String EXTENSION_URL = "http://fhir.de/StructureDefinition/gender-amtlich-de";
    assertTrue(result.hasExtension(EXTENSION_URL));
    Extension extension = result.getExtensionByUrl(EXTENSION_URL);
    String CODING_SYSTEM = "http://fhir.de/CodeSystem/gender-amtlich-de";
    assertExtensionWithCoding("D", CODING_SYSTEM, "divers", EXTENSION_URL, extension);
  }

  public static void assertAnnotation(String expectedText, Annotation annotation) {
    assertNonEmptyValue(annotation);
    assertEquals(expectedText, annotation.getText());
  }

  public static <T> void assertEmptyValue(T value) {
    assertNull(value);
  }

  public static <T> void assertAllEmptyValue(List<T> values) {
    assertNonEmptyValue(values);
    values.forEach(Asserter::assertEmptyValue);
  }

  public static <T> void assertNonEmptyValue(T value) {
    assertNotNull(value);
  }

  public static <T> void assertAllNonEmptyValue(List<T> values) {
    assertNonEmptyValue(values);
    values.forEach(Asserter::assertNonEmptyValue);
  }
}
