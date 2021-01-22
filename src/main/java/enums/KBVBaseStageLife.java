package enums;

import constants.CodingSystem;
import constants.Constants;
import helper.Helper;

import java.util.Arrays;

/** @see "https://simplifier.net/packages/kbv.basis/1.1.0/files/242198" */
public enum KBVBaseStageLife {
  ADULTHOOD("41847000", "Adulthood (qualifier value)"),
  ADOLESCENCE("263659003", "Adolescence (qualifier value)"),
  CHILDHOOD("255398004", "Childhood (qualifier value)"),
  TODDLER("713153009", "Toddler (qualifier value)"),
  INFANCY("3658006", "Infancy (qualifier value)"),
  NEONATAL("255407002", "Neonatal (qualifier value)"),
  OLD_AGE("271872005", "Old age (qualifier value)");

  private final String code;
  private final String display;

  KBVBaseStageLife(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns KBV Base Stage Life corresponding to code.
   *
   * @param code SNOMED_CT code
   * @return KBV Base Stage Life
   * @throws IllegalArgumentException if code is invalid
   * @see "https://simplifier.net/packages/kbv.basis/1.1.0/files/242198"
   */
  public static KBVBaseStageLife fromCode(String code) {
    return Arrays.stream(KBVBaseStageLife.values())
        .filter(stage -> stage.getCode().equals(code))
        .findFirst()
        .orElseThrow(Helper.illegalCode(code, "KBV Base Stage Life"));
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.SNOMED_CLINICAL_TERMS;
  }

  public String getVersion() {
    return Constants.VERSION_2020;
  }
}
