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

  private static final String CODE = "code";
  private static final String SYSTEM = "system";
  private static final String DISPLAY = "display";
  private static final String VALUE = "value";
  private static final String USE = "use";
  private static final String TYPE = "type";
  private static final String IDENTIFIER = "identifier";
  private static final String REFERENCE = "reference";
  private static final String START_DATE = "start date";
  private static final String END_DATE = "end date";
  private static final String URL = "url";
  private static final String UNIT = "unit";
  private static final String TEXT = "text";
  private static final String FAMILY_NAME = "family name";
  private static final String GIVEN_NAME = "given name";
  private static final String NAME_PREFIX = "name prefix";
  private static final String TYPE_CODING = "type expected: Coding";
  private static final String TYPE_CODEABLE_CONCEPT = "type expected: CodeableConcept";
  private static final String TYPE_STRINGTYPE = "type expected: StringType";
  private static final String NUMBER_OF_CODINGS = "number of codings";

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
    assertEquals(expectedValue, identifier.getValue(), VALUE);
    assertEquals(expectedSystem, identifier.getSystem(), SYSTEM);
    assertEquals(1, identifier.getType().getCoding().size(), NUMBER_OF_CODINGS);
    assertCoding(expectedCode, identifier.getType().getCoding().get(0));
  }

  public static void assertIdentifier(
      String expectedValue,
      String expectedSystem,
      Identifier.IdentifierUse expectedUse,
      Identifier identifier) {
    assertNonEmptyValue(identifier);
    assertEquals(expectedValue, identifier.getValue(), VALUE);
    assertEquals(expectedSystem, identifier.getSystem(), SYSTEM);
    assertEquals(expectedUse, identifier.getUse(), USE);
  }

  public static void assertIdentifier(
      String expectedValue, String expectedSystem, Identifier identifier) {
    assertNonEmptyValue(identifier);
    assertEquals(expectedValue, identifier.getValue(), VALUE);
    assertEquals(expectedSystem, identifier.getSystem(), SYSTEM);
  }

  public static void assertReference(
      String expectedType, String expectedValue, String expectedSystem, Reference reference) {
    assertNonEmptyValue(reference);
    assertEquals(expectedType, reference.getType(), TYPE);
    assertTrue(reference.hasIdentifier(), IDENTIFIER);
    assertIdentifier(expectedValue, expectedSystem, reference.getIdentifier());
  }

  public static void assertReference(String expectedReference, Reference reference) {
    assertNonEmptyValue(reference);
    assertEquals(expectedReference, reference.getReference(), REFERENCE);
  }

  public static void assertCodeableConcept(
      String expectedCode,
      String expectedSystem,
      String expectedDisplay,
      CodeableConcept codeableConcept) {
    assertNonEmptyValue(codeableConcept);
    assertEquals(1, codeableConcept.getCoding().size(), NUMBER_OF_CODINGS);
    assertCoding(expectedCode, expectedSystem, expectedDisplay, codeableConcept.getCoding().get(0));
  }

  public static void assertCodeableConcept(
      String expectedCode, String expectedSystem, CodeableConcept codeableConcept) {
    assertCodeableConcept(expectedCode, expectedSystem, null, codeableConcept);
  }

  public static void assertCodeableConcept(Code expectedCode, CodeableConcept codeableConcept) {
    assertNonEmptyValue(codeableConcept);
    assertEquals(1, codeableConcept.getCoding().size(), NUMBER_OF_CODINGS);
    assertCoding(expectedCode, codeableConcept.getCoding().get(0));
  }

  public static void assertCoding(Code expectedCode, Coding coding) {
    assertCoding(
        expectedCode.getCode(), expectedCode.getSystem(), expectedCode.getDisplay(), coding);
  }

  public static void assertCoding(
      String expectedCode, String expectedSystem, String expectedDisplay, Coding coding) {
    assertNonEmptyValue(coding);
    assertEquals(expectedCode, coding.getCode(), CODE);
    assertEquals(expectedSystem, coding.getSystem(), SYSTEM);
    assertEquals(expectedDisplay, coding.getDisplay(), DISPLAY);
  }

  public static void assertCoding(String expectedCode, String expectedSystem, Coding coding) {
    assertNonEmptyValue(coding);
    assertEquals(expectedCode, coding.getCode(), CODE);
    assertEquals(expectedSystem, coding.getSystem(), SYSTEM);
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
    assertNonEmptyValue(period);
    assertEquals(expectedStart, period.getStart(), START_DATE);
    assertEquals(expectedEnd, period.getEnd(), END_DATE);
  }

  public static void assertExtensionWithCoding(
      String expectedCode,
      String expectedSystem,
      String expectedDisplay,
      String expectedUrl,
      Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl(), URL);
    assertTrue(extension.hasValue(), VALUE);
    assertTrue(extension.getValue() instanceof Coding, TYPE_CODING);
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
      String expectedCode,
      String expectedSystem,
      String expectedDisplay,
      String expectedUrl,
      Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl(), URL);
    assertTrue(extension.hasValue(), VALUE);
    assertTrue(extension.getValue() instanceof CodeableConcept, TYPE_CODEABLE_CONCEPT);
    CodeableConcept value = (CodeableConcept) extension.getValue();
    assertCodeableConcept(expectedCode, expectedSystem, expectedDisplay, value);
  }

  public static void assertExtensionWithStringType(
      String expectedValue, String expectedUrl, Extension extension) {
    assertNonEmptyValue(extension);
    assertEquals(expectedUrl, extension.getUrl(), URL);
    assertTrue(extension.hasValue(), VALUE);
    assertTrue(extension.getValue() instanceof StringType, TYPE_STRINGTYPE);
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
    assertEquals(expectedFamily, humanName.getFamily(), FAMILY_NAME);
    if (expectedGiven == Constants.getEmptyValue()) {
      assertEquals(Helper.listOf(), humanName.getGiven(), GIVEN_NAME);
    } else {
      List<StringType> given = humanName.getGiven();
      assertNonEmptyValue(given);
      assertEquals(expectedGiven, Helper.listMap(given, StringType::getValue), GIVEN_NAME);
    }
    if (expectedPrefix == Constants.getEmptyValue()) {
      assertEquals(Helper.listOf(), humanName.getPrefix(), NAME_PREFIX);
    } else {
      List<StringType> prefix = humanName.getPrefix();
      assertNonEmptyValue(prefix);
      assertEquals(
          Helper.listOf(expectedPrefix), Helper.listMap(prefix, StringType::getValue), NAME_PREFIX);
    }
    assertEquals(expectedUse, humanName.getUse(), USE);
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
    assertEquals(expectedValue, quantity.getValue(), VALUE);
    assertEquals(expectedCode, quantity.getCode(), CODE);
    assertEquals(expectedSystem, quantity.getSystem(), SYSTEM);
    assertEquals(expectedUnit, quantity.getUnit(), UNIT);
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
    assertEquals(code, expectedGender.get().getCode(), CODE);
  }

  public static void assertAdministrativeGenderUnbestimmt(
      Optional<Enumeration<Enumerations.AdministrativeGender>> expectedGender) {
    assertTrue(expectedGender.isPresent());
    Enumeration<Enumerations.AdministrativeGender> result = expectedGender.get();
    assertEquals("other", result.getCode(), CODE);
    String EXTENSION_URL = "http://fhir.de/StructureDefinition/gender-amtlich-de";
    assertTrue(result.hasExtension(EXTENSION_URL), URL);
    Extension extension = result.getExtensionByUrl(EXTENSION_URL);
    String CODING_SYSTEM = "http://fhir.de/CodeSystem/gender-amtlich-de";
    assertExtensionWithCoding("X", CODING_SYSTEM, "unbestimmt", EXTENSION_URL, extension);
  }

  public static void assertAdministrativeGenderDivers(
      Optional<Enumeration<Enumerations.AdministrativeGender>> expectedGender) {
    assertTrue(expectedGender.isPresent());
    Enumeration<Enumerations.AdministrativeGender> result = expectedGender.get();
    assertEquals("other", result.getCode(), CODE);
    String EXTENSION_URL = "http://fhir.de/StructureDefinition/gender-amtlich-de";
    assertTrue(result.hasExtension(EXTENSION_URL), URL);
    Extension extension = result.getExtensionByUrl(EXTENSION_URL);
    String CODING_SYSTEM = "http://fhir.de/CodeSystem/gender-amtlich-de";
    assertExtensionWithCoding("D", CODING_SYSTEM, "divers", EXTENSION_URL, extension);
  }

  public static void assertAnnotation(String expectedText, Annotation annotation) {
    assertNonEmptyValue(annotation);
    assertEquals(expectedText, annotation.getText(), TEXT);
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
