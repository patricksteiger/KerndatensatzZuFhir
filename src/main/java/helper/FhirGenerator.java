package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import interfaces.Code;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class FhirGenerator {
  private FhirGenerator() {}

  public static Meta meta(
      List<String> profiles,
      String source,
      String versionId,
      List<Coding> securities,
      List<Coding> tags) {
    Meta meta = new Meta();
    meta.setSource(source);
    meta.setVersionId(versionId);
    Helper.filterForEach(profiles, Helper::checkNonEmptyString, meta::addProfile);
    Helper.filterForEach(securities, Objects::nonNull, meta::addSecurity);
    Helper.filterForEach(tags, Objects::nonNull, meta::addTag);
    meta.setLastUpdated(new Date());
    return meta;
  }

  public static Meta meta(String profile, String source, String versionId) {
    return meta(Helper.listOf(profile), source, versionId, null, null);
  }

  public static Coding coding(String code, String system, String display, String version) {
    return new Coding().setCode(code).setSystem(system).setDisplay(display).setVersion(version);
  }

  public static Coding coding(Code code, String version) {
    return coding(code.getCode(), code.getSystem(), code.getDisplay(), version);
  }

  public static Coding coding(String code, String system, String display) {
    return coding(code, system, display, "");
  }

  public static Coding coding(Code code) {
    return coding(code.getCode(), code.getSystem(), code.getDisplay());
  }

  public static Coding coding(String code, String system) {
    return coding(code, system, "");
  }

  public static CodeableConcept codeableConcept(Coding... codings) {
    CodeableConcept codeableConcept = new CodeableConcept();
    for (Coding coding : codings) {
      codeableConcept.addCoding(coding);
    }
    return codeableConcept;
  }

  public static CodeableConcept codeableConcept(Code code) {
    Coding coding = coding(code);
    return codeableConcept(coding);
  }

  public static Period period(Date start, Date end) {
    return new Period().setStart(start).setEnd(end);
  }

  public static Period period(DateTimeType start, DateTimeType end) {
    return new Period().setStartElement(start).setEndElement(end);
  }

  public static Period period(Date start) {
    return period(start, null);
  }

  public static Identifier identifier(
      String value,
      String system,
      CodeableConcept type,
      Reference assignerRef,
      Identifier.IdentifierUse use) {
    return new Identifier()
        .setValue(value)
        .setSystem(system)
        .setType(type)
        .setAssigner(assignerRef)
        .setUse(use);
  }

  public static Identifier identifier(String value, String system, CodeableConcept type) {
    return identifier(value, system, type, null);
  }

  public static Identifier identifier(String value, String system, Identifier.IdentifierUse use) {
    return identifier(value, system, null, null, use);
  }

  public static Identifier identifier(String value, String system, Coding coding) {
    CodeableConcept codeableConcept = codeableConcept(coding);
    return identifier(value, system, codeableConcept, null);
  }

  public static Identifier identifier(
      String value, String system, CodeableConcept type, Reference assignerRef) {
    return identifier(value, system, type, assignerRef, null);
  }

  public static Identifier identifier(String value, String system, Reference assignerRef) {
    return identifier(value, system, null, assignerRef);
  }

  public static Identifier identifier(String value, String system) {
    return identifier(value, system, null, null);
  }

  public static HumanName humanName(
      StringType family, List<StringType> given, StringType prefix, HumanName.NameUse use) {
    return new HumanName()
        .setFamilyElement(family)
        .setGiven(given)
        .setPrefix(prefix != null ? Helper.listOf(prefix) : null)
        .setUse(use);
  }

  public static HumanName humanName(StringType family, HumanName.NameUse use) {
    return humanName(family, null, null, use);
  }

  public static Ratio ratio(Quantity numerator, Quantity denominator) {
    return new Ratio().setNumerator(numerator).setDenominator(denominator);
  }

  public static Quantity quantity(
      BigDecimal value,
      Quantity.QuantityComparator comparator,
      String unit,
      String system,
      String code) {
    return new Quantity()
        .setValue(value)
        .setComparator(comparator)
        .setUnit(unit)
        .setSystem(system)
        .setCode(code);
  }

  public static Quantity quantity(String value, String unit, String system, String code) {
    DecimalType decimalType = null;
    if (Helper.checkNonEmptyString(value)) {
      decimalType = new DecimalType().setRepresentation(value);
    }
    return new Quantity()
        .setUnit(unit)
        .setSystem(system)
        .setCode(code)
        .setValueElement(decimalType);
  }

  public static Quantity quantity(BigDecimal value, String unit, String system, String code) {
    return quantity(value, null, unit, system, code);
  }

  public static BooleanType booleanType(boolean type) {
    return new BooleanType(type);
  }

  public static Reference reference(
      String ref, String type, Identifier identifier, String display) {
    return new Reference()
        .setReference(ref)
        .setType(type)
        .setIdentifier(identifier)
        .setDisplay(display);
  }

  public static Reference reference(String ref) {
    return reference(ref, "", null, "");
  }

  public static Reference reference(String type, Identifier identifier) {
    return reference("", type, identifier, "");
  }

  public static Reference reference(Identifier identifier, String display) {
    return reference("", "", identifier, display);
  }

  public static Reference reference(Identifier identifier) {
    return reference(identifier, "");
  }

  public static Extension extension(String url, Type value) {
    return new Extension().setUrl(url).setValue(value);
  }

  public static Address address(
      Address.AddressUse use,
      Address.AddressType type,
      String text,
      List<String> line,
      String city,
      String district,
      String state,
      String postalCode,
      String country) {
    Address address =
        new Address()
            .setUse(use)
            .setType(type)
            .setText(text)
            .setCity(city)
            .setDistrict(district)
            .setState(state)
            .setPostalCode(postalCode)
            .setCountry(country);
    Helper.filterForEach(line, Helper::checkNonEmptyString, address::addLine);
    return address;
  }

  public static Address address(
      Address.AddressType type, String line, String city, String postalCode, String country) {
    return address(null, type, "", Helper.listOf(line), city, "", "", postalCode, country);
  }

  public static Dosage dosage(String text) {
    return dosage(text, null, null, null, null);
  }

  public static Dosage dosage(
      String text,
      Timing timing,
      Type asNeeded,
      CodeableConcept route,
      List<Dosage.DosageDoseAndRateComponent> doseAndRate) {
    return dosage("", text, timing, asNeeded, route, doseAndRate);
  }

  public static Dosage dosage(
      String sequence,
      String text,
      Timing timing,
      Type asNeeded,
      CodeableConcept route,
      List<Dosage.DosageDoseAndRateComponent> doseAndRate) {
    return dosage(
        sequence,
        text,
        null,
        "",
        timing,
        asNeeded,
        null,
        route,
        null,
        doseAndRate,
        null,
        null,
        null);
  }

  public static Dosage dosage(
      String sequence,
      String text,
      List<CodeableConcept> additionalInstructions,
      String patientInstruction,
      Timing timing,
      Type asNeeded,
      CodeableConcept site,
      CodeableConcept route,
      CodeableConcept method,
      List<Dosage.DosageDoseAndRateComponent> doseAndRate,
      Ratio maxDosePerPeriod,
      SimpleQuantity maxDosePerAdministration,
      SimpleQuantity maxDosePerLifeTime) {
    Dosage dosage =
        new Dosage()
            .setText(text)
            .setPatientInstruction(patientInstruction)
            .setTiming(timing)
            .setAsNeeded(asNeeded)
            .setSite(site)
            .setRoute(route)
            .setMethod(method)
            .setMaxDosePerPeriod(maxDosePerPeriod)
            .setMaxDosePerAdministration(maxDosePerAdministration)
            .setMaxDosePerLifetime(maxDosePerLifeTime);
    Helper.parseInt(sequence).ifPresent(dosage::setSequence);
    Helper.filterForEach(
        additionalInstructions, Objects::nonNull, dosage::addAdditionalInstruction);
    Helper.filterForEach(doseAndRate, Objects::nonNull, dosage::addDoseAndRate);
    return dosage;
  }

  public static DateTimeType dateTimeType(Date date) {
    return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
  }
}
