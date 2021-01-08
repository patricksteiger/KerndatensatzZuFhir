package testModel;

import com.opencsv.bean.CsvBindByName;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public class DatablockMock implements Datablock {
  @CsvBindByName private String col1;

  @CsvBindByName private String col2;

  public String getCol1() {
    return col1;
  }

  public String getCol2() {
    return col2;
  }

  @Override
  public List<Resource> toFhirResources() {
    return null;
  }
}
