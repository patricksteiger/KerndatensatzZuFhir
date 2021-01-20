package enums;

import constants.CodingSystem;
import helper.Helper;

import java.util.Arrays;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulFall-ImplementationGuide/Terminologien"
 */
public enum Entlassungsgrund {
  G01("01", "Behandlung regulär beendet"),
  G02("02", "Behandlung regulär beendet, nachstationäre Behandlung vorgesehen"),
  G03("03", "Behandlung aus sonstigen Gründen beendet"),
  G04("04", "Behandlung gegen ärztlichen Rat beendet"),
  G05("05", "Zuständigkeitswechsel des Kostenträgers"),
  G06("06", "Verlegung in ein anderes Krankenhaus"),
  G07("07", "Tod"),
  G08(
      "08",
      "Verlegung in ein anderes Krankenhaus im Rahmen einer Zusammenarbeit (§ 14 Abs. 5 Satz 2 BPflV in der am 31.12.2003 geltenden Fassung)"),
  G09("09", "Entlassung in eine Rehabilitationseinrichtung"),
  G10("10", "Entlassung in eine Pflegeeinrichtung"),
  G11("11", "Entlassung in ein Hospiz"),
  G12("12", "interne Verlegung"),
  G13("13", "externe Verlegung zur psychiatrischen Behandlung"),
  G14("14", "Behandlung aus sonstigen Gründen beendet, nachstationäre Behandlung vorgesehen"),
  G15("15", "Behandlung gegen ärztlichen Rat beendet, nachstationäre Behandlung vorgesehen"),
  G16(
      "16",
      "externe Verlegung mit Rückverlegung oder Wechsel zwischen den Entgeltbereichen der DRG-Fallpau- schalen, nach der BPflV oder für besondere Einrichtungen nach § 17b Abs. 1 Satz 15 KHG mit Rückverlegung"),
  G17(
      "17",
      "interne Verlegung mit Wechsel zwischen den Entgeltbereichen der DRG-Fallpauschalen, nach der BPflV oder für besondere Einrichtungen nach § 17b Abs. 1 Satz 15 KHG"),
  G18("18", "Rückverlegung"),
  G19("19", "Entlassung vor Wiederaufnahme mit Neueinstufung"),
  G20("20", "Entlassung vor Wiederaufnahme mit Neueinstufung wegen Komplikation"),
  G21("21", "Entlassung oder Verlegung mit nachfolgender Wiederaufnahme"),
  G22(
      "22",
      "Fallabschluss (interne Verlegung) bei Wechsel zwischen voll-, teilstationärer und stationsäquivalenter Behandlung"),
  G23(
      "23",
      "Beginn eines externen Aufenthalts mit Abwesenheit über Mitternacht (BPflV-Bereich – für verlegende Fachabteilung)"),
  G24(
      "24",
      "Beendigung eines externen Aufenthalts mit Abwesenheit über Mitternacht (BPflV-Bereich – für Pseudo-Fachabteilung 0003)"),
  G25(
      "25",
      "Entlassung zum Jahresende bei Aufnahme im Vorjahr (für Zwecke der Abrechnung - § 4 PEPPV)"),
  G26(
      "26",
      "Beginn eines Zeitraumes ohne direkten Patientenkontakt (stationsäquivalente Behandlung)"),
  G27(
      "27",
      "Beendigung eines Zeitraumes ohne direkten Patientenkontakt (stationsäquivalente Behandlung – für Pseudo-Fachabteilung 0004)"),
  G28("28", "Behandlung regulär beendet, beatmet entlassen"),
  G29("29", "Behandlung regulär beendet, beatmet verlegt");

  private final String code;
  private final String display;

  Entlassungsgrund(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Entlassungsgrund corresponding to given code.
   *
   * @param code Entlassungsgrundcode
   * @return Entlasungsgrund enum
   * @throws IllegalArgumentException if code is not in "01"-"29"
   */
  public static Entlassungsgrund fromCode(String code) {
    return Arrays.stream(Entlassungsgrund.values())
        .filter(grund -> grund.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "Entlassungsgrund"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.FALL_ENTLASSUNGSGRUND;
  }
}
