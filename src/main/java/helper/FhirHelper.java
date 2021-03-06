package helper;

import constants.CodingSystem;
import constants.ExtensionUrl;
import constants.IdentifierSystem;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;
import unit.converter.UnitConverter;
import valueSets.MIICoreLocations;

import java.util.Optional;

public class FhirHelper {
  private FhirHelper() {}

  /**
   * Returns diagnostic report status from case-insensitive string.
   *
   * @param diagnosticReportStatus
   * @return diagnostic report status enum
   * @throws IllegalArgumentException if diagnosticReportStatus is not valid.
   */
  public static Optional<DiagnosticReport.DiagnosticReportStatus>
      getDiagnosticReportStatusFromString(String diagnosticReportStatus) {
    for (DiagnosticReport.DiagnosticReportStatus status :
        DiagnosticReport.DiagnosticReportStatus.values()) {
      if (status.name().equalsIgnoreCase(diagnosticReportStatus)) {
        return Optional.of(status);
      }
    }
    return Optional.empty();
  }

  public static Optional<Observation.ObservationStatus> getObservationStatusFromCode(String code) {
    try {
      return Optional.ofNullable(Observation.ObservationStatus.fromCode(code));
    } catch (FHIRException e) {
      return Optional.empty();
    }
  }

  public static Optional<ResearchSubject.ResearchSubjectStatus> getResearchSubjectStatusFromCode(
      String code) {
    try {
      return Optional.ofNullable(ResearchSubject.ResearchSubjectStatus.fromCode(code));
    } catch (FHIRException e) {
      return Optional.empty();
    }
  }

  public static Optional<Ratio> generateRatioFromFractions(
      ValueAndUnitFraction valueAndUnitFraction) {
    Optional<Quantity> numerator =
        UnitConverter.fromLocalCode(
            valueAndUnitFraction.getUnitNumerator(), valueAndUnitFraction.getValueNumerator());
    if (!numerator.isPresent()) {
      return Optional.empty();
    }
    Optional<Quantity> denominator =
        UnitConverter.fromLocalCode(
            valueAndUnitFraction.getUnitDenominator(), valueAndUnitFraction.getValueDenominator());
    return denominator.map(d -> FhirGenerator.ratio(numerator.get(), d));
  }

  public static boolean emptyDateTimeType(DateTimeType dateTimeType) {
    return !dateTimeType.hasValue() && !dateTimeType.hasExtension();
  }

  /**
   * Returns the mapping of given gender code
   *
   * @param gender codes: F, M, D, X, U, UN, UNK
   * @return Mapping of given code, F maps to FEMALE, M to MALE, D to OTHER with extension "D" and
   *     X, U, UN, UNK to OTHER with extension "X"
   * @see "https://wiki.hl7.de/index.php?title=Geschlecht#administratives_Geschlecht"
   */
  public static Optional<Enumeration<Enumerations.AdministrativeGender>> getGenderMapping(
      String gender) {
    if (Helper.checkEmptyString(gender)) {
      return Optional.empty();
    }
    switch (gender) {
      case "F":
        return Optional.of(
            administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.FEMALE));
      case "M":
        return Optional.of(
            administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.MALE));
      case "D":
        return Optional.of(getGenderDE("D", "divers"));
      case "X":
      case "U":
      case "UN":
      case "UNK":
        return Optional.of(getGenderDE("X", "unbestimmt"));
      default:
        return Optional.empty();
    }
  }

  private static Enumeration<Enumerations.AdministrativeGender> getGenderDE(
      String code, String display) {
    String system = CodingSystem.ADMINISTRATIVE_GENDER;
    Coding gender = FhirGenerator.coding(code, system, display);
    String url = ExtensionUrl.ADMINISTRATIVE_GENDER;
    Extension extension = FhirGenerator.extension(url, gender);
    Enumeration<Enumerations.AdministrativeGender> result =
        administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.OTHER);
    result.addExtension(extension);
    return result;
  }

  public static Enumeration<Enumerations.AdministrativeGender>
      administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender gender) {
    Enumeration<Enumerations.AdministrativeGender> result =
        new Enumeration<>(new Enumerations.AdministrativeGenderEnumFactory());
    result.setValue(gender);
    return result;
  }

  public static Reference getMIIPatientReference(String patNr) {
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId = FhirGenerator.identifier(patNr, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirGenerator.reference(subjectId);
  }

  public static Reference getUKUAssignerReference() {
    Identifier assignerId =
        FhirGenerator.identifier(MIICoreLocations.UKU.getCode(), IdentifierSystem.NS_DIZ);
    return FhirGenerator.reference(assignerId, MIICoreLocations.UKU.getDisplay());
  }
}
