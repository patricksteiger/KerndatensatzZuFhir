package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/packages/de.basisprofil.r4/1.0.0-rc4/files/377902" */
public enum EncounterClassDE implements Code {
  AMBULATORY("AMB", "ambulatory"),
  INPATIENT("IMP", "inpatient encounter"),
  PRE_ADMISSION("PRENC", "pre-admission"),
  VIRTUAL("VR", "virtual"),
  SHORT_STAY("SS", "short stay"),
  HOME_HEALTH("HH", "home health");

  private final String code;
  private final String display;

  EncounterClassDE(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<EncounterClassDE> fromCode(String code) {
    return Helper.codeFromString(EncounterClassDE.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.ENCOUNTER_CLASS_DE;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
