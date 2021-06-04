package helper;

/** @see "https://simplifier.net/packages/hl7.fhir.uv.ips/1.0.0/files/244204" */
public class DoseFormUvIps {

  private DoseFormUvIps() {}

  /**
   * Checks if given code is a valid DoseForm. There are two different types of codes. Examples:
   * "50049300" and "22100".
   *
   * @param code Simple formatted code.
   * @return true if code is valid. false otherwise.
   */
  public static boolean validate(String code) {
    if (Helper.checkEmptyString(code) || (code.length() != 5 && code.length() != 8)) {
      return false;
    }
    switch (code.charAt(0)) {
      case '1':
        return isFormCode1(code);
      case '2':
        return isFormCode2(code);
      case '3':
        return isFormCode3(code);
      case '5':
        return isFormCode5(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode5(String code) {
    switch (code.charAt(1)) {
      case '0':
        return isFormCode50(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode50(String code) {
    switch (code.charAt(2)) {
      case '0':
        return isFormCode500(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode500(String code) {
    return isFormCode(code, 1, 66)
        || isFormCode(code, 70, 74)
        || isFormCode(code, 76, 82)
        || stringEndingWith(code, "01", "250", "500")
        || stringEndingWith(code, "09", "300", "500")
        || "50013500".equals(code)
        || stringEndingWith(code, "15", "300", "400", "500")
        || "50020500".equals(code)
        || "50021500".equals(code)
        || "50023500".equals(code)
        || stringEndingWith(code, "26", "250", "500")
        || stringEndingWith(code, "29", "250", "500", "600", "700")
        || stringEndingWith(code, "33", "300", "500")
        || stringEndingWith(code, "36", "100", "500")
        || stringEndingWith(code, "37", "250", "750")
        || "50038500".equals(code)
        || stringEndingWith(code, "39", "300", "500")
        || "50041500".equals(code)
        || "50044500".equals(code)
        || "50045500".equals(code)
        || stringEndingWith(code, "47", "500", "700")
        || stringEndingWith(code, "48", "250", "300", "500", "600", "750")
        || stringEndingWith(code, "49", "100", "250", "270", "300", "500")
        || "50051100".equals(code)
        || "50053500".equals(code)
        || "50055500".equals(code)
        || "50056500".equals(code)
        || stringEndingWith(code, "60", "100", "200", "300", "400", "500")
        || stringEndingWith(code, "61", "300", "500", "600")
        || "50062500".equals(code)
        || stringEndingWith(code, "63", "100", "200", "300", "500");
  }

  private static boolean isFormCode3(String code) {
    switch (code.charAt(1)) {
      case '0':
        return isFormCode30(code);
      case '1':
        return isFormCode31(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode31(String code) {
    return "31030".equals(code) || "31080".equals(code);
  }

  private static boolean isFormCode30(String code) {
    return "30047500".equals(code);
  }

  private static boolean isFormCode2(String code) {
    char c = code.charAt(1);
    switch (c) {
      case '0':
        return isFormCode20(code);
      case '1':
        return isFormCode21(code);
      case '2':
        return isFormCode22(code);
      case '6':
        return isFormCode26(code);
      case '9':
        return isFormCode29(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode29(String code) {
    return "29020".equals(code);
  }

  private static boolean isFormCode26(String code) {
    return "26010".equals(code);
  }

  private static boolean isFormCode22(String code) {
    return "22010".equals(code)
        || "22050".equals(code)
        || "22090".equals(code)
        || "22100".equals(code)
        || "22120".equals(code);
  }

  private static boolean isFormCode21(String code) {
    return "21010".equals(code)
        || "21060".equals(code)
        || "21100".equals(code)
        || "21140".equals(code);
  }

  private static boolean isFormCode20(String code) {
    return "20050".equals(code);
  }

  private static boolean isFormCode1(String code) {
    char n = code.charAt(1);
    switch (n) {
      case '0':
        return isFormCode10(code);
      case '1':
        return isFormCode11(code);
      case '2':
        return isFormCode12(code);
      case '3':
        return isFormCode13(code);
      case '4':
        return isFormCode14(code);
      case '5':
        return isFormCode15(code);
      case '6':
        return isFormCode16(code);
      case '7':
        return isFormCode17(code);
      case '8':
        return isFormCode18(code);
      case '9':
        return isFormCode19(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode19(String code) {
    return "19050".equals(code) || "19100".equals(code);
  }

  private static boolean isFormCode18(String code) {
    return "18040".equals(code) || "18080".equals(code);
  }

  private static boolean isFormCode17(String code) {
    return "17040".equals(code) || "17090".equals(code) || "17120".equals(code);
  }

  private static boolean isFormCode16(String code) {
    return "16040".equals(code);
  }

  private static boolean isFormCode15(String code) {
    return "15090".equals(code) || "15130".equals(code);
  }

  private static boolean isFormCode14(String code) {
    return "14050".equals(code);
  }

  private static boolean isFormCode13(String code) {
    return "13050".equals(code) || "13220".equals(code);
  }

  private static boolean isFormCode12(String code) {
    char c = code.charAt(2);
    switch (c) {
      case '0':
        return isFormCode120(code);
      case '1':
        return isFormCode121(code);
      case '2':
        return isFormCode122(code);
      case '3':
        return isFormCode123(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode123(String code) {
    return isFormCode(code, 1, 3);
  }

  private static boolean isFormCode122(String code) {
    return "12200".equals(code);
  }

  private static boolean isFormCode121(String code) {
    return isFormCode(code, 1, 20)
        || isFormCode(code, 30, 31)
        || "12100".equals(code)
        || "12100500".equals(code)
        || "12109500".equals(code)
        || "12115500".equals(code)
        || "12120".equals(code)
        || "12150".equals(code);
  }

  private static boolean isFormCode120(String code) {
    return "12004000".equals(code);
  }

  private static boolean isFormCode11(String code) {
    char c = code.charAt(2);
    switch (c) {
      case '0':
        return isFormCode110(code);
      case '1':
        return isFormCode111(code);
      case '2':
        return isFormCode112(code);
      case '3':
        return isFormCode113(code);
      case '4':
        return isFormCode114(code);
      case '5':
        return isFormCode115(code);
      case '6':
        return isFormCode116(code);
      case '7':
        return isFormCode117(code);
      case '9':
        return isFormCode119(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode119(String code) {
    return isFormCode(code, 1, 6);
  }

  private static boolean isFormCode117(String code) {
    return isFormCode(code, 1, 2);
  }

  private static boolean isFormCode116(String code) {
    return isFormCode(code, 1, 4);
  }

  private static boolean isFormCode115(String code) {
    return isFormCode(code, 1, 5);
  }

  private static boolean isFormCode114(String code) {
    return isFormCode(code, 1, 5);
  }

  private static boolean isFormCode113(String code) {
    return isFormCode(code, 1, 4) || "11303500".equals(code);
  }

  private static boolean isFormCode112(String code) {
    return isFormCode(code, 1, 18) || "11210500".equals(code) || "11214500".equals(code);
  }

  private static boolean isFormCode111(String code) {
    return isFormCode(code, 1, 18);
  }

  private static boolean isFormCode110(String code) {
    return isFormCode(code, 1, 15) || "11010".equals(code) || "11050".equals(code);
  }

  private static boolean isFormCode10(String code) {
    char c = code.charAt(2);
    switch (c) {
      case '1':
        return isFormCode101(code);
      case '2':
        return isFormCode102(code);
      case '3':
        return isFormCode103(code);
      case '4':
        return isFormCode104(code);
      case '5':
        return isFormCode105(code);
      case '6':
        return isFormCode106(code);
      case '7':
        return isFormCode107(code);
      case '8':
        return isFormCode108(code);
      case '9':
        return isFormCode109(code);
      default:
        return false;
    }
  }

  private static boolean isFormCode109(String code) {
    return isFormCode(code, 1, 16) || "10900500".equals(code);
  }

  private static boolean isFormCode108(String code) {
    return isFormCode(code, 1, 12);
  }

  private static boolean isFormCode107(String code) {
    return isFormCode(code, 1, 15);
  }

  private static boolean isFormCode106(String code) {
    return isFormCode(code, 1, 13) || "10604500".equals(code);
  }

  private static boolean isFormCode105(String code) {
    return isFormCode(code, 1, 25)
        || "10517500".equals(code)
        || "10521500".equals(code)
        || "10539500".equals(code)
        || "10546500".equals(code)
        || isFormCode(code, 47, 50);
  }

  private static boolean isFormCode104(String code) {
    return isFormCode(code, 1, 11);
  }

  private static boolean isFormCode103(String code) {
    return isFormCode(code, 1, 23)
        || "10314005".equals(code)
        || "10314010".equals(code)
        || "10314011".equals(code);
  }

  private static boolean isFormCode102(String code) {
    return isFormCode(code, 1, 31) || "10236100".equals(code);
  }

  private static boolean isFormCode101(String code) {
    return isFormCode(code, 1, 22);
  }

  private static boolean isFormCode(String code, int lo, int hi) {
    // Example-Code: 101xx000, 01 <= xx <= 22
    if (code.length() != 8 || !code.endsWith("000")) {
      return false;
    }
    String xx = code.substring(3, 5);
    return isStringInRange(xx, lo, hi);
  }

  private static boolean isStringInRange(String s, int lo, int hi) {
    try {
      int n = Integer.parseInt(s);
      return lo <= n && n <= hi;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean stringEndingWith(String code, String middle, String... endings) {
    String xx = code.substring(3, 5);
    if (!xx.equals(middle)) {
      return false;
    }
    for (String ending : endings) {
      if (code.endsWith(ending)) {
        return true;
      }
    }
    return false;
  }
}
