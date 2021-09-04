package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see "https://simplifier.net/guide/LaborbefundinderMedizininformatik-Initiative/Terminologien"
 */
public enum SemiQuantitativesLaborergebnis implements Code {
  ONE_OF_FOUR("260347006", "Present + out of ++++ (qualifier value)"),
  TWO_OF_FOUR("260348001", "Present ++ out of ++++ (qualifier value)"),
  THREE_OF_FOUR("260349009", "Present +++ out of ++++ (qualifier value)"),
  FOUR_OF_FOUR("260350009", "Present ++++ out of ++++ (qualifier value)"),
  NOT_PRESENT("410594000", "Definitely NOT present (qualifier value)"),
  TRACE("260405006", "Trace (qualifier value)"),
  ONE_OF_THREE("441614007", "Present one plus out of three plus"),
  TWO_OF_THREE("441517005", "Present two plus out of three plus"),
  THREE_OF_THREE("441521003", "Present three plus out of three plus");

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
