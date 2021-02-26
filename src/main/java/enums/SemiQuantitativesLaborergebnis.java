package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see "https://simplifier.net/guide/LaborbefundinderMedizininformatik-Initiative/Terminologien"
 */
public enum SemiQuantitativesLaborergebnis implements Code {
  NOT_PRESENT("410594000", "Definitely not present"),
  TRACE("260405006", "Trace"),
  ONE_PLUS("441614007", "Present one plus out of three plus"),
  TWO_PLUS("441517005", "Present two plus out of three plus"),
  THREE_PLUS("441521003", "Present three plus out of three plus"),
  FOUR_PLUS("260350009", "Present four plus out of four plus");

  private final String code;
  private final String display;

  SemiQuantitativesLaborergebnis(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns enum containing semi-quantitatives Laborergebnis corresponding to code.
   *
   * @param code SNOMED-Code
   * @return enum, empty if code is invalid.
   * @see "https://simplifier.net/guide/LaborbefundinderMedizininformatik-Initiative/Terminologien"
   */
  public static Optional<SemiQuantitativesLaborergebnis> fromCode(String code) {
    return Helper.codeFromString(SemiQuantitativesLaborergebnis.values(), code);
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
