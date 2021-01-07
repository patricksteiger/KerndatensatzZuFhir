package enums;

import helper.Helper;

import java.util.Arrays;

import static helper.Helper.illegalCode;

public enum ProcedureCategorySnomedMapping {

    DIAGNOSTIC("103693007", "Diagnostic procedure", '1'),
    IMAGING("363679005", "Imaging", '3'),
    SURGICAL("387713003", "Surgical procedure", '5'),
    ADMINISTRATION_OF_MEDICINE("18629005", "Administration of medicine", '6'),
    THERAPEUTIC("277132007", "Therapeutic procedure", '8'),
    OTHER("394841004", "Other category", '9');

    private final String code;
    private final String display;
    private final char opsMapping;

    ProcedureCategorySnomedMapping(String code, String display, char opsMapping) {
        this.code = code;
        this.display = display;
        this.opsMapping = opsMapping;
    }

    /**
     * Returns SNOMED-Mapping corresponding to OPS-Code.
     *
     * @param opsCode OPS-Code
     * @return SNOMED-Mapping
     * @throws IllegalArgumentException if opsCode is empty or first character is not 1,3,5,6,8,9
     * @see "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
     */
    public static ProcedureCategorySnomedMapping getSnomedMappingByOpsCode(String opsCode) {
        if (!Helper.checkNonEmptyString(opsCode))
            throw new IllegalArgumentException("OPS-Code can't be null or empty");
        final char opsMapping = opsCode.charAt(0);
        return Arrays.stream(ProcedureCategorySnomedMapping.values())
                .filter(mappingEnum -> mappingEnum.getOpsMapping() == opsMapping)
                .findFirst()
                .orElseThrow(illegalCode(opsCode, "OPS-Code"));
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }

    public char getOpsMapping() {
        return this.opsMapping;
    }
}
