package helper;

import constants.CodingSystem;
import constants.Constants;
import constants.ExtensionUrl;
import constants.IdentifierSystem;
import org.hl7.fhir.r4.model.*;
import unit.ucum.Ucum;
import valueSets.MIICoreLocations;

import java.util.Optional;

public class FhirHelper {
  private FhirHelper() {}

  /**
   * Returns diagnostic report status from case-insensitive string.
   *
   * @param diagnosticReportStatus
   * @return diagnostic report status enum
   * @throws IllegalArgumentException if diagnosticReportStatus is not valid.
   */
  public static Optional<DiagnosticReport.DiagnosticReportStatus>
      getDiagnosticReportStatusFromString(String diagnosticReportStatus) {
    for (DiagnosticReport.DiagnosticReportStatus status :
        DiagnosticReport.DiagnosticReportStatus.values()) {
      if (status.name().equalsIgnoreCase(diagnosticReportStatus)) {
        return Optional.of(status);
      }
    }
    return Optional.empty();
  }

  public static Optional<Observation.ObservationStatus> getObservationStatusFromCode(String code) {
    return Helper.optionalOfException(() -> Observation.ObservationStatus.fromCode(code));
  }

  public static Optional<ResearchSubject.ResearchSubjectStatus> getResearchSubjectStatusFromCode(
      String code) {
    return Helper.optionalOfException(() -> ResearchSubject.ResearchSubjectStatus.fromCode(code));
  }

  // TODO: Add method to UnitConverter to handle guaranteed non-fractions
  public static Ratio generateRatioFromFractions(
      ValueAndUnitFraction valueAndUnitFraction) {
    Quantity n = quantityFromNonFraction(valueAndUnitFraction.getValueNumerator(), valueAndUnitFraction.getUnitNumerator());
    Quantity d = quantityFromNonFraction(valueAndUnitFraction.getValueDenominator(), valueAndUnitFraction.getUnitDenominator());
    return FhirGenerator.ratio(n, d);
  }

  public static Quantity quantityFromNonFraction(String value, String unit) {
    String display = Ucum.formalRepresentation(unit).orElse("");
    String system = Ucum.validate(unit) ? Constants.QUANTITY_SYSTEM : "";
    return FhirGenerator.quantity(value, display, system, unit);
  }

  public static boolean emptyDateTimeType(DateTimeType dateTimeType) {
    return !dateTimeType.hasValue() && !dateTimeType.hasExtension();
  }

  /**
   * Returns the mapping of given gender code
   *
   * @param gender codes: F, M, D, X, U, UN, UNK
   * @return Mapping of given code, F maps to FEMALE, M to MALE, D to OTHER with extension "D" and
   *     X, U, UN, UNK to OTHER with extension "X"
   * @see "https://wiki.hl7.de/index.php?title=Geschlecht#administratives_Geschlecht"
   */
  public static Optional<Enumeration<Enumerations.AdministrativeGender>> getGenderMapping(String gender) {
    return Optional.ofNullable(
        switch (gender == null ? "" : gender) {
          case "F" ->
              administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.FEMALE);
          case "M" ->
              administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.MALE);
          case "D" -> getGenderDE("D", "divers");
          case "X", "U", "UN", "UNK" -> getGenderDE("X", "unbestimmt");
          default -> null;
        }
      );
  }

  private static Enumeration<Enumerations.AdministrativeGender> getGenderDE(
      String code, String display) {
    String system = CodingSystem.ADMINISTRATIVE_GENDER;
    Coding gender = FhirGenerator.coding(code, system, display);
    String url = ExtensionUrl.ADMINISTRATIVE_GENDER;
    Extension extension = FhirGenerator.extension(url, gender);
    Enumeration<Enumerations.AdministrativeGender> result =
        administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender.OTHER);
    result.addExtension(extension);
    return result;
  }

  public static Enumeration<Enumerations.AdministrativeGender>
      administrativeGenderEnumToEnumeration(Enumerations.AdministrativeGender gender) {
    Enumeration<Enumerations.AdministrativeGender> result =
        new Enumeration<>(new Enumerations.AdministrativeGenderEnumFactory());
    result.setValue(gender);
    return result;
  }

  public static Reference getUKUAssignerReference() {
    Identifier assignerId =
        FhirGenerator.identifier(MIICoreLocations.UKU.getCode(), IdentifierSystem.NS_DIZ);
    return FhirGenerator.reference(assignerId, MIICoreLocations.UKU.getDisplay());
  }
}
