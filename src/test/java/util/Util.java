package util;

import constants.Constants;
import helper.Helper;
import helper.Logger;
import interfaces.Code;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;

public class Util {
  public static final String LOGGER_FIELD_NAME = "LOGGER";

  private Util() {}

  public static String getValueStr(String value) {
    return "value=\"" + value + "\"";
  }

  public static String getUnitStr(String unit) {
    return "unit=\"" + unit + "\"";
  }

  public static String getValueUnitStr(String value, String unit) {
    return getValueStr(value) + " " + getUnitStr(unit);
  }

  public static String getSystemStr(String system) {
    return "system=\"" + system + "\"";
  }

  public static String getCodeSystemStr(String code, String system) {
    return getCodeStr(code) + " " + getSystemStr(system);
  }

  public static String getCodeDisplaySystemStr(String code, String display, String system) {
    return getCodeDisplayStr(code, display) + " " + getSystemStr(system);
  }

  public static String getCodeDisplayStr(String code, String display) {
    return getCodeStr(code) + " " + getDisplayStr(display);
  }

  public static String getCodeDisplayStr(Code code) {
    return getCodeDisplayStr(code.getCode(), code.getDisplay());
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
    Field field = obj.getClass().getDeclaredField(LOGGER_FIELD_NAME);
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(obj, newValue);
  }

  public static void setUpLoggerMock(Logger mockedLogger) {
    Mockito.when(mockedLogger.emptyValue(any(), any())).thenReturn(Constants.getEmptyValue());
    Mockito.when(mockedLogger.error(any(), any())).thenReturn(Constants.getEmptyValue());
    Mockito.when(mockedLogger.error(any(), any(), any())).thenReturn(Constants.getEmptyValue());
    Mockito.when(mockedLogger.warning(any(), any(), any()))
        .thenAnswer(i -> (Supplier) () -> i.getArgument(0));
    Mockito.when(mockedLogger.emptyValueSupplier(any(), any()))
        .thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.errorSupplier(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.errorSupplier(any(), any(), any()))
        .thenReturn(Constants::getEmptyValue);
    Mockito.when(mockedLogger.warningSupplier(any(), any(), any()))
        .thenAnswer(i -> (Supplier) () -> i.getArgument(0));
  }
}
