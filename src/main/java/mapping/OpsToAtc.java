package mapping;

import helper.AnyCode;
import helper.FhirGenerator;
import helper.Helper;
import interfaces.Code;
import org.hl7.fhir.r4.model.Coding;

import java.util.Optional;

public class OpsToAtc {
  private OpsToAtc() {}

  public static Optional<Coding> from(Code parsedCode, String unit) {
    if (Helper.checkEmptyString(parsedCode.getCode())) {
      return Optional.empty();
    }
    return OpsToAtcMapper.getMapping(parsedCode.getCode(), unit)
        .map(AnyCode::from)
        .or(() -> Optional.of(parsedCode))
        .map(FhirGenerator::coding);
  }
}
