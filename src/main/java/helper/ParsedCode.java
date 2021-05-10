package helper;

import interfaces.Code;

import java.util.List;
import java.util.Objects;

public class ParsedCode implements Code {
  public static final String CODE_PREFIX = "code=";
  public static final String SYSTEM_PREFIX = "system=";
  public static final String DISPLAY_PREFIX = "display=";

  private final String code;
  private final String system;
  private final String display;

  public ParsedCode(String code, String system, String display) {
    this.code = code;
    this.system = system;
    this.display = display;
  }

  /**
   * Parses given string. It expects following formats:
   * "system="http://hl7.org/fhir/ValueSet/diagnostic-report-status" code="final" display="Final""
   * or ""final"". Order of code, system and display doesn't matter, but they are case-sensitive. If
   * code, system or display are missing, they are initialized with an empty string. Quotes around
   * the values are trimmed. Currently doesn't handle quotes within the values. If simple value is
   * given (second format) it is parsed as code.
   *
   * @param str String containing code, system and display
   * @return Parsed code, system and display
   */
  public static ParsedCode fromString(String str) {
    if (Helper.checkEmptyString(str)) {
      return new ParsedCode("", "", "");
    }
    if (isSimpleFormattedCode(str)) {
      String code = Helper.trimQuotes(str);
      return new ParsedCode(code, "", "");
    }
    List<String> words = Helper.splitCode(str);
    String code = Helper.extractCode(words, CODE_PREFIX);
    String system = Helper.extractCode(words, SYSTEM_PREFIX);
    String display = Helper.extractCode(words, DISPLAY_PREFIX);
    return new ParsedCode(code, system, display);
  }

  /**
   * Same as "fromString"-method only expecting 1 String, but always sets system to given system.
   *
   * @param str
   * @param system
   * @return
   */
  public static ParsedCode fromString(String str, String system) {
    if (Helper.checkEmptyString(str)) {
      return new ParsedCode("", system, "");
    }
    if (isSimpleFormattedCode(str)) {
      String code = Helper.trimQuotes(str);
      return new ParsedCode(code, system, "");
    }
    List<String> words = Helper.splitCode(str);
    String code = Helper.extractCode(words, CODE_PREFIX);
    String display = Helper.extractCode(words, DISPLAY_PREFIX);
    return new ParsedCode(code, system, display);
  }

  private static boolean isSimpleFormattedCode(String code) {
    return !code.contains(CODE_PREFIX)
        && !code.contains(SYSTEM_PREFIX)
        && !code.contains(DISPLAY_PREFIX);
  }

  public String getCode() {
    return code;
  }

  public String getSystem() {
    return system;
  }

  public String getDisplay() {
    return display;
  }

  public boolean hasEmptyCode() {
    return Helper.checkEmptyString(code);
  }

  public boolean hasDisplay() {
    return Helper.checkNonEmptyString(display);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ParsedCode that = (ParsedCode) o;
    return getCode().equals(that.getCode())
        && getSystem().equals(that.getSystem())
        && getDisplay().equals(that.getDisplay());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode(), getSystem(), getDisplay());
  }
}
