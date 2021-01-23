package enums;

public enum ICD_Diagnosesicherheit {
  AUSGESCHLOSSEN("A", "ausgeschlossen"),
  GESICHERTE_DIAGNOSE("G", "gesicherte Diagnose"),
  VERDACHT_AUF("V", "Verdacht auf / zum Ausschluss von"),
  ZUSTAND_NACH("Z", "Zustand nach");

  private final String code;
  private final String display;

  ICD_Diagnosesicherheit(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns ICD-Diagnosesicherheit corresponding to SNOMED-code. First SNOMED-Code allows to get
   * Diagnosesicherheit. From there the ICD-Mapping can be extracted.
   *
   * @param snomedCode SNOMED-Code of Diagnosesicherheit
   * @return ICD-Diagnosesicherheit-enum
   * @throws IllegalArgumentException if snomedCode is invalid for Diagnosesicherheit
   * @see "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT"
   */
  public static ICD_Diagnosesicherheit fromSnomedCode(String snomedCode) {
    Diagnosesicherheit diagnosesicherheit = Diagnosesicherheit.fromCode(snomedCode);
    return diagnosesicherheit.getIcdMapping();
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT";
  }
}
