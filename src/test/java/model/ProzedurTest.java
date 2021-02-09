package model;

import constants.MetaProfile;
import constants.MetaSource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Meta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProzedurTest {
  private Prozedur prozedur = null;

  @BeforeEach
  public void setUp() {
    prozedur = new Prozedur();
  }

  @Test
  void testGetMeta() {
    Meta received = prozedur.getMeta();
    assertEquals(MetaSource.PROZEDUR_PROCEDURE, received.getSource());
    List<CanonicalType> profiles = received.getProfile();
    assertEquals(1, profiles.size());
    assertEquals(MetaProfile.PROZEDUR_PROCEDURE, profiles.get(0).asStringValue());
  }

  @Test
  void testGetPerformed() {
    String durchfuehrung = "2020-03-07";
    prozedur.setDurchfuehrungsdatum(durchfuehrung);
    DateTimeType date = prozedur.getPerformed();
    // Check for correct date and timezone
    // String pattern = "2020-01-07T[012][0-9]:[0-5][0-9]:[0-5][0-9]\\+01:00";
    // assertTrue(date.getValueAsString().matches(pattern));
    //assertEquals("2020-03-07", date.getValueAsString());
  }
}
