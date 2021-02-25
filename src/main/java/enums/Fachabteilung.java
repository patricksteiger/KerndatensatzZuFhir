package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * Subset of Fachabteilungsschlüssel for Modul Fall
 *
 * @see "https://simplifier.net/medizininformatikinitiative-modulfall/fachabteilungsschluessel"
 */
public enum Fachabteilung implements Code {
  INNERE_MEDIZIN("0100", "Innere Medizin"),
  GERIATRIE("0200", "Geriatrie"),
  KARDIOLOGIE("0300", "Kardiologie"),
  NEPHROLOGIE("0400", "Nephrologie"),
  HAMATOLOGIE_UND_INTERNISTISCHE_ONKOLOGIE("0500", "Hamatologie und internistische Onkologie"),
  ENDOKRINOLOGIE("0600", "Endokrinologie"),
  GASTROENTEROLOGIE("0700", "Gastroenterologie"),
  PNEUMOLOGIE("0800", "Pneumologie"),
  RHEUMATOLOGIE("0900", "Rheumatologie"),
  PADIATRIE("1000", "Padiatrie"),
  KINDERKARDIOLOGIE("1100", "Kinderkardiologie"),
  NEONATOLOGIE("1200", "Neonatologie"),
  KINDERCHIRURGIE("1300", "Kinderchirurgie"),
  LUNGEN_UND_BRONCHIALHEILKUNDE("1400", "Lungen- und Bronchialheilkunde"),
  ALLGEMEINE_CHIRURGIE("1500", "Allgemeine Chirurgie"),
  UNFALLCHIRURGIE("1600", "Unfallchirurgie"),
  NEUROCHIRURGIE("1700", "Neurochirurgie"),
  GEFACHIRURGIE("1800", "Gefa.chirurgie"),
  PLASTISCHE_CHIRURGIE("1900", "Plastische Chirurgie"),
  THORAXCHIRURGIE("2000", "Thoraxchirurgie"),
  HERZCHIRURGIE("2100", "Herzchirurgie"),
  UROLOGIE("2200", "Urologie"),
  ORTHOPADIE("2300", "Orthopadie"),
  FRAUENHEILKUNDE_UND_GEBURTSHILFE("2400", "Frauenheilkunde und Geburtshilfe"),
  GEBURTSHILFE("2500", "Geburtshilfe"),
  HALS_NASEN_OHRENHEILKUNDE("2600", "Hals-, Nasen-, Ohrenheilkunde"),
  AUGENHEILKUNDE("2700", "Augenheilkunde"),
  NEUROLOGIE("2800", "Neurologie"),
  ALLGEMEINE_PSYCHATRIE("2900", "Allgemeine Psychatrie"),
  KINDER_UND_JUGENDPSYCHATRIE("3000", "Kinder- und Jugendpsychiatrie"),
  PSYCHOTHERAPIE("3100", "Psychosomatik/Psychotherapie"),
  NUKLEARMEDIZIN("3200", "Nuklearmedizin"),
  STRAHLENHEILKUNDE("3300", "Strahlenheilkunde"),
  DERMATOLOGIE("3400", "Dermatologie"),
  MUND_UND_KIEFERCHIRURGIE("3500", "Zahn- und Kieferheilkunde, Mund- und Kieferchirurgie"),
  INTENSIVMEDIZIN("3600", "Intensivmedizin"),
  ORTHOPADIE_UND_UNFALLCHIRURGIE("2316", "Orthopadie und Unfallchirurgie"),
  FRAUENHEILKUNDE("2425", "Frauenheilkunde"),
  SONSTIGE("3700", "Sonstige Fachabteilung");

  private final String code;
  private final String display;

  Fachabteilung(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Fachabteilung corresponding to given code. Since Fachabteilung only is a subset of all
   * valid codes, returning empty does not necessarily mean, that the given code is invalid.
   *
   * @param code Fachabteilungsschluessel
   * @return Optional containing Fachabteilung or empty if code is not part of subset.
   * @see "https://simplifier.net/medizininformatikinitiative-modulfall/fachabteilungsschluessel"
   */
  public static Optional<Fachabteilung> fromCode(String code) {
    return Helper.codeFromString(Fachabteilung.values(), code);
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL;
  }
}
