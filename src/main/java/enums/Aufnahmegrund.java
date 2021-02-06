package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Arrays;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulFall-ImplementationGuide/Terminologien"
 */
public enum Aufnahmegrund implements Code {
  G01("01", "Krankenhausbehandlung, vollstationär"),
  G02("02", "Krankenhausbehandlung, vollstationär mit vorausgegangener vorstationärer Behandlung"),
  G03("03", "Krankenhausbehandlung, teilstationär"),
  G04("04", "vorstationäre Behandlung ohne anschließende vollstationäre Behandlung"),
  G05("05", "Stationäre Entbindung"),
  G06("06", "Geburt"),
  G07("07", "Wiederaufnahme wegen Komplikationen (Fallpauschale) nach KFPV 2003"),
  G08("08", "Stationäre Aufnahme zur Organentnahme"),
  G10("10", "Stationsäquivalente Behandlung");

  private final String code;
  private final String display;

  Aufnahmegrund(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Aufnahmegrund corresponding to given code.
   *
   * @param code Aufnahmegrundcode
   * @return Aufnahmegrund enum
   * @throws IllegalArgumentException if code is not in "01"-"08" or "10"
   */
  public static Aufnahmegrund fromCode(String code) {
    return Arrays.stream(Aufnahmegrund.values())
        .filter(grund -> grund.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "Aufnahmegrund"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.FALL_AUFNAHMEGRUND;
  }
}
