package valueSet;

import interfaces.Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class CodeUtil {

  public static final String RESOURCES_PATH = "src/test/resources/valueSet/";

  /**
   * To assert all valid codes this methods assumes a json-file in test/resources/valueSet named
   * testedClassCodes.json exists, and on the root level exists the "codes"-field with all valid
   * codes. Also the given class has to have a fromCode-Method expecting the code and returning an
   * optional containing an instance of the testedClass. It also has to be be an Enum.
   *
   * @param testedClass
   * @param <T>
   * @param <Enum>
   */
  public static <T extends Code, Enum> void assertValidCodes(Class<T> testedClass) {
    String className = testedClass.getSimpleName();
    try {
      Method fromCodeMethod = testedClass.getMethod("fromCode", String.class);
      List<CodeDto> expectedCodes = get(className + "Codes.json");
      for (CodeDto expectedCode : expectedCodes) {
        Optional<T> code = (Optional<T>) fromCodeMethod.invoke(null, expectedCode.getCode());
        assertTrue(code.isPresent(), "Expected code: \"" + expectedCode.getCode() + "\"");
        T actualCode = code.get();
        assertEquals(expectedCode.getCode(), actualCode.getCode());
        assertEquals(expectedCode.getDisplay(), actualCode.getDisplay());
      }
      Method valuesMethod = testedClass.getMethod("values", null);
      T[] values = (T[]) valuesMethod.invoke(null, null);
      assertEquals(expectedCodes.size(), values.length);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed automated testing of all valid codes of valueSet: " + className);
    }
  }

  public static <T extends Code> void assertValidCodes(
      List<CodeDto> expectedCodes, Function<String, Optional<T>> actualCodeSupplier) {
    for (CodeDto expectedCode : expectedCodes) {
      Optional<T> code = actualCodeSupplier.apply(expectedCode.getCode());
      assertTrue(code.isPresent(), "Expected code: \"" + expectedCode.getCode() + "\"");
      T actualCode = code.get();
      assertEquals(expectedCode.getCode(), actualCode.getCode());
      assertEquals(expectedCode.getDisplay(), actualCode.getDisplay());
    }
  }

  public static List<CodeDto> parseCodes(JSONArray jsonArray) {
    List<CodeDto> result = new ArrayList<>(jsonArray.length());
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject codeJson = jsonArray.getJSONObject(i);
      String codeValue = codeJson.getString("code");
      String displayValue = codeJson.getString("display");
      CodeDto code = new CodeDto(codeValue, displayValue);
      result.add(code);
    }
    return result;
  }

  public static List<CodeDto> get(String fileName) throws FileNotFoundException {
    JSONObject root = CodeUtil.parseFile(RESOURCES_PATH + fileName);
    JSONArray codes = root.getJSONArray("codes");
    return CodeUtil.parseCodes(codes);
  }

  public static JSONObject parseFile(String path) throws FileNotFoundException {
    FileReader fileReader = new FileReader(path);
    JSONTokener tokener = new JSONTokener(fileReader);
    return new JSONObject(tokener);
  }

  public static <T extends Code> void assertSimpleSystem(String expectedSystem, T[] codes) {
    for (T code : codes) {
      assertEquals(expectedSystem, code.getSystem(), "Code: \"" + code.getCode() + "\"");
    }
  }
}
