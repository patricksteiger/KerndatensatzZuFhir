package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.CodingSystem;
import constants.IdentifierSystem;
import constants.ReferenceType;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import org.hl7.fhir.r4.model.*;

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

  public static Reference getUKUAssignerReference() {
    Identifier assignerId =
        FhirHelper.generateIdentifier(MIICoreLocations.UKU.name(), IdentifierSystem.NS_DIZ);
    return FhirHelper.generateReference(assignerId, MIICoreLocations.UKU.toString());
  }

  public static Reference getOrganizationAssignerReference() {
    String type = ReferenceType.ORGANIZATION;
    // Identifier
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    String system = IdentifierSystem.ORGANIZATION_REFERENCE_ID;
    IdentifierTypeCode identifierTypeCode = IdentifierTypeCode.XX;
    String identifierSystem = CodingSystem.PID;
    Coding coding =
        FhirHelper.generateCoding(
            identifierTypeCode.getCode(), identifierSystem, identifierTypeCode.getDisplay());
    CodeableConcept identifierType = new CodeableConcept().addCoding(coding);
    // FIXME: What is the value of the identifier?
    String identifierValue = "";
    Identifier identifier =
        FhirHelper.generateIdentifier(identifierValue, system, identifierType, null, use);
    return FhirHelper.generateReference("", type, identifier, "");
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

  public static DateTimeType generateDate(Date date) {
    return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
  }
}
