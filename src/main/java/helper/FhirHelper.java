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
}
