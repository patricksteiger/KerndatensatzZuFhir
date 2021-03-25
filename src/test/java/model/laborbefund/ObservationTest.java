package model.laborbefund;

import helper.Logger;
import model.Laborbefund;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.assertEmptyValue;
import static util.Asserter.assertNonEmptyValue;
import static util.Util.*;

class ObservationTest {
  private Laborbefund laborbefund;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    laborbefund = new Laborbefund();
    setMockLoggerField(laborbefund, LOGGER);
  }

  @Test
  void testReferenceRangeHigh() {
    // empty grenze [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
    assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
    // invalid grenze
    String grenze = getValueUnitStr("abc", "invalid unit");
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(grenze);
    assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
    // valid grenze
    grenze = "127";
    String input = getValueStr(grenze);
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
    Quantity result = laborbefund.getObservationReferenceRangeHigh();
    assertNonEmptyValue(result);
    assertEquals(new BigDecimal(grenze), result.getValue());
    assertEquals("(unity)", result.getUnit());
    assertEquals(null, result.getCode());
  }
}
