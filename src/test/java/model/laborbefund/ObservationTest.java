package model.laborbefund;

import constants.Constants;
import enums.ReferenceRangeMeaning;
import helper.Logger;
import model.Laborbefund;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static util.Asserter.*;
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
    assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
  }

  @Test
  void testReferenceRangeLow() {
    // empty grenze [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
    assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
    // invalid grenze
    String grenze = getValueUnitStr("abc", "invalid unit");
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(grenze);
    assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
    // valid grenze
    grenze = "72";
    String input = getValueStr(grenze);
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
    Quantity result = laborbefund.getObservationReferenceRangeHigh();
    assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
  }

  @Test
  void testReferenceRangeType() {
    // empty typ [NO LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaboruntersuchung_referenzbereich_typ,
        laborbefund::getObservationReferenceRangeType);
    // invalid typ
    laborbefund.setLaboruntersuchung_referenzbereich_typ("invalid typ");
    assertEmptyValue(laborbefund.getObservationReferenceRangeType());
    // valid typ
    ReferenceRangeMeaning typ = ReferenceRangeMeaning.NORMAL;
    laborbefund.setLaboruntersuchung_referenzbereich_typ(getCodeStr(typ.getCode()));
    CodeableConcept result = laborbefund.getObservationReferenceRangeType();
    assertCodeableConcept(typ, result);
  }

  @Test
  void testReferenceRange() {
    // empty untergrenze, obergrenze, typ [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
    laborbefund.setLaboruntersuchung_referenzbereich_typ("");
    assertEmptyValue(laborbefund.getObservationReferenceRange());
    // non-empty untergrenze, obergrenze, typ [NO LOGGING]
    String untergrenze = "71";
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(getValueStr(untergrenze));
    String obergrenze = "132";
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(getValueStr(obergrenze));
    ReferenceRangeMeaning typ = ReferenceRangeMeaning.LUTEAL;
    laborbefund.setLaboruntersuchung_referenzbereich_typ(getCodeStr(typ.getCode()));
    Observation.ObservationReferenceRangeComponent result =
        laborbefund.getObservationReferenceRange();
    assertNonEmptyValue(result);
    assertQuantity(
        new BigDecimal(untergrenze), "1", Constants.QUANTITY_SYSTEM, "1", result.getLow());
    assertQuantity(
        new BigDecimal(obergrenze), "1", Constants.QUANTITY_SYSTEM, "1", result.getHigh());
    assertCodeableConcept(typ, result.getType());
  }
}
