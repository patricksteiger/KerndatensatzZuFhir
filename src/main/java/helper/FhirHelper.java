package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.IdentifierSystem;
import enums.MIICoreLocations;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.*;

public class FhirHelper {

  public static Meta generateMeta(
      List<String> profiles,
      String source,
      String versionId,
      List<Coding> securities,
      List<Coding> tags) {
    Meta meta = new Meta();
    if (Helper.checkNonEmptyString(source)) meta.setSource(source);
    if (Helper.checkNonEmptyString(versionId)) meta.setVersionId(versionId);
    profiles.stream().filter(Helper::checkNonEmptyString).forEach(meta::addProfile);
    securities.stream().filter(Objects::nonNull).forEach(meta::addSecurity);
    tags.stream().filter(Objects::nonNull).forEach(meta::addTag);
    meta.setLastUpdated(new Date());
    return meta;
  }

  public static Meta generateMeta(String profile, String source, String versionId) {
    return generateMeta(
        Helper.listOf(profile), source, versionId, Helper.listOf(), Helper.listOf());
  }

  public static Coding generateCoding(String code, String system, String display, String version) {
    Coding coding = new Coding();
    coding.setCode(code);
    coding.setSystem(system);
    if (Helper.checkNonEmptyString(display)) coding.setDisplay(display);
    if (Helper.checkNonEmptyString(version)) coding.setVersion(version);
    return coding;
  }

  public static Coding generateCoding(String code, String system, String display) {
    return generateCoding(code, system, display, "");
  }

  public static Coding generateCoding(String code, String system) {
    return generateCoding(code, system, "");
  }

  public static Period generatePeriod(Date start, Date end) {
    Period period = new Period();
    if (start != null) period.setStart(start);
    if (end != null) period.setEnd(end);
    return period;
  }

  public static Period generatePeriod(Date start) {
    return generatePeriod(start, null);
  }

