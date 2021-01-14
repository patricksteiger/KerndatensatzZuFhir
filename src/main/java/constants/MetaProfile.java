package constants;

public class MetaProfile {
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
}
