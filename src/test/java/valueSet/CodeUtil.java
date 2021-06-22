package valueSet;

import interfaces.Code;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeUtil {

  public static final String RESOURCES_PATH = "src/test/resources/valueSet/";

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
