package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.Constants;
import constants.IdentifierSystem;
import interfaces.Code;
import org.hl7.fhir.r4.model.*;
import valueSets.MedikationStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
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
        ? logMethodValue(loggingData, kerndatensatzValue)
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

  public static CodeableConcept codeWithOptionalText(
      String kerndatensatzValue, String text, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return parsedCode.hasEmptyCode()
        ? logMethodValue(loggingData, kerndatensatzValue)
        : FhirGenerator.codeableConcept(parsedCode).setText(text);
  }

  public static CodeableConcept optionalCode(String kerndatensatzValue) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return parsedCode.hasEmptyCode()
        ? Constants.getEmptyValue()
        : FhirGenerator.codeableConcept(parsedCode);
  }

  public static <T extends Code> CodeableConcept optionalCodeFromValueSet(
      String kerndatensatzValue, Function<String, Optional<T>> mapToFhirCodeFromValueSet) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return mapToFhirCodeFromValueSet
        .apply(parsedCode.getCode())
        .map(FhirGenerator::codeableConcept)
        .orElse(Constants.getEmptyValue());
  }

  public static CodeableConcept optionalCodeFromSystem(
      String kerndatensatzValue, String codeSystem) {
    return optionalFhirFromSystem(kerndatensatzValue, codeSystem, FhirGenerator::codeableConcept);
  }

  public static CodeableConcept optionalCodeFromSystemWithValidation(
      String kerndatensatzValue,
      String system,
      Predicate<String> validator,
      LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, system);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    if (!validator.test(parsedCode.getCode())) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    return FhirGenerator.codeableConcept(parsedCode);
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
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static <T extends Code> CodeableConcept optionalCodeFromValueSet(
      String kerndatensatzValue,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    return optionalFhirFromValueSet(
        kerndatensatzValue, mapToFhirCodeFromValueSet, loggingData, FhirGenerator::codeableConcept);
  }

  public static CodeableConcept optionalCodeWithCodingAndOptionalText(
      String kerndatensatzValue, String text, Function<ParsedCode, Coding> codingChooser) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode() && Helper.checkEmptyString(text)) {
      return Constants.getEmptyValue();
    }
    Coding coding = codingChooser.apply(parsedCode);
    return FhirGenerator.codeableConcept(coding).setText(text);
  }

  public static CodeableConcept codeFromSystemWithOptionalText(
      String kerndatensatzValue, String codeSystem, String text, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, codeSystem);
    return parsedCode.hasEmptyCode()
        ? logMethodValue(loggingData, kerndatensatzValue)
        : FhirGenerator.codeableConcept(parsedCode).setText(text);
  }

  public static CodeableConcept codeWithDefaultDisplay(
      String kerndatensatzValue, String defaultDisplay, LoggingData data) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode()) {
      return logMethodValue(data, kerndatensatzValue);
    }
    String display = parsedCode.hasDisplay() ? parsedCode.getDisplay() : defaultDisplay;
    Coding coding = FhirGenerator.coding(parsedCode.getCode(), parsedCode.getSystem(), display);
    return FhirGenerator.codeableConcept(coding);
  }

  public static Coding optionalCodingFromSystemWithDefaultDisplay(
      String kerndatensatzValue, String system, String defaultDisplay) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue, system);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    String display = parsedCode.hasDisplay() ? parsedCode.getDisplay() : defaultDisplay;
    return FhirGenerator.coding(parsedCode.getCode(), parsedCode.getSystem(), display);
  }

  public static Extension extensionWithDateTimeType(
      String date, String extensionUrl, LoggingData loggingData) {
    return Helper.getDateFromISO(date)
        .map(FhirGenerator::dateTimeType)
        .map(dateTime -> FhirGenerator.extension(extensionUrl, dateTime))
        .orElseGet(getMethodValueLoggingSupplier(loggingData, date));
  }

  public static Extension extensionWithCoding(Code code, String url) {
    Coding coding = FhirGenerator.coding(code);
    return FhirGenerator.extension(url, coding);
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

  public static Extension optionalExtensionWithStringType(
      String kerndatensatzValue, String extensionUrl) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    StringType type = new StringType(kerndatensatzValue);
    return FhirGenerator.extension(extensionUrl, type);
  }

  public static Extension optionalExtensionWithCodeType(
      String kerndatensatzValue, String extensionUrl) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    CodeType type = new CodeType(parsedCode.getCode());
    return FhirGenerator.extension(extensionUrl, type);
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

  public static <T extends Code> Coding optionalCodingFromValueSet(
      String kerndatensatzValue,
      Function<String, Optional<T>> mapToFhirCodeFromValueSet,
      LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    return mapToFhirCodeFromValueSet
        .apply(parsedCode.getCode())
        .map(FhirGenerator::coding)
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static Identifier optionalIdentifierFromSystemWithCoding(
      String kerndatensatzValue, String identifierSystem, Code code) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(code);
    return FhirGenerator.identifier(kerndatensatzValue, identifierSystem, coding);
  }

  public static Identifier optionalIdentifierFromSystemWithCodeAndReference(
      String kerndatensatzValue,
      String identifierSystem,
      Identifier.IdentifierUse identifierUse,
      Code code,
      Supplier<Reference> reference) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    CodeableConcept type = FhirGenerator.codeableConcept(code);
    return FhirGenerator.identifier(
        kerndatensatzValue, identifierSystem, type, reference.get(), identifierUse);
  }

  public static Identifier optionalIdentifier(String id) {
    return Helper.checkEmptyString(id)
        ? Constants.getEmptyValue()
        : FhirGenerator.identifier(id, IdentifierSystem.EMPTY);
  }

  public static Identifier identifierWithCoding(
      String kerndatensatzValue, Code code, LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    Coding coding = FhirGenerator.coding(code);
    return FhirGenerator.identifier(kerndatensatzValue, IdentifierSystem.EMPTY, coding);
  }

  public static Identifier identifierWithCodeAndReference(
      String kerndatensatzValue, Code code, String reference, LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    CodeableConcept codeableConcept = FhirGenerator.codeableConcept(code);
    Reference assignerRef = FhirGenerator.reference(reference);
    return FhirGenerator.identifier(
        kerndatensatzValue, IdentifierSystem.EMPTY, codeableConcept, assignerRef);
  }

  public static Identifier identifierFromSystemAndCodeWithCode(
      String kerndatensatzValue, String identifierSystem, Code code, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    if (parsedCode.hasEmptyCode()) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    CodeableConcept type = FhirGenerator.codeableConcept(code);
    return FhirGenerator.identifier(parsedCode.getCode(), identifierSystem, type);
  }

  public static Reference optionalReferenceWithIdentifier(String type, String identifierId) {
    if (Helper.checkEmptyString(identifierId)) {
      return Constants.getEmptyValue();
    }
    Identifier identifier = FhirGenerator.identifier(identifierId, IdentifierSystem.EMPTY);
    return FhirGenerator.reference(type, identifier);
  }

  public static Reference optionalReference(String ref) {
    return Helper.checkNonEmptyString(ref)
        ? FhirGenerator.reference(ref)
        : Constants.getEmptyValue();
  }

  public static Reference referenceWithIdentifierFromSystem(
      String kerndatensatzValue,
      String identifierSystem,
      Identifier.IdentifierUse identifierUse,
      LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    Identifier identifier =
        FhirGenerator.identifier(kerndatensatzValue, identifierSystem, identifierUse);
    return FhirGenerator.reference(identifier);
  }

  public static <T extends Code> Reference referenceWithIdentifierFromSystemWithCodeableConcept(
      String kerndatensatzValue,
      String identifierSystem,
      Identifier.IdentifierUse identifierUse,
      T typeValue,
      LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    CodeableConcept type = FhirGenerator.codeableConcept(typeValue);
    Identifier identifier =
        FhirGenerator.identifier(kerndatensatzValue, identifierSystem, type, null, identifierUse);
    return FhirGenerator.reference(identifier);
  }

  public static Quantity quantity(String kerndatensatzValue, LoggingData loggingData) {
    return ParsedQuantity.fromString(kerndatensatzValue)
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static Quantity optionalQuantity(String kerndatensatzValue, LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    return ParsedQuantity.fromString(kerndatensatzValue)
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static Ratio optionalRatio(String kerndatensatzValue, LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    return ParsedRatio.fromString(kerndatensatzValue)
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static MedicationAdministration.MedicationAdministrationStatus
      medicationAdministrationStatus(String kerndatensatzValue, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return MedikationStatus.medicationAdministrationStatusFromCode(parsedCode.getCode())
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static MedicationStatement.MedicationStatementStatus medicationStatementStatus(
      String kerndatensatzValue, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return MedikationStatus.medicationStatementStatusFromCode(parsedCode.getCode())
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static ResearchSubject.ResearchSubjectStatus researchSubjectStatus(
      String kerndatensatzValue, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return FhirHelper.getResearchSubjectStatusFromCode(parsedCode.getCode())
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  public static DateTimeType dateTimeType(String date, LoggingData loggingData) {
    return Helper.getDateFromISO(date)
        .map(FhirGenerator::dateTimeType)
        .orElseGet(getMethodValueLoggingSupplier(loggingData, date));
  }

  public static DateTimeType dateTimeTypeWithExtension(
      String date, LoggingData loggingData, Extension extension) {
    Optional<DateTimeType> dateTimeType =
        Helper.getDateFromISO(date).map(FhirGenerator::dateTimeType);
    dateTimeType.ifPresent(d -> d.addExtension(extension));
    return dateTimeType.orElseGet(getMethodValueLoggingSupplier(loggingData, date));
  }

  public static DateTimeType nonEmptyDateTimeTypeWithExtension(
      String date, LoggingData loggingData, Extension extension) {
    DateTimeType result = new DateTimeType();
    if (Helper.checkNonEmptyString(date)) {
      result.setPrecision(TemporalPrecisionEnum.SECOND);
      Date resultDate =
          Helper.getDateFromISO(date).orElseGet(getMethodValueLoggingSupplier(loggingData, date));
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

  public static Type dateTimeTypeOrPeriod(
      String start, String end, LoggingData startData, LoggingData endData) {
    if (Helper.checkEmptyString(end)) {
      return dateTimeType(start, startData);
    }
    DateTimeType startType = dateTimeType(start, startData), endType = dateTimeType(end, endData);
    if (Constants.isEmptyValue(startType) || Constants.isEmptyValue(endType)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.period(startType, endType);
  }

  public static Type optionalDateTimeTypeOrBooleanType(
      String kernDatum, String kernBool, LoggingData datumData, LoggingData boolData) {
    if (Helper.checkNonEmptyString(kernDatum)) {
      Optional<DateTimeType> date =
          Helper.getDateFromISO(kernDatum).map(FhirGenerator::dateTimeType);
      if (date.isPresent()) {
        return date.get();
      }
      logMethodValue(datumData, kernDatum);
    }
    if (Helper.checkNonEmptyString(kernBool)) {
      return Helper.booleanFromString(kernBool)
          .map(FhirGenerator::booleanType)
          .orElseGet(getMethodValueLoggingSupplier(boolData, kernBool));
    }
    return Constants.getEmptyValue();
  }

  public static Date date(String date, LoggingData loggingData) {
    return Helper.getDateFromISO(date).orElseGet(getMethodValueLoggingSupplier(loggingData, date));
  }

  public static Date optionalDate(String date, LoggingData loggingData) {
    if (Helper.checkEmptyString(date)) {
      return Constants.getEmptyValue();
    }
    return date(date, loggingData);
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

  public static Address optionalAddress(
      String line,
      String city,
      String postalCode,
      String country,
      Address.AddressType type,
      LoggingData loggingData) {
    if (Helper.checkAllEmptyString(line, city, postalCode, country)) {
      return Constants.getEmptyValue();
    }
    if (Helper.checkAnyEmptyString(line, city, postalCode, country)) {
      return logMethodMessage(loggingData);
    }
    return FhirGenerator.address(type, line, city, postalCode, country);
  }

  public static HumanName optionalHumanName(String kerndatensatzValue, HumanName.NameUse use) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return Constants.getEmptyValue();
    }
    StringType family = new StringType(kerndatensatzValue);
    return FhirGenerator.humanName(family, use);
  }

  private static StringType generalStringTypeWithExtensions(
      String kerndatensatzvalue, Supplier<StringType> emptyValue, Extension... extensions) {
    if (Helper.checkEmptyString(kerndatensatzvalue)) {
      return emptyValue.get();
    }
    StringType type = new StringType(kerndatensatzvalue);
    for (Extension extension : extensions) {
      type.addExtension(extension);
    }
    return type;
  }

  public static StringType stringTypeWithExtensions(
      String kerndatensatzValue, LoggingData loggingData, Extension... extensions) {
    return generalStringTypeWithExtensions(
        kerndatensatzValue,
        getMethodValueLoggingSupplier(loggingData, kerndatensatzValue),
        extensions);
  }

  public static StringType optionalStringTypeWithExtension(
      String kerndatensatzValue, Extension extension) {
    return generalStringTypeWithExtensions(kerndatensatzValue, Constants::getEmptyValue, extension);
  }

  public static List<StringType> stringTypeListFromName(
      String kerndatensatzValue, LoggingData loggingData) {
    if (Helper.checkEmptyString(kerndatensatzValue)) {
      return logMethodValue(loggingData, kerndatensatzValue);
    }
    List<String> names = Helper.splitNames(kerndatensatzValue);
    return Helper.listMap(names, StringType::new);
  }

  public static Enumeration<Enumerations.AdministrativeGender> administrativeGender(
      String kerndatensatzValue, LoggingData loggingData) {
    ParsedCode parsedCode = ParsedCode.fromString(kerndatensatzValue);
    return FhirHelper.getGenderMapping(parsedCode.getCode())
        .orElseGet(getMethodValueLoggingSupplier(loggingData, kerndatensatzValue));
  }

  private static <T> T logMethodValue(LoggingData loggingData, String value) {
    return loggingData.LOGGER.error(loggingData.METHOD_NAME, loggingData.VALUE_NAME, value);
  }

  private static <T> Supplier<T> getMethodValueLoggingSupplier(
      LoggingData loggingData, String value) {
    return loggingData.LOGGER.errorSupplier(loggingData.METHOD_NAME, loggingData.VALUE_NAME, value);
  }

  private static <T> T logMethodMessage(LoggingData loggingData) {
    return loggingData.LOGGER.error(loggingData.METHOD_NAME, loggingData.MESSAGE);
  }

  private static <T> Supplier<T> getWarningSupplier(
      Supplier<T> returnValue, LoggingData loggingData, String value) {
    return loggingData.LOGGER.warningSupplier(
        returnValue, loggingData.METHOD_NAME, "Value could be outside of ValueSet: " + value);
  }
}
