package helper;

import java.util.Objects;

public class LoggingData {
  public final String METHOD_NAME;
  public final String VALUE_NAME;
  public final String MESSAGE;
  public final Logger LOGGER;

  private LoggingData(Logger LOGGER, String methodName, String valueName, String message) {
    this.LOGGER = LOGGER;
    this.METHOD_NAME = methodName;
    this.VALUE_NAME = valueName;
    this.MESSAGE = message;
  }

  // FIXME: Remove requireNonNulls
  public static LoggingData of(Logger LOGGER, String methodName, String valueName) {
    Objects.requireNonNull(LOGGER);
    Objects.requireNonNull(methodName);
    Objects.requireNonNull(valueName);
    return new LoggingData(LOGGER, methodName, valueName, "");
  }

  public static LoggingData withMessage(Logger LOGGER, String methodName, String message) {
    Objects.requireNonNull(LOGGER);
    Objects.requireNonNull(methodName);
    Objects.requireNonNull(message);
    return new LoggingData(LOGGER, methodName, "", message);
  }
}
