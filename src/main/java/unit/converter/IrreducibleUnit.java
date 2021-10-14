package unit.converter;

import java.util.HashSet;
import java.util.Set;

// TODO: What are all non-reducible units? Could this be transferred to UnitMapping?
public class IrreducibleUnit {
  private static final Set<String> nonReducibleUnits = generateIrreducibleUnits();

  private IrreducibleUnit() {}

  public static boolean check(String ucumUnit) {
    return nonReducibleUnits.contains(ucumUnit);
  }

  public static Set<String> generateIrreducibleUnits() {
    var result = new HashSet<String>();
    result.add("mm[Hg]");
    result.add("mm Hg");
    result.add("mmHg");
    return result;
  }
}
