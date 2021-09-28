package helper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static helper.FhirHelper.getGenderMapping;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Asserter.*;

class FhirHelperTest {
  @Nested
  class GenderMappingTest {
    @Test
    void testFReturnsFemale() {
      String FEMALE = "female";
      assertAdministrativeGender(FEMALE, getGenderMapping("F"));
    }

    @Test
    void testMReturnsMale() {
      String MALE = "male";
      assertAdministrativeGender(MALE, getGenderMapping("M"));
    }

    @Test
    void testDReturnsOtherWithExtensionD() {
      assertAdministrativeGenderDivers(getGenderMapping("D"));
    }

    @Test
    void testXReturnsOtherWithExtensionX() {
      assertAdministrativeGenderUnbestimmt(getGenderMapping("X"));
    }

    @Test
    void testUNKReturnsOtherWithExtensionX() {
      assertAdministrativeGenderUnbestimmt(getGenderMapping("UNK"));
    }

    @Test
    void testUNReturnsOtherWithExtensionX() {
      assertAdministrativeGenderUnbestimmt(getGenderMapping("UN"));
    }

    @Test
    void testUReturnsOtherWithExtensionX() {
      assertAdministrativeGenderUnbestimmt(getGenderMapping("U"));
    }

    @Test
    void testOReturnsEmpty() {
      assertTrue(getGenderMapping("O").isEmpty());
    }

    @Test
    void testEmptyInputReturnsEmpty() {
      assertTrue(getGenderMapping("").isEmpty());
    }

    @Test
    void testNullReturnsEmpty() {
      assertTrue(getGenderMapping(null).isEmpty());
    }
  }
}
