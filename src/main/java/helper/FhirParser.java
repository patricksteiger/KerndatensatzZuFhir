package helper;

import constants.Constants;
import interfaces.Code;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;

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

  public static CodeableConcept optionalCodeFromSystem(
      String kerndatensatzValue, String codeSystem) {
    return optionalFhirFromSystem(kerndatensatzValue, codeSystem, FhirGenerator::codeableConcept);
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

  public static Coding optionalCodeFromSystemWithVersionAndExtension(
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

  public static DateTimeType dateTimeType(String date, LoggingData loggingData) {
    return Helper.getDateFromISO(date)
        .map(FhirGenerator::dateTimeType)
        .orElseGet(getLoggingSupplier(loggingData, date));
  }

  private static <T, R> Supplier<T> getLoggingSupplier(LoggingData loggingData, String value) {
    return loggingData.LOGGER.errorSupplier(loggingData.METHOD_NAME, loggingData.VALUE_NAME, value);
  }
}
