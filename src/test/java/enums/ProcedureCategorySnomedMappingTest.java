package enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcedureCategorySnomedMappingTest {
  @Test
  public void testGetSnomedMappingByOpsCode() {
    assertThrows(
        IllegalArgumentException.class,
        () -> ProcedureCategorySnomedMapping.fromOpsCode(null));
    assertThrows(
        IllegalArgumentException.class,
        () -> ProcedureCategorySnomedMapping.fromOpsCode(""));
    assertThrows(
        IllegalArgumentException.class,
        () -> ProcedureCategorySnomedMapping.fromOpsCode("test"));
    assertThrows(
        IllegalArgumentException.class,
        () -> ProcedureCategorySnomedMapping.fromOpsCode("0"));
    assertEquals(
        ProcedureCategorySnomedMapping.DIAGNOSTIC,
        ProcedureCategorySnomedMapping.fromOpsCode("1"));
    assertEquals(
        ProcedureCategorySnomedMapping.IMAGING,
        ProcedureCategorySnomedMapping.fromOpsCode("3"));
    assertEquals(
        ProcedureCategorySnomedMapping.SURGICAL,
        ProcedureCategorySnomedMapping.fromOpsCode("5"));
    assertEquals(
        ProcedureCategorySnomedMapping.ADMINISTRATION_OF_MEDICINE,
        ProcedureCategorySnomedMapping.fromOpsCode("6"));
    assertEquals(
        ProcedureCategorySnomedMapping.THERAPEUTIC,
        ProcedureCategorySnomedMapping.fromOpsCode("8"));
    assertEquals(
        ProcedureCategorySnomedMapping.OTHER,
        ProcedureCategorySnomedMapping.fromOpsCode("9"));
  }
}
