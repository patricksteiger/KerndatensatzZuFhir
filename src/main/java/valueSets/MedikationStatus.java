package valueSets;

import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationStatement;

import java.util.Optional;

/**
 * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/78822"
 * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/82252"
 */
public enum MedikationStatus {
  AKTIV(
      "aktiv",
      MedicationStatement.MedicationStatementStatus.ACTIVE,
      MedicationAdministration.MedicationAdministrationStatus.INPROGRESS),
  ABGESCHLOSSEN(
      "abgeschlossen",
      MedicationStatement.MedicationStatementStatus.COMPLETED,
      MedicationAdministration.MedicationAdministrationStatus.COMPLETED),
  GEPLANT(
      "geplant",
      MedicationStatement.MedicationStatementStatus.INTENDED,
      MedicationAdministration.MedicationAdministrationStatus.NOTDONE),
  UNTERBROCHEN(
      "unterbrochen",
      MedicationStatement.MedicationStatementStatus.ONHOLD,
      MedicationAdministration.MedicationAdministrationStatus.ONHOLD),
  ABGEBROCHEN(
      "abgebrochen",
      MedicationStatement.MedicationStatementStatus.STOPPED,
      MedicationAdministration.MedicationAdministrationStatus.STOPPED),
  UNBEKANNT(
      "unbekannt",
      MedicationStatement.MedicationStatementStatus.UNKNOWN,
      MedicationAdministration.MedicationAdministrationStatus.UNKNOWN);

  private final String code;
  private final MedicationStatement.MedicationStatementStatus medicationStatementStatus;
  private final MedicationAdministration.MedicationAdministrationStatus
      medicationAdministrationStatus;

  MedikationStatus(
      String code,
      MedicationStatement.MedicationStatementStatus medicationStatementStatus,
      MedicationAdministration.MedicationAdministrationStatus medicationAdministrationStatus) {
    this.code = code;
    this.medicationStatementStatus = medicationStatementStatus;
    this.medicationAdministrationStatus = medicationAdministrationStatus;
  }

  /**
   * Returns MedikationStatus corresponding to code. Valid codes are aktiv, abgeschlossen, geplant,
   * unterbrochen, abgebrochen and unbekannt. Codes are checked case-sensitively.
   *
   * @param code
   * @return MedaikationStatus enum, empty if code is invalid
   */
  public static Optional<MedikationStatus> fromCode(String code) {
    for (MedikationStatus status : MedikationStatus.values()) {
      if (status.getCode().equals(code)) {
        return Optional.of(status);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns MedicationStatementStatus corresponding to MedikationStatus-mapping.
   *
   * @param code MedikationStatus
   * @return MedicationStatementStatus enum, empty if MedikationStatus is invalid.
   */
  public static Optional<MedicationStatement.MedicationStatementStatus>
      medicationStatementStatusFromCode(String code) {
    return MedikationStatus.fromCode(code).map(MedikationStatus::getMedicationStatementStatus);
  }

  /**
   * Returns MedicationAdministrationStatus corresponding to MedikationStatus-mapping.
   *
   * @param code MedikationStatus
   * @return MedicationAdministrationStatus enum, empty if MedikationStatus is invalid.
   */
  public static Optional<MedicationAdministration.MedicationAdministrationStatus>
      medicationAdministrationStatusFromCode(String code) {
    return MedikationStatus.fromCode(code).map(MedikationStatus::getMedicationAdministrationStatus);
  }

  public String getCode() {
    return code;
  }

  public MedicationStatement.MedicationStatementStatus getMedicationStatementStatus() {
    return medicationStatementStatus;
  }

  public MedicationAdministration.MedicationAdministrationStatus
      getMedicationAdministrationStatus() {
    return medicationAdministrationStatus;
  }
}
