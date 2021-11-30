package mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OpsToAtcMapper {
  private OpsToAtcMapper() {}

  private static final Map<OpsCodeAndUnit, OpsToAtcMapping> mappings = generateMappings();

  public static Optional<OpsToAtcMapping> getMapping(String code, String unit) {
    OpsCodeAndUnit from = new OpsCodeAndUnit(code, unit);
    return Optional.ofNullable(mappings.get(from));
  }

  public static Map<OpsCodeAndUnit, OpsToAtcMapping> generateMappings() {
    var mappings = new HashMap<OpsCodeAndUnit, OpsToAtcMapping>();
    return mappings;
  }
}
