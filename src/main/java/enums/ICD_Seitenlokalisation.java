package enums;

import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://applications.kbv.de/S_ICD_SEITENLOKALISATION_V1.00.xhtml" */
public enum ICD_Seitenlokalisation implements Code {
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
   * @return ICD-Seitenlokalisation-enum, empty if code is invalid
   */
  public static Optional<ICD_Seitenlokalisation> fromCode(String code) {
    return Helper.codeFromString(ICD_Seitenlokalisation.values(), code);
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
