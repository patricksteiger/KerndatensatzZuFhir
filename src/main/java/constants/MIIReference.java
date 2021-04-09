package constants;

import enums.MIICoreLocations;

public class MIIReference {
  private static final String ORGANIZATION = "Organization/";
  public static final String MII_ORGANIZATION = ORGANIZATION + MIICoreLocations.UKU;
  private static final String PATIENT = "Patient/";
  public static final String MII_PATIENT = PATIENT + "MII-Patient";
  private static final String CONSENT = "Consent/";
  public static final String MII_CONSENT = CONSENT + "MII-Consent";
  private static final String RESEARCH_STUDY = "ResearchStudy/";
  public static final String MII_RESEARCH_STUDY = RESEARCH_STUDY + "MII-Beispielstudie";
  private static final String SERVICE_REQUEST = "ServiceRequest/";
  public static final String MII_SERVICE_REQUEST = SERVICE_REQUEST + "MII-ServiceRequest";
  private static final String MEDICATION = "Medication/";
  public static final String REF_MEDICATION = MEDICATION + "Reference-Medication";

  private MIIReference() {}
}
