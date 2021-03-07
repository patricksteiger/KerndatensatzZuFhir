package unit.converter;

import constants.Constants;
import helper.FhirGenerator;
import helper.Helper;
import org.hl7.fhir.r4.model.Quantity;
import unit.mapping.UnitMapper;
import unit.mapping.mapping.UnitMapping;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

public class UnitConverter {
  public static final int BIG_DECIMAL_SCALE = 20;
  public static final int BIG_DECIMAL_ROUNDING_MODE = BigDecimal.ROUND_HALF_DOWN;
  private static final String FRACTION = "/";

  private UnitConverter() {}

  public static Optional<Quantity> fromLocalCode(String localCode, String value) {
    Optional<UnitMapping> mappingOptional = UnitMapper.getUcum(localCode);
    if (!mappingOptional.isPresent()) {
      return Optional.empty();
    }
    UnitMapping mapping = mappingOptional.get();
    Optional<String> valueStr = convertValue(value, mapping.getConversion());
    return valueStr.flatMap(val -> quantity(val, mapping));
  }

  private static Optional<String> convertValue(String value, BigDecimal conversion) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    // TODO: Check if value is allowed to be reduced (Blutdruck, ...)
    String[] formula = value.split(FRACTION);
    if (formula.length == 1) {
      return simpleValueConversion(value, conversion);
    } else if (formula.length == 2) {
      return fractionConversion(formula[0], formula[1], conversion);
    } else {
      return Optional.empty();
    }
  }

  private static Optional<String> fractionConversion(
      String numerator, String denominator, BigDecimal conversion) {
    Optional<BigDecimal> numeratorOptional = Helper.parseValue(numerator);
    Optional<BigDecimal> denominatorOptional = Helper.parseValue(denominator);
    if (!numeratorOptional.isPresent() || !denominatorOptional.isPresent()) {
      return Optional.empty();
    }
    BigDecimal num = numeratorOptional.get();
    BigDecimal den = denominatorOptional.get();
    return safeDiv(num, den).map(x -> x.multiply(conversion)).map(BigDecimal::toString);
  }

  private static Optional<BigDecimal> safeDiv(BigDecimal numerator, BigDecimal denominator) {
    try {
      return Optional.of(
          numerator.divide(denominator, BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private static Optional<String> simpleValueConversion(String value, BigDecimal conversion) {
    Optional<BigDecimal> number = Helper.parseValue(value);
    return number.map(n -> n.multiply(conversion).toString());
  }

  private static Optional<Quantity> quantity(String value, UnitMapping mapping) {
    String code = mapping.getUcumCode();
    Optional<String> unitOptional = Ucum.formalRepresentation(code);
    return unitOptional.map(
        unit -> FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, code));
  }
}
