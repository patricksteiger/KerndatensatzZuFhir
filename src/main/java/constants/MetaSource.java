package constants;

public class MetaSource {
  private static final String UKU_SAP = "UKU-SAP-";

  // Prozedur
  public static final String PROZEDUR_PROCEDURE = UKU_SAP + "Prozedur-Procedure";
  // Person
  public static final String PERSON_PATIENT = UKU_SAP + "Person-Patient";
  public static final String PERSON_RESEARCH_SUBJECT = UKU_SAP + "Person-ResearchSubject";
  public static final String PERSON_OBSERVATION = UKU_SAP + "Person-Observation";
  public static final String PERSON_TODESURSACHE = UKU_SAP + "Person-Todesursache";
  // Laborbefund
  public static final String LABOR_DIAGNOSTIC_REPORT = UKU_SAP + "Laborbefund-DiagnosticReport";
  public static final String LABOR_SERVICE_REQUEST = UKU_SAP + "Laborbefund-ServiceRequest";
  public static final String LABOR_OBSERVATION = UKU_SAP + "Laborbefund-Observation";
  // Fall
  public static final String FALL_EINRICHTUNG_ENCOUNTER = UKU_SAP + "Fall-Einrichtung-Encounter";
  public static final String FALL_ABTEILUNG_ENCOUNTER = UKU_SAP + "Fall-Abteilung-Encounter";
  public static final String FALL_VERSORGUNGSSTELLE_ENCOUNTER =
      UKU_SAP + "Fall-Versorgungsstelle-Encounter";
  public static final String DIAGNOSE_CONDITION = UKU_SAP + "Diagnose-Condition";
  public static final String MEDIKATION_MEDICATION = UKU_SAP + "Medikation-Medication";
  public static final String MEDIKATION_MEDICATION_ADMINISTRATION =
      UKU_SAP + "Medikation-MedicationAdministration";
  public static final String MEDIKATION_MEDICATION_STATEMENT =
      UKU_SAP + "Medikation-MedicationStatement";
}
