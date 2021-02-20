package enums;

import constants.CodingSystem;
import interfaces.Code;

import java.util.Optional;

public enum SemiQuantitativesLaborergebnis implements Code {
  NOT_PRESENT("410594000", "Definitely not present", "-"),
  TRACE("260405006", "Trace", "0"),
  ONE_PLUS("441614007", "Present one plus out of three plus", "+"),
  TWO_PLUS("441517005", "Present two plus out of three plus", "++"),
  THREE_PLUS("441521003", "Present three plus out of three plus", "+++"),
  FOUR_PLUS("260350009", "Present four plus out of four plus", "++++");

  private final String code;
  private final String display;
  private final String mapping;

  SemiQuantitativesLaborergebnis(String code, String display, String mapping) {
    this.code = code;
    this.display = display;
    this.mapping = mapping;
  }

  public static Optional<SemiQuantitativesLaborergebnis> fromCode(String code) {
    for (SemiQuantitativesLaborergebnis ergebnis : SemiQuantitativesLaborergebnis.values()) {
      if (ergebnis.getMapping().equals(code)) {
        return Optional.of(ergebnis);
      }
    }
    return Optional.empty();
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

  public String getMapping() {
    return mapping;
  }
}
