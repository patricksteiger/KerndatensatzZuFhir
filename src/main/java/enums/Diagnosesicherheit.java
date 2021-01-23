package enums;

import constants.CodingSystem;
import helper.Helper;

import java.util.Arrays;

public enum Diagnosesicherheit {
  SUSPECTED("415684004", "Suspected", ICD_Diagnosesicherheit.VERDACHT_AUF),
  DIAGNOSIS_OF_EXCLUSION(
      "733495001", "Diagnosis of exclusion", ICD_Diagnosesicherheit.AUSGESCHLOSSEN),
  CONFIRMED_PRESENT("410605003", "Confirmed present", ICD_Diagnosesicherheit.GESICHERTE_DIAGNOSE),
  STATUS_POST("237679004", "Status post", ICD_Diagnosesicherheit.ZUSTAND_NACH);

  private final String code;
  private final String display;
  private final ICD_Diagnosesicherheit icdMapping;

  Diagnosesicherheit(String code, String display, ICD_Diagnosesicherheit icdMapping) {
    this.code = code;
    this.display = display;
    this.icdMapping = icdMapping;
  }

  /**
   * Returns Diagnosesicherheit corresponding to code.
   *
   * @param code SNOMED Code
   * @return Diagnosesicherheit-enum
   * @throws IllegalArgumentException if code is invalid
   * @see
   *     "https://art-decor.org/decor/services/RetrieveValueSet?prefix=mide-&id=2.16.840.1.113883.3.1937.777.24.11.1&effectiveDate=2018-06-13T13:05:22&version=&format=html&language=de-DE&seetype=live-services"
   */
  public static Diagnosesicherheit fromCode(String code) {
    return Arrays.stream(Diagnosesicherheit.values())
        .filter(diagnosesicherheit -> diagnosesicherheit.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "Diagnosesicherheit"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public ICD_Diagnosesicherheit getIcdMapping() {
    return icdMapping;
  }

  public String getSystem() {
    return CodingSystem.SNOMED_CLINICAL_TERMS;
  }
}
