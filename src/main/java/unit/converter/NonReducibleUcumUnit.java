package unit.converter;

import java.util.HashSet;
import java.util.Set;

public class NonReducibleUcumUnit {
  private static final Set<String> nonReducibleUnits = new HashSet<>();

  static {
    nonReducibleUnits.add("mm[Hg]");
  }

  private NonReducibleUcumUnit() {}

  public static boolean check(String unit) {
    return nonReducibleUnits.contains(unit);
  }
}
