package valueSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;

/** @see "https://simplifier.net/packages/hl7.fhir.uv.ips/1.0.0/files/244204/~json" */
public class DoseFormUvIpsCodes {

  private static final String FILE_PATH = "src/test/resources/valueSet/DoseFormUvIpsCodes.json";

  public static List<CodeDto> get() throws FileNotFoundException {
    JSONObject root = CodeUtil.parseFile(FILE_PATH);
    JSONArray codeDefinition = root.getJSONObject("compose").getJSONArray("include");
    JSONArray codes = codeDefinition.getJSONObject(0).getJSONArray("concept");
    return CodeUtil.parseCodes(codes);
  }
}
