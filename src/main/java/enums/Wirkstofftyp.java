package enums;

import constants.CodingSystem;
import helper.Helper;

import java.util.Arrays;

/** @see "https://simplifier.net/medizininformatikinitiative-modulmedikation/wirkstofftyp" */
public enum Wirkstofftyp {
  ALLGEMEIN("IN", "Wirkstoff allgemein"),
  PRAEZISE("PIN", "Wirkstoff präzise"),
  KOMBINATION("MIN", "Kombinationswirkstoff");

  private final String code;
  private final String display;

  Wirkstofftyp(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Wirkstofftyp corresponding to code. Valid codes are IN, PIN and MIN.
   *
   * @param code Wirkstofftyp
   * @return Wirkstofftyp enum
   * @throws IllegalArgumentException if code is invalid
   */
  public static Wirkstofftyp fromCode(String code) {
    return Arrays.stream(Wirkstofftyp.values())
        .filter(typ -> typ.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "Wirkstofftyp"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.MEDIKATION_WIRKSTOFFTYP;
  }
}
