package constants;

public class IdentifierSystem {
  public static final String LOCAL_PID = "http://uniklinik-ulm.de/fhir/NamingSystem/pid";
  public static final String VERSICHERTEN_ID_GKV = "http://fhir.de/NamingSystem/gkv/kvid-10";
  public static final String ORGANIZATION_REFERENCE_ID = "http://fhir.de/NamingSystem/arge-ik/iknr";
  public static final String ACME_PATIENT = "http://www.acme.com/identifiers/patient";
  public static final String NS_DIZ =
      "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/DIZ";
  public static final String PID =
      "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/patient-identifier";
  public static final String SUBJECT_IDENTIFICATION_CODE =
      "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/SubjectIdentificationCode";

  private IdentifierSystem() {}
}
