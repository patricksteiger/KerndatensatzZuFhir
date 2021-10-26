package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import unit.converter.IrreducibleUnit;
import unit.converter.UnitConverter;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

public class ParsedQuantity {

  private ParsedQuantity() {}

  public static Optional<Quantity> fromString(String s) {
    var parsedValue = ParsedValueAndUnit.fromString(s);
    return valueAndUnitToQuantity(parsedValue);
  }

  private static Optional<Quantity> valueAndUnitToQuantity(ParsedValueAndUnit valueAndUnit) {
    return IrreducibleUnit.check(valueAndUnit.getUnit())
        ? ucumOrLocalQuantity(valueAndUnit.getValue(), valueAndUnit.getUnit())
        : Helper.parseBigDecimalFromQuantity(valueAndUnit.getValue())
            .map(value -> ucumOrLocalQuantity(value, valueAndUnit.getUnit()));
  }

  private static Optional<Quantity> ucumOrLocalQuantity(String value, String unit) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    Optional<String> optionalDisplay = Ucum.formalRepresentation(unit);
    String system;
    String display;
    if (optionalDisplay.isPresent()) {
      system = Constants.QUANTITY_SYSTEM;
      display = optionalDisplay.get();
    } else {
      system = "";
      display = unit;
    }
    return Optional.of(FhirGenerator.quantity(value, display, system, unit));
  }

  private static Quantity ucumOrLocalQuantity(BigDecimal value, String unit) {
    Optional<String> optionalDisplay = Ucum.formalRepresentation(unit);
    String system;
    String display;
    if (optionalDisplay.isPresent()) {
      system = Constants.QUANTITY_SYSTEM;
      display = optionalDisplay.get();
    } else {
      system = "";
      display = unit;
    }
    return FhirGenerator.quantity(value, display, system, unit);
  }

  public static Optional<Quantity> fromStringWithLocalCode(String s, String localCode) {
    var parsedValue = ParsedValueAndUnit.fromString(s);
    String code = ParsedCode.fromString(localCode).getCode();
    return UnitConverter.fromLocalCodeAndUnit(code, parsedValue);
  }
}
