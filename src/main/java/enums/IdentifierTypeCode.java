package enums;

/** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/80925" */
public enum IdentifierTypeCode {
  MR("MR", "Krankenaktennummer"),
  OBI("OBI", "Observation Instance Identifier"),
  RI("RI", "Resource identifier"),
  XX("XX", "Organisations-ID");

  private final String code;
  private final String display;

  IdentifierTypeCode(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplay() {
    return this.display;
  }
}
