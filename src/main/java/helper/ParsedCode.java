package helper;

import java.util.ArrayList;
import java.util.List;

public class ParsedCode {
  private static final String SEPARATOR_REGEX = " ";

  private final String code;
  private final String system;
  private final String display;

  public ParsedCode(String code, String system, String display) {
    this.code = code;
    this.system = system;
    this.display = display;
  }

  /**
   * Parses given string. It expects following format:
   * system="http://hl7.org/fhir/ValueSet/diagnostic-report-status" code="final" display="Final"
   * Order of code, system and display doesn't matter, but they are case-sensitive. The separator is
   * expected to be a single space. If code, system or display are missing, they are initialized
   * with an empty string. Quotes around the values are trimmed.
   *
   * @param str String containing code, system and display
   * @return Parsed code, system and display
   */
  public static ParsedCode fromString(String str) {
    List<String> words = splitCode(str);
    String code = Helper.trimQuotes(extractCode(words, "code="));
    String system = Helper.trimQuotes(extractCode(words, "system="));
    String display = Helper.trimQuotes(extractCode(words, "display="));
    return new ParsedCode(code, system, display);
  }

  private static String extractCode(List<String> words, String code) {
    for (String word : words) if (word.startsWith(code)) return word.substring(code.length());
    return "";
  }

  private static List<String> splitCode(String code) {
    List<String> codes = new ArrayList<>();
    if (Helper.checkEmptyString(code)) return codes;
    final int CODE_LEN = code.length();
    int index = 0;
    // Example: code="1234"
    while (index < CODE_LEN) {
      // Skip whitespaces before and after parsed words
      while (index < CODE_LEN && Character.isWhitespace(code.charAt(index))) index++;
      // return codes if no new code was parsed
      if (index >= CODE_LEN) return codes;
      StringBuilder sb = new StringBuilder();
      // Parse every non-whitespace character, e.g. code=
      while (index < CODE_LEN && code.charAt(index) != '\"') {
        sb.append(code.charAt(index));
        index++;
      }
      // If index is valid, add quote
      if (index < CODE_LEN) {
        sb.append(code.charAt(index));
        index++;
      }
      // Parse everything until next quote, e.g. 1234
      while (index < CODE_LEN && code.charAt(index) != '\"') {
        sb.append(code.charAt(index));
        index++;
      }
      // If index is valid, add quote and parsed code to codes
      if (index < CODE_LEN) {
        sb.append(code.charAt(index));
        index++;
        codes.add(sb.toString());
      }
    }
    return codes;
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
}
