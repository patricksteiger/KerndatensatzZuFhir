package unit.ucum;

import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;

import java.util.Optional;

public class Ucum {
  private static final String UCUM_ESSENCE_PATH = "src/main/resources/ucum-essence.xml";
  private static UcumEssenceService service;

  static {
    try {
      service = new UcumEssenceService(UCUM_ESSENCE_PATH);
    } catch (UcumException e) {
      e.printStackTrace();
    }
  }

  private Ucum() {}

  /**
   * Checks whether the given unit is in UCUM-Format.
   *
   * @param unit Unit to be checked
   * @return true if unit is in UCUM-Format, false otherwise
   */
  public static boolean validate(String unit) {
    return unit != null && service.validate(unit) == null;
  }

  /**
   * Returns representation of UCUM-Unit generally used for "unit" in FHIR-Resource "Quantity".
   *
   * @param ucumUnit UCUM-Unit
   * @return Representation of UCUM-Unit, returns empty if given unit is not int UCUM-Format
   */
  public static Optional<String> formalRepresentation(String ucumUnit) {
    try {
      String formal = service.analyse(ucumUnit);
      return Optional.of(formal);
    } catch (UcumException e) {
      return Optional.empty();
    }
  }
}
