package unit.mapping;

import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ONE;

public class Mapper {

  private static final BigDecimal THOUSAND = new BigDecimal("1000");
  private static final BigDecimal THOUSANDTH = new BigDecimal("0.001");
  private static final BigDecimal HUNDRED = new BigDecimal("100");
  private static final Map<String, UnitMapping> mappings = generateMappings();

  private Mapper() {}

  public static Optional<UnitMapping> getUcumMappingFromLocalUnit(String localCode) {
    Optional<UnitMapping> mapping = Optional.ofNullable(mappings.get(localCode));
    return mapping.isPresent() ? mapping : getUcumMappingFromUcumCode(localCode);
  }

  private static Optional<UnitMapping> getUcumMappingFromUcumCode(String ucumCode) {
    return Ucum.validate(ucumCode) ? Optional.of(new UnitMapping(ucumCode, ONE)) : Optional.empty();
  }

  // TODO: some local codes are mapped to creatin-codes, even though its not in their name
  // TODO: codes with unknown conversions
  // TODO: should the conversion be dependant on loinc-code given in resource Ulm-Mapping_checklist?
  public static Map<String, UnitMapping> generateMappings() {
    Map<String, UnitMapping> result = new HashMap<>();
    result.put("%", new UnitMapping("%", ONE));
    result.put("µg/d", new UnitMapping("ug/(24.h)", ONE));
    result.put("µg/dl", new UnitMapping("ug/dL", ONE));
    result.put("µg/l", new UnitMapping("ug/L", ONE));
    result.put("µg/ml", new UnitMapping("ug/L", THOUSAND));
    result.put("µmol/d", new UnitMapping("ug/(24.h)", ONE));
    result.put("µmol/l", new UnitMapping("umol/L", ONE));
    result.put("µmol/mol", new UnitMapping("umol/mol{creat}", ONE));
    result.put("Anz./µl", new UnitMapping("/uL", ONE));
    result.put("B.E", new UnitMapping("", ONE));
    result.put("fl", new UnitMapping("fL", ONE));
    result.put("g/dl", new UnitMapping("g/dL", ONE));
    result.put("g/g Krea", new UnitMapping("mg/g{creat}", THOUSAND));
    result.put("g/l", new UnitMapping("mg/dL", HUNDRED));
    result.put("g/mol K.", new UnitMapping("mg/mmol{creat}", ONE));
    result.put("Giga/l", new UnitMapping("10*3/uL", ONE));
    result.put("IU/l", new UnitMapping("[IU]/L", ONE));
    result.put("IU/ml", new UnitMapping("U/mL", ONE));
    result.put("kU/l", new UnitMapping("k[IU]/L", ONE));
    result.put("l/l", new UnitMapping("%", HUNDRED));
    result.put("mg/d", new UnitMapping("mg/(24.h)", ONE));
    result.put("mg/dl", new UnitMapping("mg/dL", ONE));
    result.put("mg/l", new UnitMapping("mg/L", ONE));
    result.put("mg/lFEU", new UnitMapping("ng/mL{FEU}", THOUSAND));
    result.put("mIU/l", new UnitMapping("m[IU]/L", ONE));
    result.put("mIU/ml", new UnitMapping("[IU]/L", ONE));
    result.put("mm", new UnitMapping("mm", ONE));
    result.put("mm Hg", new UnitMapping("mm[Hg]", ONE));
    result.put("mm/h", new UnitMapping("mm/h", ONE));
    result.put("mmol/d", new UnitMapping("mmol/(24.h)", ONE));
    result.put("mmol/l", new UnitMapping("mmol/L", ONE));
    result.put("mmol/mol", new UnitMapping("mmol/mol{creat}", ONE));
    result.put("mol/molK", new UnitMapping("mmol/mol{creat}", THOUSAND));
    result.put("mosm/kg", new UnitMapping("mosm/kg", ONE));
    result.put("mU/l", new UnitMapping("u[IU]/mL", ONE));
    result.put("ng/l", new UnitMapping("pg/mL", ONE));
    result.put("ng/ml", new UnitMapping("ug/L", ONE));
    result.put("nmol/l", new UnitMapping("nmol/L", ONE));
    result.put("pg", new UnitMapping("pg", ONE));
    result.put("pg/ml", new UnitMapping("ug/L", THOUSANDTH));
    result.put("pmol/l", new UnitMapping("pmol/L", ONE));
    result.put("Ratio", new UnitMapping("{ratio}", ONE));
    result.put("sec.", new UnitMapping("s", ONE));
    result.put("Tera/l", new UnitMapping("10*6/uL", ONE));
    result.put("Titer", new UnitMapping("{titer}", ONE));
    result.put("U/l", new UnitMapping("U/L", ONE));
    result.put("U/ml", new UnitMapping("[arb'U]/mL", ONE));
    result.put("Zell./µl", new UnitMapping("/uL", ONE));
    return result;
  }
}
