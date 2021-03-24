package helper;

import constants.Constants;
import interfaces.Datablock;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Logger {

  private static final String ERROR_IN_MEITHOD = "An error occurred in method: {}\n";
  private final org.slf4j.Logger SL4J_LOGGER;
  private long errorCounter;

  public <T extends Datablock> Logger(Class<T> datablockClass) {
    this.SL4J_LOGGER = LoggerFactory.getLogger(datablockClass);
    this.errorCounter = 0L;
  }

  public <T> Supplier<T> error(String method, String valueName, String value) {
    return () -> {
      countError();
      this.SL4J_LOGGER.error(
          ERROR_IN_MEITHOD + "Datablock-Value \"{}\", invalid value: \"{}\".\n",
          method,
          valueName,
          value);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> error(String method, String message) {
    return () -> {
      countError();
      this.SL4J_LOGGER.error(ERROR_IN_MEITHOD + "Message: {}", method, message);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> emptyValue(String method, String value) {
    return () -> {
      countError();
      this.SL4J_LOGGER.error(
          ERROR_IN_MEITHOD + "Datablock-Value \"{}\" has to have a value!\n", method, value);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> warn(T value, String method, String message) {
    return () -> {
      this.SL4J_LOGGER.warn("A warning occured in method: {}\n" + "Message: {}\n", method, message);
      return value;
    };
  }

  private void countError() {
    this.errorCounter++;
  }

  public long getErrorCount() {
    return errorCounter;
  }
}
