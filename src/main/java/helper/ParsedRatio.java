package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ParsedRatio {

  private final Optional<Entry> numerator;
  private final Optional<Entry> denominator;

  private ParsedRatio(Optional<Entry> numerator, Optional<Entry> denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public static ParsedRatio fromString(String str) {
    List<String> words = Helper.splitCode(str);
    String value = Helper.extractCode(words, "value=");
    String unit = Helper.extractCode(words, "unit=");
    String[] splitValue = value.split("/");
    String[] splitUnit = unit.split("/");
    if ((splitValue.length != 1 && splitValue.length != 2) || splitUnit.length != 2) {
      return new ParsedRatio(Optional.empty(), Optional.empty());
    }
    Optional<Entry> numerator = parseEntry(splitValue[0], splitUnit[0]);
    String denom = splitValue.length == 2 ? splitValue[1] : "1";
    Optional<Entry> denominator = parseEntry(denom, splitUnit[1]);
    return new ParsedRatio(numerator, denominator);
  }

  private static Optional<Entry> parseEntry(String value, String unit) {
    if (Helper.checkEmptyString(unit)) {
      return Optional.empty();
    }
    Optional<BigDecimal> parsedValue = Helper.maybeBigDecimal(value);
    return parsedValue.map(val -> new Entry(val, unit, unit));
  }

  public Optional<Ratio> toRatio() {
    if (!numerator.isPresent() || !denominator.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(
        FhirGenerator.ratio(numerator.get().toQuantity(), denominator.get().toQuantity()));
  }

  private static class Entry {
    private final BigDecimal value;
    private final String unit;
    private final String ucum;

    public Entry(BigDecimal value, String unit, String ucum) {
      this.value = value;
      this.unit = unit;
      this.ucum = ucum;
    }

    public Quantity toQuantity() {
      return FhirGenerator.quantity(value, unit, Constants.QUANTITY_SYSTEM, ucum);
    }
  }
}
