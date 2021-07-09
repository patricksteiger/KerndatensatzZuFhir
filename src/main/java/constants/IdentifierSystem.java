package constants;

public class IdentifierSystem {
  public static final String EMPTY = null;
  public static final String LOCAL_PID = "http://uniklinik-ulm.de/fhir/NamingSystem/pid";
  public static final String VERSICHERTEN_ID_GKV = "http://fhir.de/NamingSystem/gkv/kvid-10";
  public static final String IKNR = "http://fhir.de/NamingSystem/arge-ik/iknr";
  public static final String ACME_PATIENT = "http://www.acme.com/identifiers/patient";
  public static final String NS_DIZ =
      "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/DIZ";
  public static final String SUBJECT_IDENTIFICATION_CODE =
      "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/SubjectIdentificationCode";
  public static final String CORE_LOCATIONS =
      "https://www.medizininformatik-initiative.de/fhir/core/CodeSystem/core-location-identifier";

  private IdentifierSystem() {}
}
