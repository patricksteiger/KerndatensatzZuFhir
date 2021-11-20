package mapping;

import helper.AnyCode;
import helper.FhirGenerator;
import helper.Helper;
import interfaces.Code;
import org.hl7.fhir.r4.model.Coding;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OpsToAtc {
  private static final Map<OpsCodeAndUnit, OpsToAtcMapping> mappings = generateMappings();

  private OpsToAtc() {}

  public static Optional<Coding> from(Code parsedCode, String unit) {
    if (Helper.checkEmptyString(parsedCode.getCode())) {
      return Optional.empty();
    }
    return getAtcMapping(parsedCode.getCode(), unit)
        .map(AnyCode::from)
        .or(() -> Optional.of(parsedCode))
        .map(FhirGenerator::coding);
  }

  public static Optional<OpsToAtcMapping> getAtcMapping(String code, String unit) {
    OpsCodeAndUnit from = new OpsCodeAndUnit(code, unit);
    return Optional.ofNullable(mappings.get(from));
  }

  public static Map<OpsCodeAndUnit, OpsToAtcMapping> generateMappings() {
    var mappings = new HashMap<OpsCodeAndUnit, OpsToAtcMapping>();
    return mappings;
  }
}
