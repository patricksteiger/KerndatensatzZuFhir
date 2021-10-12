package helper;

import org.hl7.fhir.r4.model.Quantity;
import unit.converter.UnitConverter;

import java.util.List;
import java.util.Optional;

public class ParsedQuantity {

  private ParsedQuantity() {}

  public static Optional<Quantity> fromString(String s) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCode(splitCode, "value=");
    String unit = Helper.extractCode(splitCode, "unit=");
    return UnitConverter.fromLocalUnit(unit, value);
  }

  public static Optional<Quantity> fromStringWithLocalCode(String s, String localCode) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCode(splitCode, "value=");
    String unit = Helper.extractCode(splitCode, "unit=");
    String code = ParsedCode.fromString(localCode).getCode();
    return UnitConverter.fromLocalCodeAndUnit(code, unit, value);
  }
}
