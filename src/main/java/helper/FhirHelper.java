package helper;

import constants.IdentifierSystem;
import enums.MIICoreLocations;
import org.hl7.fhir.r4.model.*;

import java.util.Arrays;

public class FhirHelper {
  // TODO: Add mapping from Dosage to MedicationAdministrationDosage
  public static MedicationAdministration.MedicationAdministrationDosageComponent
      getMedicationAdministrationDosageFromDosage(Dosage dosage) {
    MedicationAdministration.MedicationAdministrationDosageComponent dosageComponent =
        new MedicationAdministration.MedicationAdministrationDosageComponent();
    return dosageComponent;
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
