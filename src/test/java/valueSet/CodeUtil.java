package valueSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CodeUtil {
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

  public static JSONObject parseFile(String path) throws FileNotFoundException {
    FileReader fileReader = new FileReader(path);
    JSONTokener tokener = new JSONTokener(fileReader);
    return new JSONObject(tokener);
  }
}
