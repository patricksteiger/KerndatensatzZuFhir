package unit.mapping;

import unit.ucum.Ucum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Mapper {

  private static final Map<String, UnitMapping> mappings = generateMappings();

  private Mapper() {}

  public static Optional<UnitMapping> getUcumMappingFromLocalUnit(String localCode) {
    Optional<UnitMapping> mapping = Optional.ofNullable(mappings.get(localCode));
    return mapping.isPresent() ? mapping : getUcumMappingFromUcumCode(localCode);
  }

  public static Optional<UnitMapping> getUcumMappingFromUcumCode(String ucumCode) {
    return Ucum.validate(ucumCode) ? Optional.of(new UnitMapping(ucumCode, "1")) : Optional.empty();
  }

  // TODO: some local codes are mapped to creatin-codes, even though its not in their name
  // TODO: codes with unknown conversions
  public static Map<String, UnitMapping> generateMappings() {
    Map<String, UnitMapping> result = new HashMap<>();
    result.put("%", new UnitMapping("%", "1"));
    result.put("µg/d", new UnitMapping("ug/(24.h)", "1"));
    result.put("µg/dl", new UnitMapping("ug/dL", "1"));
    result.put("µg/l", new UnitMapping("ug/L", "1"));
    result.put("µg/ml", new UnitMapping("ug/L", "1000"));
    result.put("µmol/d", new UnitMapping("ug/(24.h)", "1"));
    result.put("µmol/l", new UnitMapping("umol/L", "1"));
    result.put("µmol/mol", new UnitMapping("umol/mol{creat}", "1"));
    result.put("Anz./µl", new UnitMapping("/uL", "1"));
    result.put("B.E", new UnitMapping("", "1"));
    result.put("fl", new UnitMapping("fL", "1"));
    result.put("g/dl", new UnitMapping("g/dL", "1"));
    result.put("g/g Krea", new UnitMapping("mg/g{creat}", "1000"));
    result.put("g/l", new UnitMapping("mg/dL", "100"));
    result.put("g/mol K.", new UnitMapping("mg/mmol{creat}", "1"));
    result.put("Giga/l", new UnitMapping("10*3/uL", "1"));
    result.put("IU/l", new UnitMapping("[IU]/L", "1"));
    result.put("IU/ml", new UnitMapping("U/mL", "1"));
    result.put("kU/l", new UnitMapping("k[IU]/L", "1"));
    result.put("l/l", new UnitMapping("%", "100"));
    result.put("mg/d", new UnitMapping("mg/(24.h)", "1"));
    result.put("mg/dl", new UnitMapping("mg/dL", "1"));
    result.put("mg/l", new UnitMapping("mg/L", "1"));
    result.put("mg/lFEU", new UnitMapping("ng/mL{FEU}", "1000"));
    result.put("mIU/l", new UnitMapping("m[IU]/L", "1"));
    result.put("mIU/ml", new UnitMapping("[IU]/L", "1"));
    result.put("mm", new UnitMapping("mm", "1"));
    result.put("mm Hg", new UnitMapping("mm[Hg]", "1"));
    result.put("mm/h", new UnitMapping("mm/h", "1"));
    result.put("mmol/d", new UnitMapping("mmol/(24.h)", "1"));
    result.put("mmol/l", new UnitMapping("mmol/L", "1"));
    result.put("mmol/mol", new UnitMapping("mmol/mol{creat}", "1"));
    result.put("mol/molK", new UnitMapping("mmol/mol{creat}", "1000"));
    result.put("mosm/kg", new UnitMapping("mosm/kg", "1"));
    result.put("mU/l", new UnitMapping("u[IU]/mL", "1"));
    result.put("ng/l", new UnitMapping("pg/mL", "1"));
    result.put("ng/ml", new UnitMapping("ug/L", "1"));
    result.put("nmol/l", new UnitMapping("nmol/L", "1"));
    result.put("pg", new UnitMapping("pg", "1"));
    result.put("pg/ml", new UnitMapping("ug/L", "0.001"));
    result.put("pmol/l", new UnitMapping("pmol/L", "1"));
    result.put("Ratio", new UnitMapping("{ratio}", "1"));
    result.put("sec.", new UnitMapping("s", "1"));
    result.put("Tera/l", new UnitMapping("10*6/uL", "1"));
    result.put("Titer", new UnitMapping("{titer}", "1"));
    result.put("U/l", new UnitMapping("U/L", "1"));
    result.put("U/ml", new UnitMapping("[arb'U]/mL", "1"));
    result.put("Zell./µl", new UnitMapping("/uL", "1"));
    return result;
  }
}
