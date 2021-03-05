package helper;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UnitConverter {
  private static final String MAPPINGS_PATH = "src/main/resources/ULM-LOINC_Mapping_Checklist.csv";
  private static final String UCUM_ESSENCE_PATH = "src/main/resources/ucum-essence.xml";

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitConverter.class);
  private static final boolean LOGGING_ACTIVATED = false;

  private static UnitConverter unitConverter = null;
  private static UcumEssenceService service = null;

  private Map<String, UnitMapping> mappings = null;

  private UnitConverter(Map<String, UnitMapping> mappings) {
    this.mappings = mappings;
  }

  public static Optional<UnitMapping> getUcum(String unit) throws UcumException, IOException {
    if (Helper.checkEmptyString(unit)) {
      return Optional.empty();
    }
    if (unitConverter == null) {
      initUnitConverter();
    }
    return unitConverter.getMapping(unit);
  }

  private static void initUnitConverter() throws IOException, UcumException {
    BufferedReader br =
        Files.newBufferedReader(Paths.get(MAPPINGS_PATH), StandardCharsets.ISO_8859_1);
    ColumnPositionMappingStrategy<MappingBean> headerStrategy =
        new ColumnPositionMappingStrategy<>();
    headerStrategy.setType(MappingBean.class);
    CsvToBean<MappingBean> beanParser =
        new CsvToBeanBuilder<MappingBean>(br)
            .withMappingStrategy(headerStrategy)
            .withSeparator(';')
            .withIgnoreEmptyLine(true)
            .build();
    List<MappingBean> mappingsBeans = beanParser.parse();
    br.close();
    Map<String, UnitMapping> mappings = generateMappings(mappingsBeans);
    unitConverter = new UnitConverter(mappings);
  }

  private static Map<String, UnitMapping> generateMappings(List<MappingBean> mappingsBeans)
      throws UcumException {
    Map<String, UnitMapping> mappings = new HashMap<>();
    for (int line = 1; line < mappingsBeans.size(); line++) {
      MappingBean bean = mappingsBeans.get(line);
      Optional<UnitMapping> mapping = UnitMapping.generateUnitMapping(bean);
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
    UnitMapping currentMapping = mappings.get(localUnit);
    if (currentMapping == null
        || currentMapping.getConversion().scale() > unitMapping.getConversion().scale()) {
      mappings.put(localUnit, unitMapping);
    }
  }

  private static void initUcumEssenceService() throws UcumException {
    service = new UcumEssenceService(UCUM_ESSENCE_PATH);
  }

  private static boolean isUcumUnit(String unit) throws UcumException {
    if (service == null) {
      initUcumEssenceService();
    }
    return service.validate(unit) == null;
  }

  private Optional<UnitMapping> getMapping(String unit) {
    UnitMapping mapping = mappings.get(unit);
    return mapping != null ? Optional.of(mapping) : Optional.empty();
  }

  public static class UnitMapping {
    private final String ucumUnit;
    private final String ucumCode;
    private final BigDecimal conversion;

    private UnitMapping(String ucumUnit, String ucumCode, BigDecimal conversion) {
      this.ucumUnit = ucumUnit;
      this.ucumCode = ucumCode;
      this.conversion = conversion;
    }

    public static Optional<UnitMapping> generateUnitMapping(MappingBean mappingBean)
        throws UcumException {
      String localUnit = mappingBean.getLocalUnit();
      if (Helper.checkEmptyString(localUnit)) {
        return Optional.empty();
      }
      if (isUcumUnit(localUnit)) {
        UnitMapping unitMapping =
            new UnitMapping(localUnit, service.analyse(localUnit), BigDecimal.ONE);
        return Optional.of(unitMapping);
      }
      String ucumUnit = mappingBean.getUcumUnit();
      if (Helper.checkEmptyString(ucumUnit) || !isUcumUnit(ucumUnit)) {
        return Optional.empty();
      }
      Optional<BigDecimal> conversion = parseConversion(mappingBean.getConversion());
      if (!conversion.isPresent()) {
        return Optional.empty();
      }
      UnitMapping unitMapping =
          new UnitMapping(ucumUnit, service.analyse(ucumUnit), conversion.get());
      return Optional.of(unitMapping);
    }

    private static Optional<BigDecimal> parseConversion(String conversion) {
      if (Helper.checkEmptyString(conversion) || !conversion.startsWith("x*")) {
        return Optional.empty();
      }
      try {
        String number = conversion.substring(2);
        BigDecimal result = new BigDecimal(number);
        return Optional.of(result);
      } catch (Exception e) {
        return Optional.empty();
      }
    }

    public String getUcumUnit() {
      return ucumUnit;
    }

    public String getUcumCode() {
      return ucumCode;
    }

    public BigDecimal getConversion() {
      return conversion;
    }
  }

  public static class MappingBean {
    @CsvBindByPosition(position = 0)
    private String loincCode;

    @CsvBindByPosition(position = 1)
    private String loincLabel;

    @CsvBindByPosition(position = 2)
    private String relation;

    @CsvBindByPosition(position = 3)
    private String localCode;

    @CsvBindByPosition(position = 4)
    private String localLabel;

    @CsvBindByPosition(position = 5)
    private String ucumUnit;

    @CsvBindByPosition(position = 6)
    private String conversion;

    @CsvBindByPosition(position = 7)
    private String localUnit;

    @CsvBindByPosition(position = 8)
    private String molecularWeight;

    public String getLoincCode() {
      return loincCode;
    }

    public String getLoincLabel() {
      return loincLabel;
    }

    public String getRelation() {
      return relation;
    }

    public String getLocalCode() {
      return localCode;
    }

    public String getLocalLabel() {
      return localLabel;
    }

    public String getUcumUnit() {
      return ucumUnit;
    }

    public String getConversion() {
      return conversion;
    }

    public String getLocalUnit() {
      return localUnit;
    }

    public String getMolecularWeight() {
      return molecularWeight;
    }
  }
}
