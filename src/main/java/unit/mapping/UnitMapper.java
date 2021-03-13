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

  /**
   * Returns UnitMapping corresponding to given unit. If unit is already in UCUM-Format a conversion
   * of UnitMapping is BigDecimal.ONE. Otherwise returns mapping according to ParseMappings.
   *
   * @param unit unit which UCUM-Format is needed.
   * @return UCUM-Format mapping with conversion, or empty if unit is not in UCUM-Format and ha sno
   *     mapping.
   */
  public static Optional<UnitMapping> getUcum(String unit) {
    // Automatically return UCUM-Units
    Optional<UnitMapping> mapping = UnitMapping.fromUcumUnit(unit);
    if (!mapping.isPresent()) {
      mapping = Optional.ofNullable(mappings.get(unit));
    }
    return mapping;
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
        logFailedMapping(bean, line + 1);
      }
    }
    return mappings;
  }

  private static void logFailedMapping(MappingBean bean, int line) {
    LOGGER.warn(
        "Couldn't generate mapping for unit on line {}. Local = \"{}\", Ucum = \"{}\", Conversion = \"{}\"",
        line + 1,
        bean.getLocalUnit(),
        bean.getUcumUnit(),
        bean.getConversion());
  }

  private static void addMapping(
      Map<String, UnitMapping> mappings, String localUnit, UnitMapping unitMapping) {
    // UCUM-Units are automatically returned in getUcum and don't need a mapping
    if (Ucum.validate(localUnit)) {
      return;
    }
    UnitMapping currentMapping = mappings.get(localUnit);
    // Make sure to save shortest mapping
    if (currentMapping == null
        || currentMapping.getConversion().scale() > unitMapping.getConversion().scale()) {
      mappings.put(localUnit, unitMapping);
    }
  }
}
