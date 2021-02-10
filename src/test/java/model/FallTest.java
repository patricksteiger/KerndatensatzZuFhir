package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class FallTest {
  private Fall fall;

  @BeforeEach
  void setUp() {
    fall = new Fall();
  }

  @Test
  void testLogging() {
    fall.setEinrichtungskontakt_aufnahmeanlass("invalid");
    assertNull(fall.getEinrichtungsEncounterAdmitSource());
    fall.setEinrichtungskontakt_aufnahmeanlass("bla");
    assertNull(fall.getEinrichtungsEncounterAdmitSource());
  }
}
