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
    if (Helper.checkNonEmptyString(source)) meta.setSource(source);
    if (Helper.checkNonEmptyString(versionId)) meta.setVersionId(versionId);
    Helper.forFilterEach(profiles, Helper::checkNonEmptyString, meta::addProfile);
    Helper.forFilterEach(securities, Objects::nonNull, meta::addSecurity);
    Helper.forFilterEach(tags, Objects::nonNull, meta::addTag);
    meta.setLastUpdated(new Date());
    return meta;
  }

  public static Meta meta(String profile, String source, String versionId) {
    return meta(Helper.listOf(profile), source, versionId, Helper.listOf(), Helper.listOf());
  }

  public static Coding coding(String code, String system, String display, String version) {
    Coding coding = new Coding();
    coding.setCode(code);
    coding.setSystem(system);
    if (Helper.checkNonEmptyString(display)) coding.setDisplay(display);
    if (Helper.checkNonEmptyString(version)) coding.setVersion(version);
    return coding;
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

  public static Period period(Date start, Date end) {
    Period period = new Period();
    if (start != null) period.setStart(start);
    if (end != null) period.setEnd(end);
    return period;
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
    Identifier identifier = new Identifier();
    if (Helper.checkNonEmptyString(value)) identifier.setValue(value);
    if (Helper.checkNonEmptyString(system)) identifier.setSystem(system);
    if (type != null) identifier.setType(type);
    if (assignerRef != null) identifier.setAssigner(assignerRef);
    if (use != null) identifier.setUse(use);
    return identifier;
  }

  public static Identifier identifier(String value, String system, Coding coding) {
    CodeableConcept codeableConcept = new CodeableConcept().addCoding(coding);
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
      HumanName.NameUse use,
      String text,
      List<Extension> family,
      List<String> given,
      List<Extension> artDesPrefix,
      List<String> prefix,
      List<String> suffix) {
    HumanName name = new HumanName();
    if (use != null) name.setUse(use);
    if (Helper.checkNonEmptyString(text)) name.setText(text);
    Helper.forFilterEach(family, Objects::nonNull, name::addExtension);
    Helper.forFilterEach(given, Helper::checkNonEmptyString, name::addGiven);
    Helper.forFilterEach(artDesPrefix, Objects::nonNull, name::addExtension);
    Helper.forFilterEach(prefix, Helper::checkNonEmptyString, name::addPrefix);
    Helper.forFilterEach(suffix, Helper::checkNonEmptyString, name::addSuffix);
    return name;
  }

  public static HumanName humanName(
      HumanName.NameUse use,
      List<Extension> family,
      List<String> given,
      List<Extension> artDesPrefix,
      String prefix) {
    return humanName(use, "", family, given, artDesPrefix, Helper.listOf(prefix), null);
  }

  public static HumanName humanName(HumanName.NameUse use, List<Extension> family) {
    return humanName(use, family, null, null, "");
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
    Quantity quantity = new Quantity();
    if (value != null) quantity.setValue(value);
    if (comparator != null) quantity.setComparator(comparator);
    if (Helper.checkNonEmptyString(unit)) quantity.setUnit(unit);
    if (Helper.checkNonEmptyString(system)) quantity.setSystem(system);
    if (Helper.checkNonEmptyString(code)) quantity.setCode(code);
    return quantity;
  }

  public static Quantity quantity(String value, String unit, String system, String code) {
    Quantity quantity = new Quantity();
    if (Helper.checkNonEmptyString(value)) {
      DecimalType decimalType = new DecimalType().setRepresentation(value);
      quantity.setValueElement(decimalType);
    }
    if (Helper.checkNonEmptyString(unit)) quantity.setUnit(unit);
    if (Helper.checkNonEmptyString(system)) quantity.setSystem(system);
    if (Helper.checkNonEmptyString(code)) quantity.setCode(code);
    return quantity;
  }

  public static Quantity quantity(BigDecimal value, String unit, String system, String code) {
    return quantity(value, null, unit, system, code);
  }

  public static BooleanType booleanType(boolean type) {
    return new BooleanType(type);
  }

  public static Reference reference(
      String ref, String type, Identifier identifier, String display) {
    Reference reference = new Reference();
    if (Helper.checkNonEmptyString(ref)) reference.setReference(ref);
    if (Helper.checkNonEmptyString(type)) reference.setType(type);
    if (identifier != null) reference.setIdentifier(identifier);
    if (Helper.checkNonEmptyString(display)) reference.setDisplay(display);
    return reference;
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
    Extension extension = new Extension();
    extension.setUrl(url);
    extension.setValue(value);
    return extension;
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
    Address address = new Address();
    if (use != null) address.setUse(use);
    if (type != null) address.setType(type);
    if (Helper.checkNonEmptyString(text)) address.setText(text);
    Helper.forFilterEach(line, Helper::checkNonEmptyString, address::addLine);
    if (Helper.checkNonEmptyString(city)) address.setCity(city);
    if (Helper.checkNonEmptyString(district)) address.setDistrict(district);
    if (Helper.checkNonEmptyString(state)) address.setState(state);
    if (Helper.checkNonEmptyString(postalCode)) address.setPostalCode(postalCode);
    if (Helper.checkNonEmptyString(country)) address.setCountry(country);
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
    Dosage dosage = new Dosage();
    if (Helper.checkNonEmptyString(sequence)) dosage.setSequence(Integer.parseInt(sequence));
    if (Helper.checkNonEmptyString(text)) dosage.setText(text);
    Helper.forFilterEach(
        additionalInstructions, Objects::nonNull, dosage::addAdditionalInstruction);
    if (Helper.checkNonEmptyString(patientInstruction))
      dosage.setPatientInstruction(patientInstruction);
    if (timing != null) dosage.setTiming(timing);
    if (asNeeded != null) dosage.setAsNeeded(asNeeded);
    if (site != null) dosage.setSite(site);
    if (route != null) dosage.setRoute(route);
    if (method != null) dosage.setMethod(method);
    Helper.forFilterEach(doseAndRate, Objects::nonNull, dosage::addDoseAndRate);
    if (maxDosePerPeriod != null) dosage.setMaxDosePerPeriod(maxDosePerPeriod);
    if (maxDosePerAdministration != null)
      dosage.setMaxDosePerAdministration(maxDosePerAdministration);
    if (maxDosePerLifeTime != null) dosage.setMaxDosePerLifetime(maxDosePerLifeTime);
    return dosage;
  }

  public static DateTimeType dateTimeType(Date date) {
    return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
  }
}
