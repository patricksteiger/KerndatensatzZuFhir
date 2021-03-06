package converter;

import converter.mapping.UnitMapping;
import converter.parse.MappingBean;
import converter.parse.ParseMappings;
import org.fhir.ucum.UcumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UnitConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitConverter.class);
  private static final boolean LOGGING_ACTIVATED = false;

  private static Map<String, UnitMapping> mappings;

  static {
    try {
      mappings = generateMappings(ParseMappings.list());
    } catch (UcumException e) {
      e.printStackTrace();
    }
  }

  private UnitConverter() {}

  public static Optional<UnitMapping> getUcum(String unit) {
    // Automatically return UCUM-Units
    if (Ucum.validate(unit)) {
      return UnitMapping.fromUcumUnit(unit);
    }
    UnitMapping mapping = mappings.get(unit);
    return mapping != null ? Optional.of(mapping) : Optional.empty();
  }

  private static Map<String, UnitMapping> generateMappings(List<MappingBean> mappingsBeans)
      throws UcumException {
    Map<String, UnitMapping> mappings = new HashMap<>();
    // Start at 1 to skip header
    for (int line = 1; line < mappingsBeans.size(); line++) {
      MappingBean bean = mappingsBeans.get(line);
      Optional<UnitMapping> mapping = UnitMapping.fromBean(bean);
      if (mapping.isPresent()) {
        addMapping(mappings, bean.getLocalUnit(), mapping.get());
      } else if (LOGGING_ACTIVATED) {
        LOGGER.warn(
            "Couldn't generate mapping for unit on line {}. Local = \"{}\", Ucum = \"{}\", Conversion = \"{}\"",
            line + 1,
            bean.getLocalUnit(),
            bean.getUcumUnit(),
            bean.getConversion());
      }
    }
    return mappings;
  }

  private static void addMapping(
      Map<String, UnitMapping> mappings, String localUnit, UnitMapping unitMapping) {
    // UCUM-Units are automatically returned in getUcum and don't need a mapping
    if (Ucum.validate(localUnit)) {
      return;
    }
    UnitMapping currentMapping = mappings.get(localUnit);
    if (currentMapping == null
        || currentMapping.getConversion().scale() > unitMapping.getConversion().scale()) {
      mappings.put(localUnit, unitMapping);
    }
  }
}
