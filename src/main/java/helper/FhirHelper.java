package helper;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Meta;

public class FhirHelper {
    public static Meta generateMeta(String profile) {
        Meta meta = new Meta();
        meta.addProfile(profile);
        return meta;
    }

    public static Meta generateMeta(String profile, String source) {
        Meta meta = generateMeta(profile);
        meta.setSource(source);
        return meta;
    }

    public static Coding generateCoding(String code, String system, String display, String version) {
        Coding coding = new Coding();
        coding.setCode(code);
        coding.setSystem(system);
        if (display != null && !display.isEmpty())
            coding.setDisplay(display);
        if (version != null && !version.isEmpty())
            coding.setVersion(version);
        return coding;
    }

    public static Coding generateCoding(String code, String system, String display) {
        return generateCoding(code, system, display, "");
    }

    public static Coding generateCoding(String code, String system) {
        return generateCoding(code, system, "");
    }

}
