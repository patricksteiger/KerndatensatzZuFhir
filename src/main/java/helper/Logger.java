package helper;

import constants.Constants;
import interfaces.Datablock;
import org.slf4j.LoggerFactory;

public class Logger {

  private final org.slf4j.Logger SL4J_LOGGER;
  private long errorCounter;

  public <T extends Datablock> Logger(Class<T> datablockClass) {
    this.SL4J_LOGGER = LoggerFactory.getLogger(datablockClass);
    this.errorCounter = 0L;
  }

  public <T> T error(String method, String valueName, String value) {
    this.SL4J_LOGGER.error(
        "In method \"{}\" an error occurred!\n"
            + "Datablock-Valuename: \"{}\", invalid value: \"{}\".\n",
        method,
        valueName,
        value);
    this.errorCounter++;
    return Constants.getEmptyValue();
  }

  public <T> T error(String method, String message) {
    this.SL4J_LOGGER.error(
        "In method \"{}\" an error occurred!\n" + "Message: {}", method, message);
    this.errorCounter++;
    return Constants.getEmptyValue();
  }

  public long getErrorCount() {
    return errorCounter;
  }
}
