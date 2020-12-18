package enums;

import java.util.Arrays;

public enum ProcedureCategorySnomedMapping {

    DIAGNOSTIC("103693007", "Diagnostic procedure"),
    IMAGING("363679005", "Imaging"),
    SURGICAL("387713003", "Surgical procedure"),
    ADMINISTRATION_OF_MEDICINE("18629005", "Administration of medicine"),
    THERAPEUTIC("277132007", "Therapeutic procedure"),
    OTHER("394841004", "Other category");

    private final String code;
    private final String display;

    ProcedureCategorySnomedMapping(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public ProcedureCategorySnomedMapping getMappingByCode(String code) {
        return Arrays.stream(ProcedureCategorySnomedMapping.values())
                .filter(snomedEnum -> snomedEnum.getCode().equals(code))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Code '" + code + "' is not a valid SNOMED Mapping code")
                );
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }
}
