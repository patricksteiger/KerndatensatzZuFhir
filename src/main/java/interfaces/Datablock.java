package interfaces;

import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public interface Datablock {
  List<Resource> toFhirResources();
}
