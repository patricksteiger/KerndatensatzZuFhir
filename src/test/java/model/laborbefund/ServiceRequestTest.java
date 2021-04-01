package model.laborbefund;

import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Laborbefund;
import org.hl7.fhir.r4.model.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.assertEmptyValue;
import static util.Asserter.assertIdentifier;
import static util.Util.*;

class ServiceRequestTest {
  private Laborbefund laborbefund;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    laborbefund = new Laborbefund();
    setMockLoggerField(laborbefund, LOGGER);
  }

  @Test
  void testIdentifier() {
    // empty id
    laborbefund.setIdentifikation("");
    assertEmptyValue(laborbefund.getServiceRequestIdentifier());
    // non-empty id
    String id = "1234567890";
    laborbefund.setIdentifikation(id);
    Identifier result = laborbefund.getServiceRequestIdentifier();
    assertIdentifier(id, IdentifierSystem.EMPTY, IdentifierTypeCode.PLAC, result);
  }

  @Test
  void testSpecimen() {
    // fixed value
    assertEmptyValue(laborbefund.getServiceRequestSpecimen());
  }

  @Test
  void testAuthoredOn() {
    // invalid date
    laborbefund.setLaboranforderung_anforderungsdatum("1997-12-34");
    assertEmptyValue(laborbefund.getServiceRequestAuthoredOn());
    // valid date
    String date = "2007-12-01";
    laborbefund.setLaboranforderung_anforderungsdatum(date);
    Date result = laborbefund.getServiceRequestAuthoredOn();
    assertEquals(expectedDateString(date), result);
  }
}
