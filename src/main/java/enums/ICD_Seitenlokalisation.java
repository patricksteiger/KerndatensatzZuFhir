package enums;

import helper.Helper;

import java.util.Arrays;

public enum ICD_Seitenlokalisation {
  RECHTS("R", "rechts"),
  LINKS("L", "links"),
  BEIDSEITIG("B", "beidseitig");

  private final String code;
  private final String display;

  ICD_Seitenlokalisation(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returs ICD-Seitenlokalisation corresponding to case-sensitive code.
   *
   * @param code Valid codes: R, L, B
   * @return ICD-Seitenlokalisation-enum
   * @throws IllegalArgumentException if code is invalid.
   */
  public static ICD_Seitenlokalisation fromCode(String code) {
    return Arrays.stream(ICD_Seitenlokalisation.values())
        .filter(seite -> seite.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "ICD-Seitenlokalisation"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return "http://fhir.de/CodeSystem/kbv/s_icd_seitenlokalisation";
  }
}
