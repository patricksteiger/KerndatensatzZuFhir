package util;

import constants.Constants;
import helper.Helper;
import helper.Logger;
import interfaces.Code;

import java.util.Date;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class Util {

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

  public static void setUpLoggerMock(Logger mockedLogger) {
    doReturn(Constants.getEmptyValue()).when(mockedLogger).emptyValue(anyString(), anyString());
    doReturn(Constants.getEmptyValue()).when(mockedLogger).error(anyString(), anyString());
    doReturn(Constants.getEmptyValue()).when(mockedLogger).error(anyString(), anyString(), any());
    doAnswer(returnsFirstArg()).when(mockedLogger).warning(any(), anyString(), anyString());
  }
}
