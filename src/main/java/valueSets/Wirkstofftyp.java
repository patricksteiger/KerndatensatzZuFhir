package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/medizininformatikinitiative-modulmedikation/wirkstofftyp" */
public enum Wirkstofftyp implements Code {
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
   * @return Wirkstofftyp enum, empty if code is invalid.
   */
  public static Optional<Wirkstofftyp> fromCode(String code) {
    return Helper.codeFromString(Wirkstofftyp.values(), code);
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
