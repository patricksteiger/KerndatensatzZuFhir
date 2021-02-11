package helper;

import constants.Constants;
import interfaces.Datablock;
import org.slf4j.LoggerFactory;

public class Logger {

  private final org.slf4j.Logger logger;
  private long errorCounter;

  public <T extends Datablock> Logger(Class<T> datablockClass) {
    this.logger = LoggerFactory.getLogger(datablockClass);
    this.errorCounter = 0L;
  }

  public <T extends Object> T error(String method, String valueName, String value) {
    this.logger.error(
        "In method \"{}\" an error occurred!\n"
            + "Datablock-Valuename: \"{}\", invalid value: \"{}\".\n",
        method,
        valueName,
        value);
    this.errorCounter++;
    return Constants.getEmptyValue();
  }

  public long getErrorCount() {
    return errorCounter;
  }
}
