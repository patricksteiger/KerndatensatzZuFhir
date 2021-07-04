package interfaces;

@FunctionalInterface
public interface CharPredicate {
  default CharPredicate and(CharPredicate other) {
    return value -> this.test(value) && other.test(value);
  }

  default CharPredicate negate() {
    return value -> !this.test(value);
  }

  default CharPredicate or(CharPredicate other) {
    return value -> this.test(value) || other.test(value);
  }

  boolean test(char value);
}
