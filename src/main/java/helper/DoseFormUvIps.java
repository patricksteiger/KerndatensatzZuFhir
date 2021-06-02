package helper;

import constants.CodingSystem;
import interfaces.Code;

import java.util.Optional;

/** @see "https://simplifier.net/packages/hl7.fhir.uv.ips/1.0.0/files/244204" */
public class DoseFormUvIps implements Code {
  private final String code;
  private final String display;

  private DoseFormUvIps(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<DoseFormUvIps> fromCode(String code) {
    return Optional.of(new DoseFormUvIps(code, ""));
  }

  public static boolean validDoseFormUvIpsCode(String code) {
    return isFormCode101(code)
        || isFormCode102(code)
        || isFormCode103(code)
        || isFormCode104(code)
        || isFormCode105(code)
        || isFormCode106(code)
        || isFormCode107(code)
        || isFormCode108(code)
        || isFormCode109(code)
        || isFormCode110(code);
  }

  private static boolean isFormCode110(String code) {
    return isFormCode(code, "110", 1, 15) || "11010".equals(code) || "11050".equals(code);
  }

  private static boolean isFormCode109(String code) {
    return isFormCode(code, "109", 1, 16) || "10900500".equals(code);
  }

  private static boolean isFormCode108(String code) {
    return isFormCode(code, "108", 1, 12);
  }

  private static boolean isFormCode107(String code) {
    return isFormCode(code, "107", 1, 15);
  }

  private static boolean isFormCode106(String code) {
    return isFormCode(code, "106", 1, 13) || "10604500".equals(code);
  }

  private static boolean isFormCode105(String code) {
    return isFormCode(code, "105", 1, 25)
        || "10517500".equals(code)
        || "10521500".equals(code)
        || "10539500".equals(code)
        || "10546500".equals(code)
        || isFormCode(code, "105", 47, 50);
  }

  private static boolean isFormCode104(String code) {
    return isFormCode(code, "104", 1, 11);
  }

  private static boolean isFormCode103(String code) {
    return isFormCode(code, "103", 1, 23)
        || "10314005".equals(code)
        || "10314010".equals(code)
        || "10314011".equals(code);
  }

  private static boolean isFormCode102(String code) {
    return isFormCode(code, "102", 1, 31) || "10236100".equals(code);
  }

  private static boolean isFormCode101(String code) {
    return isFormCode(code, "101", 1, 22);
  }

  private static boolean isFormCode(String code, String start, int lo, int hi) {
    // Example-Code: 101xx000, 01 <= xx <= 22
    if (code.length() != 8 || !code.startsWith(start) || !code.endsWith("000")) {
      return false;
    }
    String xx = code.substring(3, 5);
    return isStringInRange(xx, lo, hi);
  }

  private static boolean isStringInRange(String s, int lo, int hi) {
    try {
      int n = Integer.parseInt(s);
      return lo <= n && n <= hi;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean isCharInRange(char c, char lo, char hi) {
    return lo <= c && c <= hi;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.EDQM_STANDARD;
  }

  @Override
  public String getDisplay() {
    return null;
  }
}
