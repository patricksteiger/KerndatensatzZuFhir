package valueSets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProcedureCategorySnomedMappingTest {
  @Test
  public void testGetSnomedMappingByOpsCode() {
    assertFalse(ProcedureCategorySnomedMapping.fromOpsCode(null).isPresent());
    assertFalse(ProcedureCategorySnomedMapping.fromOpsCode("").isPresent());
    assertFalse(ProcedureCategorySnomedMapping.fromOpsCode("test").isPresent());
    assertFalse(ProcedureCategorySnomedMapping.fromOpsCode("0").isPresent());
    assertEquals(
        ProcedureCategorySnomedMapping.DIAGNOSTIC,
        ProcedureCategorySnomedMapping.fromOpsCode("1").get());
    assertEquals(
        ProcedureCategorySnomedMapping.IMAGING,
        ProcedureCategorySnomedMapping.fromOpsCode("3").get());
    assertEquals(
        ProcedureCategorySnomedMapping.SURGICAL,
        ProcedureCategorySnomedMapping.fromOpsCode("5").get());
    assertEquals(
        ProcedureCategorySnomedMapping.ADMINISTRATION_OF_MEDICINE,
        ProcedureCategorySnomedMapping.fromOpsCode("6").get());
    assertEquals(
        ProcedureCategorySnomedMapping.THERAPEUTIC,
        ProcedureCategorySnomedMapping.fromOpsCode("8").get());
    assertEquals(
        ProcedureCategorySnomedMapping.OTHER,
        ProcedureCategorySnomedMapping.fromOpsCode("9").get());
  }
}
