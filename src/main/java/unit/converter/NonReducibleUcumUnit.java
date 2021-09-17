package unit.converter;

import java.util.HashSet;
import java.util.Set;

// TODO: What are all non-reducible units? Could this be transferred to UnitMapping?
public class NonReducibleUcumUnit {
  private static final Set<String> nonReducibleUnits = generateNonReducibleUcumUnits();

  private NonReducibleUcumUnit() {}

  public static boolean check(String ucumUnit) {
    return nonReducibleUnits.contains(ucumUnit);
  }

  public static Set<String> generateNonReducibleUcumUnits() {
    Set<String> result = new HashSet<>();
    result.add("mm[Hg]");
    return result;
  }
}
