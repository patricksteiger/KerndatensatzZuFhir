package enums;

import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/medizininformatikinitiative-modullabor/laborbereich" */
public enum Laborbereich implements Code {
  BLOOD_BANK("18717-9", "BLOOD BANK STUDIES"),
  CELL_MARKER("18718-7", "CELL MARKER STUDIES"),
  CHEMISTRY("18719-5", "CHEMISTRY STUDIES"),
  COAGULATION("18720-3", "COAGULATION STUDIES"),
  THERAPEUTIC_DRUG_MONITORING("18721-1", "THERAPEUTIC DRUG MONITORING STUDIES"),
  FERTILITY("18722-9", "FERTILITY STUDIES"),
  HEMATOLOGY("18723-7", "HEMATOLOGY STUDIES"),
  HLA("18724-5", "HLA STUDIES"),
  MICROBIOLOGY("18725-2", "MICROBIOLOGY STUDIES"),
  SEROLOGY("18727-8", "SEROLOGY STUDIES"),
  TOXICOLOGY("18728-6", "TOXICOLOGY STUDIES"),
  URINALYSIS("18729-4", "URINALYSIS STUDIES"),
  BLOOD_GAS("18767-4", "BLOOD GAS STUDIES"),
  CELL_COUNTS_PLUS_DIFFERENTIAL("18768-2", "CELL COUNTS+DIFFERENTIAL STUDIES"),
  MICROBIAL_SUSCEPTIBILITY("18769-0", "MICROBIAL SUSCEPTIBILITY TESTS"),
  MOLECULAR_PATHOLOGY("26435-8", "MOLECULAR PATHOLOGY STUDIES"),
  LABORATORY("26436-6", "LABORATORY STUDIES"),
  CHEMISTRY_CHALLENGE("26437-4", "CHEMISTRY CHALLENGE STUDIES"),
  CYTOLOGY("26438-2", "CYTOLOGY STUDIES"),
  GENETICS("GE", "Genetics");

  private final String code;
  private final String display;

  Laborbereich(String code, String display) {
    this.code = code;
    this.display = display;
  }

  /**
   * Returns Laborbereich corresponding to code.
   *
   * @param code laborbereich code
   * @return Laborbereich enum corresponding to code, empty if code is invalid
   * @see "https://simplifier.net/medizininformatikinitiative-modullabor/laborbereich"
   */
  public static Optional<Laborbereich> fromCode(String code) {
    return Helper.codeFromString(Laborbereich.values(), code);
  }

  public String getSystem() {
    return this.getCode().equals(GENETICS.getCode())
        ? "http://terminology.hl7.org/CodeSystem/v2-0074"
        : "http://loinc.org";
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }
}
