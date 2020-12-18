package enums;

import helper.FhirHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcedureCategorySnomedMappingTest {
    @Test
    public void testGetSnomedMappingByOpsCode() {
        assertThrows(IllegalArgumentException.class, () -> ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode(null));
        assertThrows(IllegalArgumentException.class, () -> ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode(""));
        assertThrows(IllegalArgumentException.class, () -> ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("test"));
        assertThrows(IllegalArgumentException.class, () -> ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("0"));
        assertEquals(ProcedureCategorySnomedMapping.DIAGNOSTIC, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("1"));
        assertEquals(ProcedureCategorySnomedMapping.IMAGING, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("3"));
        assertEquals(ProcedureCategorySnomedMapping.SURGICAL, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("5"));
        assertEquals(ProcedureCategorySnomedMapping.ADMINISTRATION_OF_MEDICINE, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("6"));
        assertEquals(ProcedureCategorySnomedMapping.THERAPEUTIC, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("8"));
        assertEquals(ProcedureCategorySnomedMapping.OTHER, ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode("9"));
    }
}
