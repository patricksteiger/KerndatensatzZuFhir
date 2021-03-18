package util;

import constants.Constants;
import helper.Helper;
import helper.Logger;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
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

  public static Date expectedDateString(String date) {
    return Helper.getDateFromISO(date).get();
  }

  public static void setMockLoggerField(Object obj, Object newValue) throws Exception {
    Field field = obj.getClass().getDeclaredField(FALL_LOGGER_FIELD_NAME);
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(obj, newValue);
  }

  public static void setUpLoggerMock(Logger mockedLogger) {
    Mockito.when(mockedLogger.emptyValue(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.error(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.error(any(), any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.warn(any(), any(), any()))
        .thenAnswer(i -> (Supplier) () -> i.getArgument(0));
  }
}
