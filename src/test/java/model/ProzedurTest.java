package model;

import constants.Constants;
import constants.URLs;
import org.hl7.fhir.r4.model.CanonicalType;
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
    public void testGetMeta() {
        Meta received = prozedur.getMeta();
        assertEquals(Constants.SOURCE_UKU_SAP_PROZEDUR, received.getSource());
        List<CanonicalType> profiles = received.getProfile();
        assertEquals(1, profiles.size());
        assertEquals(URLs.PROCEDURE_PROFILE_URL, profiles.get(0).asStringValue());
    }


}
