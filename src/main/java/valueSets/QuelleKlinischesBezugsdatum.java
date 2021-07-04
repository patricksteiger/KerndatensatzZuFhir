package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see
 *     "https://simplifier.net/medizininformatikinitiative-modullabor/valuesetquelleklinischesbezugsdatum"
 */
public enum QuelleKlinischesBezugsdatum implements Code {
  SPECIMEN("399445004", "Specimen collection date (observable entity)"),
  LABORATORY("281271004", "Date sample received in laboratory (observable entity)");

  private final String code;
  private final String display;

  QuelleKlinischesBezugsdatum(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<QuelleKlinischesBezugsdatum> fromCode(String code) {
    return Helper.codeFromString(QuelleKlinischesBezugsdatum.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.SNOMED_CLINICAL_TERMS;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
