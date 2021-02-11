package helper;

import java.util.List;

public class ParsedCode {
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
    List<String> words = Helper.splitCode(str);
    String code = Helper.extractCode(words, "code=");
    String system = Helper.extractCode(words, "system=");
    String display = Helper.extractCode(words, "display=");
    // Set code in case of simple format
    if (Helper.checkAllEmptyString(code, system, display)) {
      code = Helper.checkNonEmptyString(str) ? Helper.trimQuotes(str) : "";
    }
    return new ParsedCode(code, system, display);
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

  public boolean hasDisplay() {
    return Helper.checkNonEmptyString(display);
  }
}
