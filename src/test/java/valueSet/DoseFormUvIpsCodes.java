package valueSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/** @see "https://simplifier.net/packages/hl7.fhir.uv.ips/1.0.0/files/244204/~json" */
public class DoseFormUvIpsCodes {

  private static final String FILE_PATH = "src/test/resources/valueSet/DoseFormUvIpsCodes.json";

  public static List<CodeDto> get() throws FileNotFoundException {
    FileReader reader = new FileReader(FILE_PATH);
    JSONTokener file = new JSONTokener(reader);
    JSONObject root = new JSONObject(file);
    JSONArray codeDefinition = root.getJSONObject("compose").getJSONArray("include");
    JSONArray codes = codeDefinition.getJSONObject(0).getJSONArray("concept");
    List<CodeDto> result = new ArrayList<>(codes.length());
    for (int i = 0; i < codes.length(); i++) {
      JSONObject codeJson = codes.getJSONObject(i);
      CodeDto code = new CodeDto(codeJson.getString("code"), codeJson.getString("display"));
      result.add(code);
    }
    return result;
  }
}
