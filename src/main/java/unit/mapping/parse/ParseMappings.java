package unit.mapping.parse;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ParseMappings {
  private static final String MAPPINGS_PATH = "src/main/resources/ULM-LOINC_Mapping_Checklist.csv";
  private static final char SEPARATOR = ';';
  private static final List<MappingBean> mappings;

  static {
    try {
      mappings = parseMappings();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Couldn't parse Mapping Checklist!");
    }
  }

  private ParseMappings() {}

  public static List<MappingBean> list() {
    return mappings;
  }

  // TODO: Charset needs to be set manually. Correct one?
  private static List<MappingBean> parseMappings() throws IOException {
    BufferedReader br =
        Files.newBufferedReader(Paths.get(MAPPINGS_PATH), StandardCharsets.ISO_8859_1);
    ColumnPositionMappingStrategy<MappingBean> headerStrategy =
        new ColumnPositionMappingStrategy<>();
    headerStrategy.setType(MappingBean.class);
    CsvToBean<MappingBean> beanParser =
        new CsvToBeanBuilder<MappingBean>(br)
            .withMappingStrategy(headerStrategy)
            .withSeparator(SEPARATOR)
            .withIgnoreEmptyLine(true)
            .build();
    List<MappingBean> mappingsBeans = beanParser.parse();
    br.close();
    return mappingsBeans;
  }
}
