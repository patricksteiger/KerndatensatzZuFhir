package unit.mapping.mapping;

import helper.Helper;
import unit.mapping.parse.MappingBean;
import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.Optional;

public class UnitMapping {
  private final String ucumCode;
  private final BigDecimal conversion;

  private UnitMapping(String ucumCode, BigDecimal conversion) {
    this.ucumCode = ucumCode;
    this.conversion = conversion;
  }

  /**
   * Checks if given unit is in UCUM-Format and returns simple mapping with conversion of 1.
   *
   * @param ucumUnit unit
   * @return UnitMapping with conversion 1, if unit is not UCUM it returns empty
   */
  public static Optional<UnitMapping> fromUcumUnit(String ucumUnit) {
    if (Ucum.validate(ucumUnit)) {
      UnitMapping mapping = new UnitMapping(ucumUnit, BigDecimal.ONE);
      return Optional.of(mapping);
    } else {
      return Optional.empty();
    }
  }

  public static Optional<UnitMapping> fromBean(MappingBean mappingBean) {
    String localUnit = mappingBean.getLocalUnit();
    if (Helper.checkEmptyString(localUnit)) {
      return Optional.empty();
    }
    if (Ucum.validate(localUnit)) {
      UnitMapping mapping = new UnitMapping(localUnit, BigDecimal.ONE);
      return Optional.of(mapping);
    }
    String ucumUnit = mappingBean.getUcumUnit();
    if (Helper.checkEmptyString(ucumUnit) || !Ucum.validate(ucumUnit)) {
      return Optional.empty();
    }
    Optional<BigDecimal> conversion = parseConversion(mappingBean.getConversion());
    return conversion.map(c -> new UnitMapping(ucumUnit, c));
  }

  private static Optional<BigDecimal> parseConversion(String conversion) {
    if (Helper.checkEmptyString(conversion) || !conversion.startsWith("x*")) {
      return Optional.empty();
    }
    String number = conversion.substring(2);
    return Helper.parseValue(number);
  }

  public String getUcumCode() {
    return ucumCode;
  }

  public BigDecimal getConversion() {
    return conversion;
  }
}
