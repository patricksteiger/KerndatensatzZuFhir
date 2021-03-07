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

  public static boolean validate(String unit) {
    return unit != null && service.validate(unit) == null;
  }

  public static Optional<String> formalRepresentation(String ucumUnit) {
    try {
      String formal = service.analyse(ucumUnit);
      return Optional.of(formal);
    } catch (UcumException e) {
      return Optional.empty();
    }
  }
}
