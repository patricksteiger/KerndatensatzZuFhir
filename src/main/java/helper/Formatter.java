package helper;

public class Formatter {
  private static final String ERROR_IN_METHOD = "An error occurred in method: ";

  private Formatter() {}

  public static String formatError(String method, String valueName, String value) {
    return ERROR_IN_METHOD
        + method
        + "\nDatablock-Value \""
        + valueName
        + "\", invalid value: \""
        + value
        + "\".\n";
  }

  public static String formatError(String method, String message) {
    return ERROR_IN_METHOD + method + "\nMessage: " + message + "\n";
  }

  public static String formatEmptyValue(String method, String value) {
    return ERROR_IN_METHOD + method + "\nDatablock-Value \"" + value + "\" has to have a value!\n";
  }

  public static String formatWarning(String method, String message) {
    return "A warning occured in method: " + method + "\nMessage: " + message + "\n";
  }
}
