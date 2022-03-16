package helper;

import interfaces.CharPredicate;

public class CharPredicates {
  private CharPredicates() {}

  public static CharPredicate not(CharPredicate p) {
    return p.negate();
  }

  public static CharPredicate equalTo(char o) {
    return c -> c == o;
  }
}
