package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see
 *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
 */
public enum ProcedureCategorySnomedMapping implements Code {
  DIAGNOSTIC("103693007", "Diagnostic procedure", '1'),
  IMAGING("363679005", "Imaging", '3'),
  SURGICAL("387713003", "Surgical procedure", '5'),
  ADMINISTRATION_OF_MEDICINE("18629005", "Administration of medicine", '6'),
  THERAPEUTIC("277132007", "Therapeutic procedure", '8'),
  OTHER("394841004", "Other category", '9');

  private final String code;
  private final String display;
  private final char opsMapping;

  ProcedureCategorySnomedMapping(String code, String display, char opsMapping) {
    this.code = code;
    this.display = display;
    this.opsMapping = opsMapping;
  }

  /**
   * Returns SNOMED-Mapping corresponding to OPS-Code.
   *
   * @param opsCode OPS-Code
   * @return SNOMED-Mapping, empty if code is empty or first character is not 1,3,5,6,8,9
   * @see
   *     "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
   */
  public static Optional<ProcedureCategorySnomedMapping> fromOpsCode(String opsCode) {
    if (Helper.checkEmptyString(opsCode)) {
      return Optional.empty();
    }
    char opsMap = opsCode.charAt(0);
    for (ProcedureCategorySnomedMapping mapping : ProcedureCategorySnomedMapping.values()) {
      if (mapping.getOpsMapping() == opsMap) {
        return Optional.of(mapping);
      }
    }
    return Optional.empty();
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplay() {
    return this.display;
  }

  public char getOpsMapping() {
    return this.opsMapping;
  }

  public String getSystem() {
    return CodingSystem.SNOMED_CLINICAL_TERMS;
  }
}
