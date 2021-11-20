package mapping;

import constants.CodingSystem;
import interfaces.Code;

public class OpsToAtcMapping implements Code {
  private static final String SYSTEM = CodingSystem.ATC_DIMDI;
  private final String atcCode;
  private final String display;

  public OpsToAtcMapping(String atcCode) {
    this.atcCode = atcCode;
    this.display = "";
  }

  @Override
  public String getCode() {
    return atcCode;
  }

  @Override
  public String getSystem() {
    return SYSTEM;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
