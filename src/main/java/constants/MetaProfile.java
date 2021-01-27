package constants;

public class MetaProfile {
  // Fall
  public static final String FALL_ENCOUNTER =
      "https://www.medizininformatik-initiative.de/fhir/core/StructureDefinition/Encounter/KontaktGesundheitseinrichtung";
  public static final String DIAGNOSE_CONDITION =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-diagnose/StructureDefinition/Diagnose";
  public static final String MEDIKATION_MEDICATION =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/Medication";
  public static final String MEDIKATION_MEDICATION_ADMINISTRATION =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/MedicationAdministration";
  public static final String MEDIKATION_MEDICATION_STATEMENT =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/MedicationStatement";
  // Prozedur
  private static final String STRUCTURE_PROZEDUR =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-prozedur/StructureDefinition/";
  public static final String PROZEDUR_PROCEDURE = STRUCTURE_PROZEDUR + "Procedure";
  // Person
  private static final String STRUCTURE_PERSON =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-person/StructureDefinition/";
  public static final String PERSON_PATIENT = STRUCTURE_PERSON + "Patient";
  public static final String PERSON_RESEARCH_SUBJECT = STRUCTURE_PERSON + "ResearchSubject";
  public static final String PERSON_OBSERVATION = STRUCTURE_PERSON + "Vitalstatus";
  // Laborbefund
  private static final String STRUCTURE_LABOR =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/";
  public static final String LABOR_DIAGNOSTIC_REPORT = STRUCTURE_LABOR + "DiagnosticReportLab";
  public static final String LABOR_SERVICE_REQUEST = STRUCTURE_LABOR + "ServiceRequestLab";
  public static final String LABOR_OBSERVATION = STRUCTURE_LABOR + "ObservationLab";
}
