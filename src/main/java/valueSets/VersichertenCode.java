package valueSets;

import constants.CodingSystem;
import interfaces.Code;

/** @see "http://fhir.de/CodeSystem/identifier-type-de-basis" */
public enum VersichertenCode implements Code {
  GKV("GKV", "Gesetzliche Krankenversicherung"),
  PKV("PKV", "Private Krankenversicherung"),
  ZANR("ZANR", "Zahnarztnummer"),
  KZVA("KZVA", "KZVAbrechnungsnummer");

  private final String code;
  private final String display;

  VersichertenCode(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getSystem() {
    return CodingSystem.IDENTIFIER_TYPE_DE;
  }
}
