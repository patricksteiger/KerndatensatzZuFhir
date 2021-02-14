package constants;

import enums.MIICoreLocations;

public class MIIReference {
  public static final String ORGANIZATION = "Organization/";
  public static final String ORGANIZATION_MII = ORGANIZATION + MIICoreLocations.UKU.toString();
  private static final String PATIENT = "Patient/";
  public static final String PATIENT_MII = PATIENT + "MII-Patient";
  private static final String CONSENT = "Consent/";
  public static final String CONSENT_MII = CONSENT + "MII-Consent";
  private static final String RESEARCH_STUDY = "ResearchStudy/";
  public static final String RESEARCH_STUDY_MII = RESEARCH_STUDY + "MII-Beispielstudie";
  private static final String SERVICE_REQUEST = "ServiceRequest/";

  private MIIReference() {}

  public static String getPatient(String patNr) {
    return PATIENT + patNr;
  }

  public static String getServiceRequest(String serviceRequestNr) {
    return SERVICE_REQUEST + serviceRequestNr;
  }
}
