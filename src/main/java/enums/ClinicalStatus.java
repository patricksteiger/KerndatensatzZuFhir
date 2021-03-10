package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "http://terminology.hl7.org/CodeSystem/condition-clinical" */
public enum ClinicalStatus implements Code {
  ACTIVE("active", "Active"),
  RECURRENCE("recurrence", "Recurrence"),
  RELAPSE("relapse", "Relapse"),
  INACTIVE("inactive", "Inactive"),
  REMISSION("remission", "Remission"),
  RESOLVED("resolved", "Resolved");

  private final String code;
  private final String display;

  ClinicalStatus(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<ClinicalStatus> fromCode(String code) {
    return Helper.codeFromString(ClinicalStatus.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.CONDITION_CLINICAL_STATUS;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
