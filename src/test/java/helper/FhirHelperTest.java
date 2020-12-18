package helper;

import enums.ProcedureCategorySnomedMapping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FhirHelperTest {
    @Test
    public void testGetSnomedMappingFromOps() {
        assertThrows(IllegalArgumentException.class, () -> FhirHelper.getSnomedMappingFromOps(null));
        assertThrows(IllegalArgumentException.class, () -> FhirHelper.getSnomedMappingFromOps(""));
        assertThrows(IllegalArgumentException.class, () -> FhirHelper.getSnomedMappingFromOps("test"));
        assertThrows(IllegalArgumentException.class, () -> FhirHelper.getSnomedMappingFromOps("0"));
        assertEquals(ProcedureCategorySnomedMapping.DIAGNOSTIC, FhirHelper.getSnomedMappingFromOps("1"));
        assertEquals(ProcedureCategorySnomedMapping.IMAGING, FhirHelper.getSnomedMappingFromOps("3"));
        assertEquals(ProcedureCategorySnomedMapping.SURGICAL, FhirHelper.getSnomedMappingFromOps("5"));
        assertEquals(ProcedureCategorySnomedMapping.ADMINISTRATION_OF_MEDICINE, FhirHelper.getSnomedMappingFromOps("6"));
        assertEquals(ProcedureCategorySnomedMapping.THERAPEUTIC, FhirHelper.getSnomedMappingFromOps("8"));
        assertEquals(ProcedureCategorySnomedMapping.OTHER, FhirHelper.getSnomedMappingFromOps("9"));
    }
}
