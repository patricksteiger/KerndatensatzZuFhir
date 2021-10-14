package unit.converter;

import helper.FhirGenerator;
import helper.Helper;
import org.hl7.fhir.r4.model.Quantity;
import unit.mapping.LoincMapping;
import unit.mapping.Mapper;
import unit.mapping.UnitMapping;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

import static constants.Constants.QUANTITY_SYSTEM;

public class UnitConverter {

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

  // TODO: can a empty value ever be valid? -> empty conversions of loinc codes!
  public static Optional<Quantity> fromLocalCodeAndUnit(
      String localCode, String localUnit, String value) {
    Optional<LoincMapping> loincMapping =
        Mapper.getLoincMappingFromLocalCodeAndUnit(localCode, localUnit);
    return loincMapping.isPresent()
        ? quantityFromLoincMappingAndValue(loincMapping.get(), value)
        : quantityFromLocalUnitAndValue(localUnit, value);
  }

  // TODO: Reduce possible fractions
  private static Optional<Quantity> quantityFromLocalUnitAndValue(String localUnit, String value) {
    String system = Ucum.validate(localUnit) ? QUANTITY_SYSTEM : "";
    return quantityFromValueUnitConversionWithSystem(value, localUnit, BigDecimal.ONE, system);
  }

  private static Optional<Quantity> quantityFromLoincMappingAndValue(
      LoincMapping loincMapping, String value) {
    String unit = loincMapping.getUcumCode();
    BigDecimal conversion = loincMapping.getConversion();
    return quantityFromValueUnitConversionWithSystem(value, unit, conversion, QUANTITY_SYSTEM);
  }

  private static Optional<Quantity> quantityFromUnitMappingAndValue(
      UnitMapping mapping, String value) {
    String unit = mapping.getUcumCode();
    BigDecimal conversion = mapping.getConversion();
    return quantityFromValueUnitConversionWithSystem(value, unit, conversion, QUANTITY_SYSTEM);
  }

  private static Optional<Quantity> quantityFromValueUnitConversionWithSystem(
      String value, String unit, BigDecimal conversion, String system) {
    return IrreducibleUnit.check(unit)
        ? quantity(value, unit, system)
        : convertValue(value, conversion).flatMap(val -> quantity(val, unit, system));
  }

  private static Optional<BigDecimal> convertValue(String value, BigDecimal conversion) {
    return Helper.parseBigDecimalFromQuantity(value).map(conversion::multiply);
  }

  private static Optional<Quantity> quantity(BigDecimal value, String unit, String system) {
    return QUANTITY_SYSTEM.equals(system)
        ? Ucum.formalRepresentation(unit).map(r -> FhirGenerator.quantity(value, r, system, unit))
        : Optional.of(FhirGenerator.quantity(value, unit, system, unit));
  }

  private static Optional<Quantity> quantity(String value, String unit, String system) {
    return QUANTITY_SYSTEM.equals(system)
        ? Ucum.formalRepresentation(unit).map(r -> FhirGenerator.quantity(value, r, system, unit))
        : Optional.of(FhirGenerator.quantity(value, unit, system, unit));
  }
}
