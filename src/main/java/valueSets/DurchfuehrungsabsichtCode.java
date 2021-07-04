package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
 */
public enum DurchfuehrungsabsichtCode implements Code {
  THERAPEUTIC("262202000", "Therapeutic"),
  PALLIATIVE_INTENT("363676003", "Palliative intent"),
  DIAGNOSTIC_INTENT("261004008", "Diagnostic intent"),
  SYMPTOMATIC("264931009", "Symptomatic"),
  REVISION("255231005", "Revision - value"),
  COMPLICATED("255302009", "Complicated"),
  PREVENTIVE_INTENT("129428001", "Preventive intent"),
  REHABILITATION("394602003", "Rehabilitation - speciality"),
  OTHER("74964007", "Other");

  private final String code;
  private final String display;

  DurchfuehrungsabsichtCode(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Durchfuehrungsabsicht-Code corresponding to SNOMED-Code.
   *
   * @param code SNOMED-Code
   * @return Durchfuehrungs-Code, empty if code is invalid
   * @see
   *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
   */
  public static Optional<DurchfuehrungsabsichtCode> fromCode(String code) {
    return Helper.codeFromString(DurchfuehrungsabsichtCode.values(), code);
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getSystem() {
    return CodingSystem.SNOMED_CLINICAL_TERMS;
  }
}
