package unit.converter;

import java.util.HashSet;
import java.util.Set;

public class UnitReducible {
  private static final Set<String> nonReducibleUnits = new HashSet<>();

  static {
    nonReducibleUnits.add("mm[Hg]");
  }

  private UnitReducible() {}

  public static boolean fromString(String unit) {
    return !nonReducibleUnits.contains(unit);
  }
}
