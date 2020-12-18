package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.URLs;
import enums.MIICoreLocations;
import enums.ProcedureCategorySnomedMapping;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.TimeZone;

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
        if (Helper.checkNonEmptyString(display))
            coding.setDisplay(display);
        if (Helper.checkNonEmptyString(version))
            coding.setVersion(version);
        return coding;
    }

    public static Coding generateCoding(String code, String system, String display) {
        return generateCoding(code, system, display, "");
    }

    public static Coding generateCoding(String code, String system) {
        return generateCoding(code, system, "");
    }

    public static Identifier generateIdentifier(String value, String system, Reference assignerRef) {
        Identifier identifier = new Identifier();
        identifier.setSystem(system);
        identifier.setValue(value);
        if (assignerRef != null)
            identifier.setAssigner(assignerRef);
        return identifier;
    }

    public static Reference generateSubjectAssignerReference() {
        Reference assigner = new Reference();
        assigner.setDisplay(MIICoreLocations.UKU.toString());
        Identifier assignerId = FhirHelper.generateIdentifier(URLs.NS_DIZ, MIICoreLocations.UKU.name(), null);
        assigner.setIdentifier(assignerId);
        return assigner;
    }

    public static Extension generateExtension(String url, Type value) {
        Extension extension = new Extension();
        extension.setUrl(url);
        extension.setValue(value);
        return extension;
    }

    public static DateTimeType generateDate(Date date) {
        return new DateTimeType(date, TemporalPrecisionEnum.DAY, TimeZone.getDefault());
    }

    /**
     * Returns Snomed-mapping needed for category in procedure.
     *
     * @param ops OPS-Code. Example: "5-470"
     * @return Snomed-Mapping
     * @throws IllegalArgumentException if first character of OPS-Code is not 1,3,5,6,8,9.
     * @see "https://simplifier.net/guide/MedizininformatikInitiative-ModulProzeduren-ImplementationGuide/Terminologien"
     */
    public static ProcedureCategorySnomedMapping getSnomedMappingFromOps(String ops) {
        String code = ops;
        if (!Helper.checkNonEmptyString(code))
            code = "f";
        final char opsCode = code.charAt(0);
        switch (opsCode) {
            case '1': return ProcedureCategorySnomedMapping.DIAGNOSTIC;
            case '3': return ProcedureCategorySnomedMapping.IMAGING;
            case '5': return ProcedureCategorySnomedMapping.SURGICAL;
            case '6': return ProcedureCategorySnomedMapping.ADMINISTRATION_OF_MEDICINE;
            case '8': return ProcedureCategorySnomedMapping.THERAPEUTIC;
            case '9': return ProcedureCategorySnomedMapping.OTHER;
            default: throw new IllegalArgumentException("Code '" + ops + "' is not valid OPS-Code.");
        }
    }
}
