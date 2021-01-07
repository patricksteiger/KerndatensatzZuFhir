package enums;

public enum IdentifierTypeCode {

    MR("MR", "Krankenaktennummer"),
    XX("XX", "Organisations-ID");

    private final String code;
    private final String display;

    IdentifierTypeCode(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }
}
