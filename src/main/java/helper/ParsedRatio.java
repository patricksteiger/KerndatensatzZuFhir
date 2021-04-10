package helper;

import org.hl7.fhir.r4.model.Ratio;

import java.util.List;
import java.util.Optional;

public class ParsedRatio {
  private ParsedRatio() {}

  public static Optional<Ratio> fromString(String str) {
    List<String> words = Helper.splitCode(str);
    String value = Helper.extractCode(words, "value=");
    String unit = Helper.extractCode(words, "unit=");
    return ValueAndUnitFraction.fromString(value, unit)
        .flatMap(FhirHelper::generateRatioFromFractions);
  }
}
