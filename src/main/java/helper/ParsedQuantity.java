package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ParsedQuantity {
  private final String value;
  private final String unit;
  private final Optional<BigDecimal> decimalValue;
  private final String ucum;

  private ParsedQuantity(
      String value, String unit, Optional<BigDecimal> decimalValue, String ucum) {
    this.value = value;
    this.unit = unit;
    this.decimalValue = decimalValue;
    this.ucum = ucum;
  }

  public static ParsedQuantity fromString(String s) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCode(splitCode, "value=");
    String unit = Helper.extractCode(splitCode, "unit=");
    Optional<BigDecimal> decimalValue = Helper.parseValue(value);
    // TODO: UCUM Conversion
    return new ParsedQuantity(value, unit, decimalValue, unit);
  }

  public Optional<Quantity> toQuantity() {
    if (!decimalValue.isPresent() || Helper.checkAnyEmptyString(unit, ucum)) {
      return Optional.empty();
    }
    return Optional.of(
        FhirGenerator.quantity(decimalValue.get(), unit, Constants.QUANTITY_SYSTEM, ucum));
  }

  public Optional<BigDecimal> getDecimalValue() {
    return decimalValue;
  }

  public String getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }

  public String getUcum() {
    return ucum;
  }

  public String getSystem() {
    return Constants.QUANTITY_SYSTEM;
  }
}
