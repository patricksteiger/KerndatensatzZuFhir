package converter.parse;

import com.opencsv.bean.CsvBindByPosition;

public class MappingBean {
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
