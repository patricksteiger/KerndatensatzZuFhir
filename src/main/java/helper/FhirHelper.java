package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.IdentifierSystem;
import enums.MIICoreLocations;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class FhirHelper {

    public static Meta generateMeta(List<String> profiles, String source, String versionId, List<Coding> securities, List<Coding> tags) {
        Meta meta = new Meta();
        if (Helper.checkNonEmptyString(source))
            meta.setSource(source);
        if (Helper.checkNonEmptyString(versionId))
            meta.setVersionId(versionId);
        profiles.stream().filter(Helper::checkNonEmptyString).forEach(meta::addProfile);
        securities.stream().filter(Objects::nonNull).forEach(meta::addSecurity);
        tags.stream().filter(Objects::nonNull).forEach(meta::addTag);
        meta.setLastUpdated(new Date());
        return meta;
    }

    public static Meta generateMeta(String profile, String source, String versionId) {
        return generateMeta(Helper.listOf(profile), source, versionId, Helper.listOf(), Helper.listOf());
    }

    public static Meta generateMeta(String profile, String source) {
        return generateMeta(profile, source, "");
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
        Identifier assignerId = FhirHelper.generateIdentifier(MIICoreLocations.UKU.name(), IdentifierSystem.NS_DIZ, null);
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
