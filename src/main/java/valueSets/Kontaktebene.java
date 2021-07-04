package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Kontaktebene"
 */
public enum Kontaktebene implements Code {
  EINRICHTUNG("einrichtungskontakt", "Einrichtungskontakt"),
  ABTEILUNG("abteilungskontakt", "Abteilungskontakt"),
  VERSORGUNGSSTELLE("versorgungsstellenkontakt", "Versorgungsstellenkontakt");

  private final String code;
  private final String display;

  Kontaktebene(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns corresponding Kontaktebene. Valid codes are einrichtungskontakt, abteilungskontakt and
   * versorgungsstellenkontakt.
   *
   * @param code
   * @return
   */
  public static Optional<Kontaktebene> fromCode(String code) {
    return Helper.codeFromString(Kontaktebene.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.FALL_KONTAKTEBENE;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
