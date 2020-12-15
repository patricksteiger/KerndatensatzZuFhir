package enums;

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

    public String getCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }

    public ProcedureCategorySnomedMapping getMappingByCode(String code) {
       if (DIAGNOSTIC.getCode().equals(code))
           return DIAGNOSTIC;
       if (IMAGING.getCode().equals(code))
           return IMAGING;
       if (SURGICAL.getCode().equals(code))
           return SURGICAL;
       if (ADMINISTRATION_OF_MEDICINE.getCode().equals(code))
           return ADMINISTRATION_OF_MEDICINE;
       if (THERAPEUTIC.getCode().equals(code))
           return THERAPEUTIC;
       if (OTHER.getCode().equals(code))
           return OTHER;
       throw new IllegalArgumentException("Code '" + code + "' is not valid.");
    }
}
