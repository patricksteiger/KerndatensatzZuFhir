package valueSet;

import interfaces.Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class CodeUtil {

  public static final String RESOURCES_PATH = "src/test/resources/valueSet/";

  /**
   * Asserts all valid codes containing in file codes to be correctly parsed from fromCode-Function
   * and the size of all actual values is the same as expected values from file codes.
   *
   * <p>The file codes should have a field "codes" at the root level with all valid codes as an
   * element in the array.
   *
   * @param values
   * @param fromCode
   * @param codes
   * @param <T>
   */
  public static <T extends Code> void assertValidCodes(
      T[] values, Function<String, Optional<T>> fromCode, File codes) {
    assertTrue(codes.canRead(), "Couldn't read file: " + codes.getName());
    List<CodeDto> expectedCodes = null;
    try {
      expectedCodes = get(codes);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Failed automated testing of all valid codes of valueSet: " + codes.getName());
    }
    for (CodeDto expectedCode : expectedCodes) {
      Optional<T> code = fromCode.apply(expectedCode.getCode());
      assertTrue(code.isPresent(), "Expected code: \"" + expectedCode.getCode() + "\"");
      T actualCode = code.get();
      assertEquals(expectedCode.getCode(), actualCode.getCode());
      assertEquals(expectedCode.getDisplay(), actualCode.getDisplay());
    }
    assertEquals(expectedCodes.size(), values.length);
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

  public static List<CodeDto> get(File file) throws FileNotFoundException {
    JSONObject root = CodeUtil.parseFile(file);
    JSONArray codes = root.getJSONArray("codes");
    return CodeUtil.parseCodes(codes);
  }

  public static JSONObject parseFile(File file) throws FileNotFoundException {
    FileReader fileReader = new FileReader(file);
    JSONTokener tokener = new JSONTokener(fileReader);
    return new JSONObject(tokener);
  }

  public static File getResourcePrefixFile(String fileName) {
    return new File(RESOURCES_PATH + fileName);
  }

  public static <T extends Code> void assertSimpleSystem(String expectedSystem, T[] codes) {
    for (T code : codes) {
      assertEquals(expectedSystem, code.getSystem(), "Code: \"" + code.getCode() + "\"");
    }
  }
}
