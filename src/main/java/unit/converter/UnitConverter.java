package unit.converter;

import constants.Constants;
import helper.FhirGenerator;
import helper.Helper;
import org.hl7.fhir.r4.model.Quantity;
import unit.mapping.Mapper;
import unit.mapping.UnitMapping;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

public class UnitConverter {
  private static final String FRACTION = "/";

  private UnitConverter() {}

  // TODO: How to react to unity-Unit? Currently doesn't set a value in Quantity!
  public static Optional<Quantity> fromLocalUnit(String localUnit, String value) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    // No unit is represented with "1".
    String unit = Helper.checkEmptyString(localUnit) ? "1" : localUnit;
    return Mapper.getUcumMappingFromLocalUnit(unit)
        .flatMap(mapping -> quantityFromUnitMappingAndValue(mapping, value));
  }

  private static Optional<Quantity> quantityFromUnitMappingAndValue(
      UnitMapping mapping, String value) {
    String ucumCode = mapping.getUcumCode();
    return NonReducibleUcumUnit.check(ucumCode)
        ? quantity(value, mapping.getUcumCode())
        : convertValue(value, mapping.getConversion()).flatMap(val -> quantity(val, ucumCode));
  }

  private static Optional<BigDecimal> convertValue(String value, BigDecimal conversion) {
    String[] formula = value.split(FRACTION);
    switch (formula.length) {
      case 1:
        return simpleValueConversion(value, conversion);
      case 2:
        return fractionConversion(formula[0], formula[1], conversion);
      default:
        return Optional.empty();
    }
  }

  private static Optional<BigDecimal> fractionConversion(
      String numerator, String denominator, BigDecimal conversion) {
    return Helper.fraction(numerator, denominator).map(conversion::multiply);
  }

  private static Optional<BigDecimal> simpleValueConversion(String value, BigDecimal conversion) {
    return Helper.maybeBigDecimal(value).map(conversion::multiply);
  }

  private static Optional<Quantity> quantity(BigDecimal value, String ucumCode) {
    return Ucum.formalRepresentation(ucumCode)
        .map(unit -> FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, ucumCode));
  }

  private static Optional<Quantity> quantity(String value, String ucumCode) {
    return Ucum.formalRepresentation(ucumCode)
        .map(unit -> FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, ucumCode));
  }
}
