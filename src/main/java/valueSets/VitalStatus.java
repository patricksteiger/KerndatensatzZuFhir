package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulPerson-ImplementationGuide/Terminologien"
 */
public enum VitalStatus implements Code {
  LEBENDIG("L", "Patient lebt"),
  VERSTORBEN("T", "Patient verstorben"),
  NICHT_AUFFINDBAR("A", "unbekannt, Patient nicht mehr auffindbar (lost to follow-up)"),
  NACHSORGE_NICHT_NOETIG("N", "unbekannt, Betreuung/Nachsorge nicht mehr nötig"),
  ANDERORTS_BETREUUNG("B", "unbekannt, Patient ist anderenorts in Betreuung"),
  VERWEIGERT("V", "unbekannt, Patient verweigert weitere Betreuung"),
  UNBEKANNT("X", "unbekannt");

  private final String code;
  private final String display;

  VitalStatus(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<VitalStatus> fromCode(String code) {
    return Helper.codeFromString(VitalStatus.values(), code);
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.VITALSTATUS;
  }
}
