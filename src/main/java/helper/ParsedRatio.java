package helper;

import org.hl7.fhir.r4.model.Ratio;

import java.util.List;
import java.util.Optional;

public class ParsedRatio {
  private ParsedRatio() {}

  /**
   * Parses Ratio from a String formatted like "value="1/2" unit="mmol/L"". If no denominator is
   * given (e.g. "value="1" unit="g"") it is set to 1 for both value and unit.
   *
   * @param str Expected format: "value="1/2" unit="mmol/L""
   * @return Ratio, if value and unit are valid. Otherwise empty.
   */
  public static Optional<Ratio> fromString(String str) {
    List<String> words = Helper.splitCode(str);
    String value = Helper.extractCode(words, "value=");
    String unit = Helper.extractCode(words, "unit=");
    return ValueAndUnitFraction.fromString(value, unit)
        .flatMap(FhirHelper::generateRatioFromFractions);
  }
}
