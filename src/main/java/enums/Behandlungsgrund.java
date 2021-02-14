package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/81039" */
public enum Behandlungsgrund implements Code {
  NONE("a", "None"),
  GIVEN_AS_ORDERED("b", "Given as Ordered"),
  EMERGENCY("c", "Emergency");

  private final String code;
  private final String display;

  Behandlungsgrund(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Behandlungsgrund corresponding to given code. Valid codes: a, b and c.
   *
   * @param code Behandlunggrund
   * @return Behandlungsgrund, or empty if code is invalid
   */
  public static Optional<Behandlungsgrund> fromCode(String code) {
    return Helper.codeFromString(Behandlungsgrund.values(), code);
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.BEHANDLUNGSGRUND;
  }
}
