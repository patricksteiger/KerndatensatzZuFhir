package util;

public class Util {
  private Util() {}

  public static String getCodeDisplayStr(String code, String display) {
    return getCodeStr(code) + " " + getDisplayStr(display);
  }

  public static String getDisplayStr(String display) {
    return "display=\"" + display + "\"";
  }

  public static String getCodeStr(String code) {
    return "code=\"" + code + "\"";
  }

  public static String expectedDateString(String date) {
    String[] numbers = date.split("-");
    return numbers[2] + "." + numbers[1] + "." + numbers[0] + " 00:00:00";
  }
}
