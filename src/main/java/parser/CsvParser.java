package parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import interfaces.Datablock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvParser {
  public static final char COLUMN_SEPARATOR = '$';

  /**
   * Parses Datablocks from given CSV-File. Format of CSV-File should be equal to the schema defined
   * with OpenCSV-Annotations in <code>datablockClass</code>.
   *
   * @param csvFilePath Path to CSV-File.
   * @param datablockClass Class containing the schema to parse.
   * @param  <T> The type needs to be a Datablock.
   * @return List of parsed Datablocks
   * @throws IOException In case of error while working with the file.
   * @throws IllegalStateException In case of error while parsing CSV.
   */
  public static <T extends Datablock> List<T> parseDatablocks(
      String csvFilePath, Class<T> datablockClass) throws IOException {
    BufferedReader br =
        new BufferedReader(new FileReader(csvFilePath, StandardCharsets.ISO_8859_1));
    HeaderColumnNameMappingStrategy<T> headerStrategy = new HeaderColumnNameMappingStrategy<>();
    headerStrategy.setType(datablockClass);
    CsvToBean<T> beanParser =
        new CsvToBeanBuilder<T>(br)
            .withMappingStrategy(headerStrategy)
            .withSeparator(COLUMN_SEPARATOR)
            .withIgnoreEmptyLine(true)
            .build();
    List<T> datablocks = beanParser.parse();
    br.close();
    return datablocks;
  }
}
