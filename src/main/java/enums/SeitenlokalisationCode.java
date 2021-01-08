package enums;

import constants.CodingSystem;

import java.util.Arrays;

import static helper.Helper.illegalCode;

public enum SeitenlokalisationCode {
  RECHTS("R", "rechts", CodingSystem.OPS_SEITENLOKALISATION),
  LINKS("L", "links", CodingSystem.OPS_SEITENLOKALISATION),
  BEIDSEITS("B", "beidseits", CodingSystem.OPS_SEITENLOKALISATION),
  MITTELLINIENZONE("M", "Mittellinienzone", CodingSystem.OPS_SEITENLOKALISATION),
  NICHT_ZUTREFFEND("NA", "nicht zutreffend", CodingSystem.OPS_SEITENLOKALISATION_ERROR),
  UNBEKANNT("UNK", "unbekannt", CodingSystem.OPS_SEITENLOKALISATION_ERROR);

  private final String code;
  private final String display;
  private final String codeSystem;

  SeitenlokalisationCode(String code, String display, String codeSystem) {
    this.code = code;
    this.display = display;
    this.codeSystem = codeSystem;
  }

  /**
   * Gets SeitenlokalisationsCode for given, case-sensitive code.
   *
   * @param code Valid codes: R, L, B, M, NA, UNK.
   * @return SeitenlokalisationsCode
   * @throws IllegalArgumentException If invalid code is given.
   * @see
   *     "https://art-decor.org/art-decor/decor-valuesets--mide-?id=1.2.40.0.34.10.176&effectiveDate="
   */
  public static SeitenlokalisationCode getSeitenlokalisationByCode(String code) {
    return Arrays.stream(SeitenlokalisationCode.values())
        .filter(seitenEnum -> seitenEnum.getCode().equals(code))
        .findFirst()
        .orElseThrow(illegalCode(code, SeitenlokalisationCode.class.getSimpleName()));
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplay() {
    return this.display;
  }

  public String getCodeSystem() {
    return this.codeSystem;
  }
}
