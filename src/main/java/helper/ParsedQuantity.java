package helper;

import org.hl7.fhir.r4.model.Quantity;
import unit.converter.UnitConverter;

import java.util.Optional;

public class ParsedQuantity {

  private ParsedQuantity() {}

  public static Optional<Quantity> fromString(String s) {
    var parsedValue = ParsedValueAndUnit.fromString(s);
    return UnitConverter.fromLocalUnit(parsedValue.getUnit(), parsedValue.getValue());
  }

  public static Optional<Quantity> fromStringWithLocalCode(String s, String localCode) {
    var parsedValue = ParsedValueAndUnit.fromString(s);
    String code = ParsedCode.fromString(localCode).getCode();
    return UnitConverter.fromLocalCodeAndUnit(code, parsedValue.getUnit(), parsedValue.getValue());
  }
}
