package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

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
   * Returns Aufnahmegrund corresponding to given code. Valid codes: "01"-"08" or "10".
   *
   * @param code Aufnahmegrundcode
   * @return Aufnahmegrund enum
   */
  public static Optional<Aufnahmegrund> fromCode(String code) {
    return Helper.codeFromString(Aufnahmegrund.values(), code);
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
