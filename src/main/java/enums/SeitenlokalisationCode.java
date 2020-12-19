package enums;

import constants.URLs;

import java.util.Arrays;

public enum SeitenlokalisationCode {

    RECHTS("R", "rechts", URLs.OPS_SEITENLOKALISATION_OID),
    LINKS("L", "links", URLs.OPS_SEITENLOKALISATION_OID),
    BEIDSEITS("B", "beidseits", URLs.OPS_SEITENLOKALISATION_OID),
    MITTELLINIENZONE("M", "Mittellinienzone", URLs.OPS_SEITENLOKALISATION_OID),
    NICHT_ZUTREFFEND("NA", "nicht zutreffend", URLs.OPS_SEITENLOKALISATION_OID_ERROR),
    UNBEKANNT("UNK", "unbekannt", URLs.OPS_SEITENLOKALISATION_OID_ERROR);

    private final String code;
    private final String display;
    private final String codeSystem;

    SeitenlokalisationCode(String code, String display, String codeSystem) {
        this.code = code;
        this.display = display;
        this.codeSystem = codeSystem;
    }

    /**
     * Gets SeitenlokalisationsCode for given, case-sensitive code.
     *
     * @param code Valid codes: R, L, B, M, NA, UNK.
     * @return SeitenlokalisationsCode
     * @throws IllegalArgumentException If invalid code is given.
     * @see "https://art-decor.org/art-decor/decor-valuesets--mide-?id=1.2.40.0.34.10.176&effectiveDate="
     */
    public static SeitenlokalisationCode getSeitenlokalisationByCode(String code) {
        return Arrays.stream(SeitenlokalisationCode.values())
                .filter(seitenEnum -> seitenEnum.getCode().equals(code))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Code \"" + code + "\" is not a valid Seitenlokalisationscode")
                );
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getCodeSystem() {
        return this.codeSystem;
    }
}
