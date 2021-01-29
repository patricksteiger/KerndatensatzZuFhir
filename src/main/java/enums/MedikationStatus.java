package enums;

import helper.Helper;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationStatement;

import java.util.Arrays;

/**
 * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/78822"
 * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/82252"
 */
public enum MedikationStatus {
  // TODO: Check for correct mappings in MedikationStatus
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
   * unterbrochen, abgebrochen and unbekannt.
   *
   * @param code
   * @return MedaikationStatus enum
   * @throws IllegalArgumentException if code is invalid
   */
  public static MedikationStatus fromCode(String code) {
    return Arrays.stream(MedikationStatus.values())
        .filter(status -> status.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "MedikationStatus"));
  }

  /**
   * Returns MedicationStatementStatus corresponding to MedikationStatus-mapping.
   *
   * @param code MedikationStatus
   * @return MedicationStatementStatus enum
   * @throws IllegalArgumentException if code is invalid MedikationStatus
   */
  public static MedicationStatement.MedicationStatementStatus medicationStatementStatusFromCode(
      String code) {
    return MedikationStatus.fromCode(code).getMedicationStatementStatus();
  }

  /**
   * Returns MedicationAdministrationStatus corresponding to MedikationStatus-mapping.
   *
   * @param code MedikationStatus
   * @return MedicationAdministrationStatus enum
   * @throws IllegalArgumentException if code is invalid MedikationStatus
   */
  public static MedicationAdministration.MedicationAdministrationStatus
      medicationAdministrationStatusFromCode(String code) {
    return MedikationStatus.fromCode(code).getMedicationAdministrationStatus();
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
