package helper;

import constants.Constants;
import interfaces.Datablock;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static helper.Formatter.*;

public class Logger {

  private final org.slf4j.Logger sljLogger;

  public <T extends Datablock> Logger(Class<T> datablockClass) {
    this.sljLogger = LoggerFactory.getLogger(datablockClass);
  }

  public <T> T error(String method, String valueName, String value) {
    return printError(formatError(method, valueName, value));
  }

  public <T> Supplier<T> errorSupplier(String method, String valueName, String value) {
    return () -> error(method, valueName, value);
  }

  public <T> T error(String method, String message) {
    return printError(formatError(method, message));
  }

  public <T> Supplier<T> errorSupplier(String method, String message) {
    return () -> error(method, message);
  }

  public <T> T emptyValue(String method, String value) {
    return printError(formatEmptyValue(method, value));
  }

  public <T> Supplier<T> emptyValueSupplier(String method, String value) {
    return () -> emptyValue(method, value);
  }

  public <T> T warning(T value, String method, String message) {
    return printWarning(value, formatWarning(method, message));
  }

  public <T> Supplier<T> warningSupplier(Supplier<T> value, String method, String message) {
    return () -> warning(value.get(), method, message);
  }

  public <T> T printError(String message) {
    this.sljLogger.error(message);
    return Constants.getEmptyValue();
  }

  public <T> T printWarning(T value, String message) {
    this.sljLogger.warn(message);
    return value;
  }
}
