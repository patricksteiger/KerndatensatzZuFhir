package helper;

import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import unit.converter.UnitConverter;

import java.util.List;
import java.util.Optional;

public class ParsedRatio {
  private static final String FRACTION = "/";

  private ParsedRatio() {}

  public static Optional<Ratio> fromString(String str) {
    List<String> words = Helper.splitCode(str);
    String value = Helper.extractCode(words, "value=");
    String unit = Helper.extractCode(words, "unit=");
    String[] splitUnit = unit.split(FRACTION);
    if (splitUnit.length < 1 || splitUnit.length > 2 || Helper.checkAnyEmptyString(splitUnit)) {
      return Optional.empty();
    }
    String[] splitValue = value.split(FRACTION);
    if (splitValue.length < 1 || splitValue.length > 2 || Helper.checkAnyEmptyString(splitValue)) {
      return Optional.empty();
    }
    String numeratorValue = splitValue[0];
    Optional<Quantity> numerator = UnitConverter.fromLocalCode(splitUnit[0], numeratorValue);
    if (!numerator.isPresent()) {
      return Optional.empty();
    }
    String denominatorValue = splitValue.length == 2 ? splitValue[1].trim() : "1";
    if (Helper.isZero(denominatorValue)) {
      return Optional.empty();
    }
    String denominatorUnit = splitUnit.length == 2 ? splitUnit[1].trim() : "1";
    Optional<Quantity> denominator = UnitConverter.fromLocalCode(denominatorUnit, denominatorValue);
    return denominator.map(d -> FhirGenerator.ratio(numerator.get(), d));
  }
}