  public static Identifier generateIdentifier(
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

  public static Identifier generateIdentifier(String value, String system, Coding coding) {
    CodeableConcept codeableConcept = new CodeableConcept().addCoding(coding);
    return generateIdentifier(value, system, codeableConcept, null);
  }

  public static Identifier generateIdentifier(
      String value, String system, CodeableConcept type, Reference assignerRef) {
    return generateIdentifier(value, system, type, assignerRef, null);
  }

  public static Identifier generateIdentifier(String value, String system, Reference assignerRef) {
    return generateIdentifier(value, system, null, assignerRef);
  }

  public static Identifier generateIdentifier(String value, String system) {
    return generateIdentifier(value, system, null, null);
  }

  public static HumanName generateHumanName(
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
    family.stream().filter(Objects::nonNull).forEach(name::addExtension);
    given.stream().filter(Helper::checkNonEmptyString).forEach(name::addGiven);
    artDesPrefix.stream().filter(Objects::nonNull).forEach(name::addExtension);
    prefix.stream().filter(Helper::checkNonEmptyString).forEach(name::addPrefix);
    suffix.stream().filter(Helper::checkNonEmptyString).forEach(name::addSuffix);
    return name;
  }

  public static HumanName generateHumanName(
      HumanName.NameUse use,
      List<Extension> family,
      List<String> given,
      List<Extension> artDesPrefix,
      String prefix) {
    return generateHumanName(
        use, "", family, given, artDesPrefix, Helper.listOf(prefix), Helper.listOf());
  }

  public static HumanName generateHumanName(HumanName.NameUse use, List<Extension> family) {
    return generateHumanName(use, family, Helper.listOf(), Helper.listOf(), "");
  }

  public static Enumerations.AdministrativeGender getGenderMapping(String gender) {
    if ("f".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.FEMALE;
    if ("m".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.MALE;
    if ("un".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.OTHER;
    return Enumerations.AdministrativeGender.UNKNOWN;
  }

  public static Quantity generateQuantity(
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

  public static Quantity generateQuantity(
      BigDecimal value, String unit, String system, String code) {
    return generateQuantity(value, null, unit, system, code);
  }

  public static Quantity generateQuantity(BigDecimal value, String unit) {
    return generateQuantity(value, unit, "", "");
  }

  public static Reference getMIIPatientReference(String patNr) {
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirHelper.generateIdentifier(patNr, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(subjectId);
  }

  public static Reference getUKUAssignerReference() {
    Identifier assignerId =
        FhirHelper.generateIdentifier(MIICoreLocations.UKU.name(), IdentifierSystem.NS_DIZ);
    return FhirHelper.generateReference(assignerId, MIICoreLocations.UKU.toString());
  }

  public static Reference generateReference(
      String ref, String type, Identifier identifier, String display) {
    Reference reference = new Reference();
    if (Helper.checkNonEmptyString(ref)) reference.setReference(ref);
    if (Helper.checkNonEmptyString(type)) reference.setType(type);
    if (identifier != null) reference.setIdentifier(identifier);
    if (Helper.checkNonEmptyString(display)) reference.setDisplay(display);
    return reference;
  }

  public static Reference generateReference(String ref) {
    return generateReference(ref, "", null, "");
  }

  public static Reference generateReference(String type, Identifier identifier) {
    return generateReference("", type, identifier, "");
  }

  public static Reference generateReference(Identifier identifier, String display) {
    return generateReference("", "", identifier, display);
  }

  public static Reference generateReference(Identifier identifier) {
    return generateReference(identifier, "");
  }

  public static Extension generateExtension(String url, Type value) {
    Extension extension = new Extension();
    extension.setUrl(url);
    extension.setValue(value);
    return extension;
  }

  public static Address generateAddress(
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
    line.stream().filter(Helper::checkNonEmptyString).forEach(address::addLine);
    if (Helper.checkNonEmptyString(city)) address.setCity(city);
    if (Helper.checkNonEmptyString(district)) address.setDistrict(district);
    if (Helper.checkNonEmptyString(state)) address.setState(state);
    if (Helper.checkNonEmptyString(postalCode)) address.setPostalCode(postalCode);
    if (Helper.checkNonEmptyString(country)) address.setCountry(country);
    return address;
  }

  public static Address generateAddress(
      Address.AddressType type, String line, String city, String postalCode, String country) {
    return generateAddress(null, type, "", Helper.listOf(line), city, "", "", postalCode, country);
  }

  public static Dosage generateDosage(
      String text,
      Timing timing,
      Type asNeeded,
      CodeableConcept route,
      List<Dosage.DosageDoseAndRateComponent> doseAndRate) {
    return generateDosage("", text, timing, asNeeded, route, doseAndRate);
  }

  public static Dosage generateDosage(
      String sequence,
      String text,
      Timing timing,
      Type asNeeded,
      CodeableConcept route,
      List<Dosage.DosageDoseAndRateComponent> doseAndRate) {
    return generateDosage(
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

  public static Dosage generateDosage(
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
    if (additionalInstructions != null)
      additionalInstructions.forEach(dosage::addAdditionalInstruction);
    if (Helper.checkNonEmptyString(patientInstruction))
      dosage.setPatientInstruction(patientInstruction);
    if (timing != null) dosage.setTiming(timing);
    if (asNeeded != null) dosage.setAsNeeded(asNeeded);
    if (site != null) dosage.setSite(site);
    if (route != null) dosage.setRoute(route);
    if (method != null) dosage.setMethod(method);
    if (doseAndRate != null) doseAndRate.forEach(dosage::addDoseAndRate);
    if (maxDosePerPeriod != null) dosage.setMaxDosePerPeriod(maxDosePerPeriod);
    if (maxDosePerAdministration != null)
      dosage.setMaxDosePerAdministration(maxDosePerAdministration);
    if (maxDosePerLifeTime != null) dosage.setMaxDosePerLifetime(maxDosePerLifeTime);
    return dosage;
  }

  /**
   * Returns diagnostic report status from case-insensitive string.
   *
   * @param diagnosticReportStatus
   * @return diagnostic report status enum
   * @throws IllegalArgumentException if diagnosticReportStatus is not valid.
   */
  public static DiagnosticReport.DiagnosticReportStatus getDiagnosticReportStatusFromString(
      String diagnosticReportStatus) {
    return Arrays.stream(DiagnosticReport.DiagnosticReportStatus.values())
        .filter(status -> status.name().equalsIgnoreCase(diagnosticReportStatus))
        .findFirst()
        .orElseThrow(Helper.illegalCode(diagnosticReportStatus, "DiagnosticReportStatus"));
  }

  /**
   * Returns MedicationStatus corresponding to code. Valid codes are aktiv, abgeschlossen, geplant,
   * unterbrochen, abgebrochen and unbekannt.
   *
   * @param code
   * @return MedicationStatus enum
   * @throws IllegalArgumentException if code is invalid
   */
  public static MedicationStatement.MedicationStatementStatus getMedicationStatementFromCode(
      String code) {
    switch (code) {
      case "aktiv":
        return MedicationStatement.MedicationStatementStatus.ACTIVE;
      case "abgeschlossen":
        return MedicationStatement.MedicationStatementStatus.COMPLETED;
      case "geplant":
        return MedicationStatement.MedicationStatementStatus.INTENDED;
      case "unterbrochen":
        return MedicationStatement.MedicationStatementStatus.ONHOLD;
      case "abgebrochen":
        return MedicationStatement.MedicationStatementStatus.STOPPED;
      case "unbekannt":
        return MedicationStatement.MedicationStatementStatus.UNKNOWN;
      default:
        throw Helper.illegalCode(code, "MedikationsStatus").get();
    }
  }

  public static DateTimeType generateDate(Date date) {
    return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
  }
}
