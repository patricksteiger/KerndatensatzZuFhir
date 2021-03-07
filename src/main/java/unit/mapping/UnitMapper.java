package unit.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unit.mapping.mapping.UnitMapping;
import unit.mapping.parse.MappingBean;
import unit.mapping.parse.ParseMappings;
import unit.ucum.Ucum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UnitMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitMapper.class);
  private static final boolean LOGGING_ACTIVATED = false;

  private static final Map<String, UnitMapping> mappings = generateMappings(ParseMappings.list());

  private UnitMapper() {}

  public static Optional<UnitMapping> getUcum(String unit) {
    // Automatically return UCUM-Units
    if (Ucum.validate(unit)) {
      return UnitMapping.fromUcumUnit(unit);
    }
    UnitMapping mapping = mappings.get(unit);
    return mapping != null ? Optional.of(mapping) : Optional.empty();
  }

  private static Map<String, UnitMapping> generateMappings(List<MappingBean> mappingsBeans) {
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
