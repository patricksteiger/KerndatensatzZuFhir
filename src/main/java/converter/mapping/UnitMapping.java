package converter.mapping;

import converter.Ucum;
import converter.parse.MappingBean;
import helper.Helper;

import java.math.BigDecimal;
import java.util.Optional;

public class UnitMapping {
  private final String ucumCode;
  private final String ucumUnit;
  private final BigDecimal conversion;

  private UnitMapping(String ucumCode, String ucumUnit, BigDecimal conversion) {
    this.ucumCode = ucumCode;
    this.ucumUnit = ucumUnit;
    this.conversion = conversion;
  }

  public static Optional<UnitMapping> fromUcumUnit(String ucumUnit) {
    return Ucum.formalRepresentation(ucumUnit)
        .map(r -> new UnitMapping(ucumUnit, r, BigDecimal.ONE));
  }

  public static Optional<UnitMapping> fromBean(MappingBean mappingBean) {
    String localUnit = mappingBean.getLocalUnit();
    if (Helper.checkEmptyString(localUnit)) {
      return Optional.empty();
    }
    if (Ucum.validate(localUnit)) {
      return Ucum.formalRepresentation(localUnit)
          .map(r -> new UnitMapping(localUnit, r, BigDecimal.ONE));
    }
    String ucumUnit = mappingBean.getUcumUnit();
    if (Helper.checkEmptyString(ucumUnit) || !Ucum.validate(ucumUnit)) {
      return Optional.empty();
    }
    Optional<BigDecimal> conversion = parseConversion(mappingBean.getConversion());
    return conversion.flatMap(
        bigDecimal ->
            Ucum.formalRepresentation(ucumUnit).map(r -> new UnitMapping(ucumUnit, r, bigDecimal)));
  }

  private static Optional<BigDecimal> parseConversion(String conversion) {
    if (Helper.checkEmptyString(conversion) || !conversion.startsWith("x*")) {
      return Optional.empty();
    }
    try {
      String number = conversion.substring(2);
      BigDecimal result = new BigDecimal(number);
      return Optional.of(result);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public String getUcumUnit() {
    return ucumUnit;
  }

  public String getUcumCode() {
    return ucumCode;
  }

  public BigDecimal getConversion() {
    return conversion;
  }
}
