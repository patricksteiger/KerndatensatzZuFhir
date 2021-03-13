package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulFall-ImplementationGuide/Terminologien"
 */
public enum Entlassungsgrund implements Code {
  G011("011", "Behandlung regulär beendet, arbeitsfähig entlassen"),
  G012("012", "Behandlung regulär beendet, arbeitsunfähig entlassen"),
  G019("019", "Behandlung regulär beendet, keine Angabe"),
  G021(
      "021",
      "Behandlung regulär beendet, nachstationäre Behandlung vorgesehen, arbeitsfähig entlassen"),
  G022(
      "022",
      "Behandlung regulär beendet, nachstationäre Behandlung vorgesehen, arbeitsunfähig entlassen"),
  G029("029", "Behandlung regulär beendet, nachstationäre Behandlung vorgesehen, keine Angabe"),
  G031("031", "Behandlung aus sonstigen Gründen beendet, arbeitsfähig entlassen"),
  G032("032", "Behandlung aus sonstigen Gründen beendet, arbeitsunfähig entlassen"),
  G039("039", "Behandlung aus sonstigen Gründen beendet, keine Angabe"),
  G041("041", "Behandlung gegen ärztlichen Rat beendet, arbeitsfähig entlassen"),
  G042("042", "Behandlung gegen ärztlichen Rat beendet, arbeitsunfähig entlassen"),
  G049("049", "Behandlung gegen ärztlichen Rat beendet, keine Angabe"),
  G059("059", "Zuständigkeitswechsel des Kostenträgers"),
  G069("069", "Verlegung in ein anderes Krankenhaus"),
  G079("079", "Tod"),
  G089(
      "089",
      "Verlegung in ein anderes Krankenhaus im Rahmen einer Zusammenarbeit (§ 14 Abs. 5 Satz 2 BPflV in der am 31.12.2003 geltenden Fassung)"),
  G099("099", "Entlassung in eine Rehabilitationseinrichtung"),
  G109("109", "Entlassung in eine Pflegeeinrichtung"),
  G119("119", "Entlassung in ein Hospiz"),
  G121("121", "interne Verlegung, arbeitsfähig entlassen"),
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
   * Returns Entlassungsgrund corresponding to given code. Valid codes: "01"-"29".
   *
   * @param code Entlassungsgrundcode
   * @return Entlasungsgrund enum, empty if code is invalid.
   */
  public static Optional<Entlassungsgrund> fromCode(String code) {
    return Helper.codeFromString(Entlassungsgrund.values(), code);
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
