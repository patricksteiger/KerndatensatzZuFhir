package unit.converter;

import constants.Constants;
import helper.FhirGenerator;
import helper.Helper;
import org.hl7.fhir.r4.model.Quantity;
import unit.mapping.LoincMapping;
import unit.mapping.Mapper;
import unit.mapping.UnitMapping;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

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
        : Optional.of(quantityFromLocalUnitAndValue(localUnit, value));
  }

  // TODO: Reduce possible fractions
  private static Quantity quantityFromLocalUnitAndValue(String localUnit, String value) {
    boolean localUnitIsUcumUnit = Ucum.validate(localUnit);
    String display = localUnitIsUcumUnit ? Ucum.formalRepresentation(localUnit).orElse("") : "";
    String system = localUnitIsUcumUnit ? Constants.QUANTITY_SYSTEM : "";
    return FhirGenerator.quantity(value, display, system, localUnit);
  }

  private static Optional<Quantity> quantityFromLoincMappingAndValue(
      LoincMapping loincMapping, String value) {
    String ucumCode = loincMapping.getUcumCode();
    return IrreducibleUnit.check(ucumCode)
        ? quantity(value, loincMapping.getUcumCode())
        : convertValue(value, loincMapping.getConversion()).flatMap(val -> quantity(val, ucumCode));
  }

  private static Optional<Quantity> quantityFromUnitMappingAndValue(
      UnitMapping mapping, String value) {
    String ucumCode = mapping.getUcumCode();
    return IrreducibleUnit.check(ucumCode)
        ? quantity(value, mapping.getUcumCode())
        : convertValue(value, mapping.getConversion()).flatMap(val -> quantity(val, ucumCode));
  }

  private static Optional<BigDecimal> convertValue(String value, BigDecimal conversion) {
    return Helper.parseBigDecimalFromQuantity(value).map(conversion::multiply);
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
