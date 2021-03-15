package helper;

import constants.Constants;
import interfaces.Datablock;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Logger {

  private final org.slf4j.Logger SL4J_LOGGER;
  private long errorCounter;

  public <T extends Datablock> Logger(Class<T> datablockClass) {
    this.SL4J_LOGGER = LoggerFactory.getLogger(datablockClass);
    this.errorCounter = 0L;
  }

  public <T> Supplier<T> error(String method, String valueName, String value) {
    countError();
    return () -> {
      this.SL4J_LOGGER.error(
          "In method \"{}\" an error occurred!\n"
              + "Datablock-Valuename: \"{}\", invalid value: \"{}\".\n",
          method,
          valueName,
          value);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> error(String method, String message) {
    countError();
    return () -> {
      this.SL4J_LOGGER.error(
          "In method \"{}\" an error occurred!\n" + "Message: {}", method, message);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> emptyValue(String method, String value) {
    countError();
    return () -> {
      this.SL4J_LOGGER.error(
          "In method \"{}\" an error occurred!\n" + "Datablock-Value \"{}\" has to have a value!",
          method,
          value);
      return Constants.getEmptyValue();
    };
  }

  public <T> Supplier<T> warn(T value, String method, String message) {
    return () -> {
      this.SL4J_LOGGER.warn("In method {} a warning occured!\n" + "Message: {}", method, message);
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
