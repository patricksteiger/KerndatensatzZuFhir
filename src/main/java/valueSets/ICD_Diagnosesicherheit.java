package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT"
 * @see
 *     "https://art-decor.org/decor/services/RetrieveValueSet?id=1.2.276.0.76.11.121&effectiveDate=&prefix=hl7de-&format=html&collapsable=true&language=*&ui=en-US"
 */
public enum ICD_Diagnosesicherheit implements Code {
  AUSGESCHLOSSEN("A", "ausgeschlossen"),
  GESICHERTE_DIAGNOSE("G", "gesicherte Diagnose"),
  VERDACHT_AUF("V", "Verdacht auf / zum Ausschluss von"),
  ZUSTAND_NACH("Z", "Zustand nach");

  private final String code;
  private final String display;

  ICD_Diagnosesicherheit(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns ICD-Diagnosesicherheit corresponding to SNOMED-code. First SNOMED-Code allows to get
   * Diagnosesicherheit. From there the ICD-Mapping can be extracted.
   *
   * @param snomedCode SNOMED-Code of Diagnosesicherheit
   * @return ICD-Diagnosesicherheit-enum, or empty if code is invalid
   * @see "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT"
   */
  public static Optional<ICD_Diagnosesicherheit> fromSnomedCode(String snomedCode) {
    return Diagnosesicherheit.fromCode(snomedCode).map(Diagnosesicherheit::getIcdMapping);
  }

  public static Optional<ICD_Diagnosesicherheit> fromCode(String code) {
    return Helper.codeFromString(ICD_Diagnosesicherheit.values(), code);
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public String getSystem() {
    return CodingSystem.ICD_DIAGNOSESICHERHEIT;
  }
}
