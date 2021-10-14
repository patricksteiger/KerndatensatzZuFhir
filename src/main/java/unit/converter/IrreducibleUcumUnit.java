package unit.converter;

import java.util.HashSet;
import java.util.Set;

// TODO: What are all non-reducible units? Could this be transferred to UnitMapping?
public class IrreducibleUcumUnit {
  private static final Set<String> nonReducibleUnits = generateIrreducibleUnits();

  private IrreducibleUcumUnit() {}

  public static boolean check(String ucumUnit) {
    return nonReducibleUnits.contains(ucumUnit);
  }

  public static Set<String> generateIrreducibleUnits() {
    Set<String> result = new HashSet<>();
    result.add("mm[Hg]");
    result.add("mm Hg");
    result.add("mmHg");
    return result;
  }
}
