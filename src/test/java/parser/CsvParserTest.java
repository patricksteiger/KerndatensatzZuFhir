package parser;

import basismodule.Prozedur;
import org.junit.jupiter.api.Test;
import testModel.DatablockMock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvParserTest {
  private static final String RESOURCES_PATH = "src/test/resources/parser/";
  private static final String EMPTY_FILE_PATH = RESOURCES_PATH + "emptyFile.csv";
  private static final String MISSING_HEADER_FILE_PATH = RESOURCES_PATH + "missingHeader.csv";
  private static final String WRONG_SEPARATOR_FILE_PATH = RESOURCES_PATH + "wrongSeparator.csv";
  private static final String INVALID_SYNTAX_FILE_PATH = RESOURCES_PATH + "invalidSyntax.csv";
  private static final String VALID_DATABLOCKS_FILE_PATH = RESOURCES_PATH + "validDatablocks.csv";
  private static final String DOUBLE_COLUMN_NAME_FILE_PATH = RESOURCES_PATH + "doubleName.csv";
  private static final String SWITCHED_COLUMN_NAMES_FILE_PATH =
      RESOURCES_PATH + "switchedNames.csv";
  private static final String EMPTY_CELL_FILE_PATH = RESOURCES_PATH + "emptyCell.csv";
  private static final String IGNORE_EMPTY_LINES_FILE_PATH =
      RESOURCES_PATH + "ignoreEmptyLines.csv";

  private static final String COLUMN_1_NAME = "col1";
  private static final String COLUMN_2_NAME = "col2";

  @Test
  public void exceptionOnNonExistentFile() {
    assertThrows(
        IOException.class,
        () -> {
          CsvParser.parseDatablocks("src/test/noFile.csv", Prozedur.class);
        });
  }

  @Test
  public void emptyListOnEmptyFile() throws IOException {
    FileWriter fileWriter = new FileWriter(EMPTY_FILE_PATH);
    fileWriter.close();
    List<DatablockMock> result = CsvParser.parseDatablocks(EMPTY_FILE_PATH, DatablockMock.class);
    assertEquals(0, result.size());
  }

  @Test
  public void exceptionOnMissingHeader() throws IOException {
    FileWriter fileWriter = new FileWriter(MISSING_HEADER_FILE_PATH);
    fileWriter.write("\n");
    fileWriter.write("1" + CsvParser.COLUMN_SEPARATOR + "2\n");
    fileWriter.close();
    assertThrows(
        RuntimeException.class,
        () -> {
          CsvParser.parseDatablocks(MISSING_HEADER_FILE_PATH, DatablockMock.class);
        });
  }

  @Test
  public void variablesNullOnWrongSeparator() throws IOException {
    FileWriter fileWriter = new FileWriter(WRONG_SEPARATOR_FILE_PATH);
    char wrongSeparator = (char) (CsvParser.COLUMN_SEPARATOR + 1);
    fileWriter.write(COLUMN_1_NAME + wrongSeparator + COLUMN_2_NAME + "\n");
    fileWriter.write("1" + wrongSeparator + "2\n");
    fileWriter.close();
    // No wrong syntax, but Parser can't parse values and treats all columns as missing.
    // Object gets instantiated, but values are set to null.
    List<DatablockMock> result =
        CsvParser.parseDatablocks(WRONG_SEPARATOR_FILE_PATH, DatablockMock.class);
    assertEquals(1, result.size());
    assertNull(result.get(0).getCol1());
    assertNull(result.get(0).getCol2());
  }

  @Test
  public void exceptionOnInvalidCsvSyntax() throws IOException {
    FileWriter fileWriter = new FileWriter(INVALID_SYNTAX_FILE_PATH);
    fileWriter.write(COLUMN_1_NAME + "\n");
    fileWriter.write("1" + CsvParser.COLUMN_SEPARATOR + "2\n");
    fileWriter.close();
    assertThrows(
        RuntimeException.class,
        () -> {
          CsvParser.parseDatablocks(INVALID_SYNTAX_FILE_PATH, DatablockMock.class);
        });
  }

  @Test
  public void parseMultipleDatablocks() throws IOException {
    FileWriter fileWriter = new FileWriter(VALID_DATABLOCKS_FILE_PATH);
    fileWriter.write(COLUMN_1_NAME + CsvParser.COLUMN_SEPARATOR + COLUMN_2_NAME + "\n");
    fileWriter.write("1.1" + CsvParser.COLUMN_SEPARATOR + "1.2\n");
    fileWriter.write("2.1" + CsvParser.COLUMN_SEPARATOR + "2.2\n");
    fileWriter.close();
    List<DatablockMock> result =
        CsvParser.parseDatablocks(VALID_DATABLOCKS_FILE_PATH, DatablockMock.class);
    assertEquals(2, result.size());
    DatablockMock fstLine = result.get(0);
    assertEquals("1.1", fstLine.getCol1());
    assertEquals("1.2", fstLine.getCol2());
    DatablockMock sndLine = result.get(1);
    assertEquals("2.1", sndLine.getCol1());
    assertEquals("2.2", sndLine.getCol2());
  }

  @Test
  public void doubleColumnNamesOverwrite() throws IOException {
    FileWriter fileWriter = new FileWriter(DOUBLE_COLUMN_NAME_FILE_PATH);
    fileWriter.write(COLUMN_1_NAME + CsvParser.COLUMN_SEPARATOR + COLUMN_1_NAME + "\n");
    fileWriter.write("1" + CsvParser.COLUMN_SEPARATOR + "2\n");
    fileWriter.close();
    // The second value overwrites the first, if there are multiple matching column names.
    List<DatablockMock> result =
        CsvParser.parseDatablocks(DOUBLE_COLUMN_NAME_FILE_PATH, DatablockMock.class);
    assertEquals(1, result.size());
    assertEquals("2", result.get(0).getCol1());
    assertNull(result.get(0).getCol2());
  }

  @Test
  public void switchedColumnNames() throws IOException {
    FileWriter fileWriter = new FileWriter(SWITCHED_COLUMN_NAMES_FILE_PATH);
    fileWriter.write(COLUMN_2_NAME + CsvParser.COLUMN_SEPARATOR + COLUMN_1_NAME + "\n");
    fileWriter.write("2" + CsvParser.COLUMN_SEPARATOR + "1\n");
    fileWriter.close();
    List<DatablockMock> result =
        CsvParser.parseDatablocks(SWITCHED_COLUMN_NAMES_FILE_PATH, DatablockMock.class);
    assertEquals(1, result.size());
    assertEquals("1", result.get(0).getCol1());
    assertEquals("2", result.get(0).getCol2());
  }

  @Test
  public void emptyCellsLeadToEmptyValues() throws IOException {
    FileWriter fileWriter = new FileWriter(EMPTY_CELL_FILE_PATH);
    fileWriter.write(COLUMN_1_NAME + CsvParser.COLUMN_SEPARATOR + COLUMN_2_NAME + "\n");
    fileWriter.write("" + CsvParser.COLUMN_SEPARATOR + "2\n");
    fileWriter.close();
    List<DatablockMock> result =
        CsvParser.parseDatablocks(EMPTY_CELL_FILE_PATH, DatablockMock.class);
    assertEquals(1, result.size());
    assertEquals("", result.get(0).getCol1());
    assertEquals("2", result.get(0).getCol2());
  }

  @Test
  public void ignoreEmptyLines() throws IOException {
    FileWriter fileWriter = new FileWriter(IGNORE_EMPTY_LINES_FILE_PATH);
    fileWriter.write(COLUMN_1_NAME + CsvParser.COLUMN_SEPARATOR + COLUMN_2_NAME + "\n");
    fileWriter.write("\n");
    fileWriter.write("1" + CsvParser.COLUMN_SEPARATOR + "2\n");
    fileWriter.write("\n");
    fileWriter.close();
    List<DatablockMock> result =
        CsvParser.parseDatablocks(IGNORE_EMPTY_LINES_FILE_PATH, DatablockMock.class);
    assertEquals(1, result.size());
    assertEquals("1", result.get(0).getCol1());
    assertEquals("2", result.get(0).getCol2());
  }
}
