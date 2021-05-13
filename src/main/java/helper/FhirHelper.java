package helper;

import constants.IdentifierSystem;
import enums.MIICoreLocations;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;
import unit.converter.UnitConverter;

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

  // TODO: Implement changes to "divers"-gender when finalized
  public static Enumerations.AdministrativeGender getGenderMapping(String gender) {
    if ("f".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.FEMALE;
    if ("m".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.MALE;
    if ("un".equalsIgnoreCase(gender)) return Enumerations.AdministrativeGender.OTHER;
    return Enumerations.AdministrativeGender.UNKNOWN;
  }

  public static Reference getMIIPatientReference(String patNr) {
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId = FhirGenerator.identifier(patNr, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirGenerator.reference(subjectId);
  }

  public static Reference getUKUAssignerReference() {
    Identifier assignerId =
        FhirGenerator.identifier(MIICoreLocations.UKU.name(), IdentifierSystem.NS_DIZ);
    return FhirGenerator.reference(assignerId, MIICoreLocations.UKU.toString());
  }
}
