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

  // TODO: How to react to unity-Unit? Currently doesn't set a value in Quantity!
  public static Optional<Quantity> fromLocalUnit(String localUnit, String value) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    // No unit is represented with "1".
    String unit = Helper.checkEmptyString(localUnit) ? "1" : localUnit;
    return UnitMapper.getUcum(unit)
        .flatMap(mapping -> quantityFromUnitMappingAndValue(mapping, value));
  }

  private static Optional<Quantity> quantityFromUnitMappingAndValue(
      UnitMapping mapping, String value) {
    if (UnitReducible.fromString(mapping.getUcumCode())) {
      return convertValue(value, mapping.getConversion()).flatMap(val -> quantity(val, mapping));
    } else {
      return quantity(value, mapping);
    }
  }

  private static Optional<BigDecimal> convertValue(String value, BigDecimal conversion) {
    String[] formula = value.split(FRACTION);
    if (formula.length == 1) {
      return simpleValueConversion(value, conversion);
    } else if (formula.length == 2) {
      return fractionConversion(formula[0], formula[1], conversion);
    } else {
      return Optional.empty();
    }
  }

  private static Optional<BigDecimal> fractionConversion(
      String numerator, String denominator, BigDecimal conversion) {
    Optional<BigDecimal> numeratorOptional = Helper.parseValue(numerator);
    Optional<BigDecimal> denominatorOptional = Helper.parseValue(denominator);
    if (!numeratorOptional.isPresent() || !denominatorOptional.isPresent()) {
      return Optional.empty();
    }
    BigDecimal num = numeratorOptional.get();
    BigDecimal den = denominatorOptional.get();
    return safeDiv(num, den).map(x -> x.multiply(conversion));
  }

  private static Optional<BigDecimal> safeDiv(BigDecimal numerator, BigDecimal denominator) {
    try {
      return Optional.of(
          numerator.divide(denominator, BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private static Optional<BigDecimal> simpleValueConversion(String value, BigDecimal conversion) {
    Optional<BigDecimal> number = Helper.parseValue(value);
    return number.map(n -> n.multiply(conversion));
  }

  private static Optional<Quantity> quantity(BigDecimal value, UnitMapping mapping) {
    String code = mapping.getUcumCode();
    return Ucum.formalRepresentation(code)
        .map(unit -> FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, code));
  }

  private static Optional<Quantity> quantity(String value, UnitMapping mapping) {
    String code = mapping.getUcumCode();
    return Ucum.formalRepresentation(code)
        .map(unit -> FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, code));
  }
}
