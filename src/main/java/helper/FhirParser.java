package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.Constants;
import interfaces.Code;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class FhirParser {
  private FhirParser() {}

  private static <T> T optionalFhirFromSystem(
      String kerndatensatzValue, String codeSystem, Function<ParsedCode, T> mapper) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    return parsedCode.hasEmptyCode() ? Constants.getEmptyValue() : mapper.apply(parsedCode);
  }

  public static Coding optionalCodingFromSystem(String kerndatensatzValue, String codeSystem) {
    return optionalFhirFromSystem(kerndatensatzValue, codeSystem, FhirGenerator::coding);
  }

  public static Coding codingFromSystem(
      String kerndatensatzValue, String codeSystem, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    return parsedCode.hasEmptyCode()
        ? log(loggingData, kerndatensatzValue)
        : FhirGenerator.coding(parsedCode);
  }

  public static Coding optionalCoding(String kerndatensatzValue) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return parsedCode.hasEmptyCode() ? Constants.getEmptyValue() : FhirGenerator.coding(parsedCode);
  }

  public static Coding optionalCodingFromSystemWithExtensions(
      String kerndatensatzValue, String codeSystem, List<Extension> extensions) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(parsedCode);
    for (Extension extension : extensions) {
      coding.addExtension(extension);
    }
    return coding;
  }

  public static CodeableConcept optionalCodeFromSystem(
      String kerndatensatzValue, String codeSystem) {
    return optionalFhirFromSystem(kerndatensatzValue, codeSystem, FhirGenerator::codeableConcept);
  }

  public static <T extends Code> CodeableConcept optionalCodeFromValueSetOrSystem(
      String kerndatensatzValue,
      String codeSystem,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    return mapToFhirCodeFromValueSet
        .apply(parsedCode.getCode())
        .map(FhirGenerator::codeableConcept)
        .orElseGet(
            getWarningSupplier(
                () -> FhirGenerator.codeableConcept(parsedCode), loggingData, kerndatensatzValue));
  }

  private static <T extends Code, R> R optionalFhirFromValueSet(
      String kerndatensatzValue,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData,
      Function<T, R> mapper) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    return mapToFhirCodeFromValueSet
        .apply(parsedCode.getCode())
        .map(mapper)
        .orElseGet(getLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static <T extends Code> CodeableConcept optionalCodeFromValueSet(
      String kerndatensatzValue,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    return optionalFhirFromValueSet(
        kerndatensatzValue, mapToFhirCodeFromValueSet, loggingData, FhirGenerator::codeableConcept);
  }

  public static <T extends Code> Extension optionalExtensionWithCodingFromValueSet(
      String kerndatensatzValue,
      String extensionUrl,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    return optionalFhirFromValueSet(
        kerndatensatzValue,
        mapToFhirCodeFromValueSet,
        loggingData,
        fhirCode -> FhirGenerator.extension(extensionUrl, FhirGenerator.coding(fhirCode)));
  }

  public static <T extends Code> Extension optionalExtensionWithCodeFromValueSet(
      String kerndatensatzValue,
      String extensionUrl,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    return optionalFhirFromValueSet(
        kerndatensatzValue,
        mapToFhirCodeFromValueSet,
        loggingData,
        fhirCode -> FhirGenerator.extension(extensionUrl, FhirGenerator.codeableConcept(fhirCode)));
  }

  public static Extension optionalExtensionWithCoding(
      String kerndatensatzValue, String codeSystem, String codeVersion, String extensionUrl) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(parsedCode, codeVersion);
    return FhirGenerator.extension(extensionUrl, coding);
  }

  public static Coding optionalCodingFromSystemWithVersionAndExtension(
      String kerndatensatzValue, String codeSystem, String codeVersion, Extension extension) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(parsedCode, codeVersion);
    coding.addExtension(extension);
    return coding;
  }

  public static Extension extensionWithDateTimeType(
      String date, String extensionUrl, LoggingData loggingData) {
    return Helper.getDateFromISO(date)
        .map(FhirGenerator::dateTimeType)
        .map(dateTime -> FhirGenerator.extension(extensionUrl, dateTime))
        .orElseGet(getLoggingSupplier(loggingData, date));
  }

  public static Identifier optionalIdentifierFromSystemWithCoding(
      String kerndatensatzValue, String identifierSystem, Code code) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(code);
    return FhirGenerator.identifier(kerndatensatzValue, identifierSystem, coding);
  }

  public static DateTimeType dateTimeType(String date, LoggingData loggingData) {
    return Helper.getDateFromISO(date)
        .map(FhirGenerator::dateTimeType)
        .orElseGet(getLoggingSupplier(loggingData, date));
  }

  public static DateTimeType dateTimeTypeWithExtension(
      String date, LoggingData loggingData, Extension extension) {
    DateTimeType result = new DateTimeType();
    if (Helper.checkNonEmptyString(date)) {
      result.setPrecision(TemporalPrecisionEnum.SECOND);
      Date resultDate =
          Helper.getDateFromISO(date).orElseGet(getLoggingSupplier(loggingData, date));
      result.setValue(resultDate);
    }
    result.addExtension(extension);
    return result;
  }

  public static Type optionalDateTimeTypeOrPeriod(DateTimeType start, DateTimeType end) {
    boolean emptyStart = FhirHelper.emptyDateTimeType(start);
    boolean emptyEnd = FhirHelper.emptyDateTimeType(end);
    if (emptyStart && emptyEnd) {
      return Constants.getEmptyValue();
    }
    return emptyEnd ? start : new Period().setStartElement(start).setEndElement(end);
  }

  public static Date date(String date, LoggingData loggingData) {
    return Helper.getDateFromISO(date).orElseGet(getLoggingSupplier(loggingData, date));
  }

  public static Period periodWithOptionalEnd(
      String startDate, String endDate, LoggingData loggingDataStart, LoggingData loggingDataEnd) {
    Date start = date(startDate, loggingDataStart);
    if (Constants.isEmptyValue(start)) {
      return Constants.getEmptyValue();
    }
    if (Helper.checkEmptyString(endDate)) {
      return FhirGenerator.period(start);
    }
    Date end = date(endDate, loggingDataEnd);
    if (Constants.isEmptyValue(end)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.period(start, end);
  }

  public static Annotation optionalAnnotation(String text) {
    if (Helper.checkEmptyString(text)) {
      return Constants.getEmptyValue();
    }
    return new Annotation().setText(text);
  }

  private static <T> T log(LoggingData loggingData, String value) {
    return loggingData.LOGGER.error(loggingData.METHOD_NAME, loggingData.VALUE_NAME, value);
  }

  private static <T> Supplier<T> getLoggingSupplier(LoggingData loggingData, String value) {
    return loggingData.LOGGER.errorSupplier(loggingData.METHOD_NAME, loggingData.VALUE_NAME, value);
  }

  private static <T> Supplier<T> getWarningSupplier(
      Supplier<T> returnValue, LoggingData loggingData, String value) {
    return loggingData.LOGGER.warningSupplier(
        returnValue, loggingData.METHOD_NAME, "Value could be outside of ValueSet: " + value);
  }
}
