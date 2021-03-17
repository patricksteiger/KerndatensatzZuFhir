package util;

import constants.Constants;
import helper.Logger;
import model.Fall;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;

public class Util {
  public static final String FALL_LOGGER_FIELD_NAME = "LOGGER";

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

  public static void setFinalStatic(Fall fall, Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(fall, newValue);
  }

  public static void setUpLoggerMock(Logger mockedLogger) {
    Mockito.when(mockedLogger.emptyValue(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.error(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.error(any(), any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.warn(any(), any(), any()))
        .thenAnswer(i -> (Supplier) () -> i.getArgument(0));
  }
}
