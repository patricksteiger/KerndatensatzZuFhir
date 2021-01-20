package enums;

import constants.CodingSystem;
import helper.Helper;

import java.util.Arrays;

public enum Aufnahmeanlass {
  EINWEISUNG_DURCH_ARZT("E", "Einweisung durch einen Arzt"),
  EINWEISUNG_DURCH_ZAHNARZT("Z", "Einweisung durch einen Zahnarzt"),
  NOTFALL("N", "Notfall"),
  REHABILITATION(
      "R", "Aufnahme nach vorausgehender Behandlung in einer Rehabilitationseinrichtung"),
  VERLEGUNG_NACH_24H(
      "V", "Verlegung mit Behandlungsdauer im verlegenden Krankenhaus länger als 24 Stunden"),
  VERLEGUNG_VOR_24H(
      "A", "Verlegung mit Behandlungsdauer im verlegenden Krankenhaus bis zu 24 Stunden"),
  GEBURT("G", "Geburt"),
  BEGLEITPERSON("B", "Begleitperson oder mitaufgenommene Pflegekraft");

  private final String code;
  private final String display;

  Aufnahmeanlass(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Return Aufnahmeanlass corresponding to code.
   *
   * @param code Augnahmeanlasscode
   * @return Aufnahmeanlass corresponding to code
   * @throws IllegalArgumentException if code is not E, Z, N, R, V, A, G, B.
   */
  public static Aufnahmeanlass fromCode(String code) {
    return Arrays.stream(Aufnahmeanlass.values())
        .filter(anlass -> anlass.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "Aufnahmeanlass"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.FALL_AUFNAHMEANLASS;
  }
}
