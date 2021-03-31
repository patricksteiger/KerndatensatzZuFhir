package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/83316" */
public enum ReferenceRangeMeaning implements Code {
  TYPE("type", "Type"),
  NORMAL("normal", "Normal Range"),
  RECOMMENDED("recommended", "Recommended Range"),
  TREATMENT("treatment", "Treatment Range"),
  THERAPEUTIC("therapeutic", "Therapeutic Desired Level"),
  PRE("pre", "Pre Therapeutic Desired Level"),
  POST("post", "Post Therapeutic Desired Level"),
  ENDOCRINE("endocrine", "Endocrine"),
  PRE_PUBERTY("pre-puberty", "Pre-Puberty"),
  FOLLICULAR("follicular", "Follicular Stage"),
  MIDCYCLE("midcycle", "MidCycle"),
  LUTEAL("luteal", "Luteal"),
  POST_MENOPAUSAL("postmenopausal", "Post-Menopause");

  private final String code;
  private final String display;

  ReferenceRangeMeaning(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<ReferenceRangeMeaning> fromCode(String code) {
    return Helper.codeFromString(ReferenceRangeMeaning.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.REFERENCE_RANGE_MEANING;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
