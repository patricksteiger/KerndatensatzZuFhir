package helper;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.IdentifierSystem;
import constants.ReferenceType;
import enums.IdentifierTypeCode;
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

    public static Identifier generateIdentifier(String value, String system, CodeableConcept type, Reference assignerRef, Identifier.IdentifierUse use) {
        Identifier identifier = new Identifier();
        if (Helper.checkNonEmptyString(value))
            identifier.setValue(value);
        if (Helper.checkNonEmptyString(system))
            identifier.setSystem(system);
        if (type != null)
            identifier.setType(type);
        if (assignerRef != null)
            identifier.setAssigner(assignerRef);
        if (use != null)
            identifier.setUse(use);
        return identifier;
    }

    public static Identifier generateIdentifier(String value, String system, CodeableConcept type, Reference assignerRef) {
        return generateIdentifier(value, system, type, assignerRef, null);
    }

    public static Identifier generateIdentifier(String value, String system, CodeableConcept type) {
        return generateIdentifier(value, system, type, null);
    }

    public static Identifier generateIdentifier(String value, String system, Reference assignerRef) {
        return generateIdentifier(value, system, null, assignerRef);
    }

    public static Identifier generateIdentifier(String value, String system) {
        return generateIdentifier(value, system, null, null);
    }

    public static Reference getUKUAssignerReference() {
        Identifier assignerId = FhirHelper.generateIdentifier(MIICoreLocations.UKU.name(), IdentifierSystem.NS_DIZ);
        return FhirHelper.generateReference(assignerId, MIICoreLocations.UKU.toString());
    }

    public static Reference getOrganizationAssignerReference() {
        String type = ReferenceType.ORGANIZATION;
        // Identifier
        Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
        String system = IdentifierSystem.ORGANIZATION_REFERENCE_ID;
        IdentifierTypeCode identifierTypeCode = IdentifierTypeCode.XX;
        String identifierSystem = IdentifierSystem.TYPE_CODE;
        Coding coding = FhirHelper.generateCoding(identifierTypeCode.getCode(), identifierSystem, identifierTypeCode.getDisplay());
        CodeableConcept identifierType = new CodeableConcept().addCoding(coding);
        // FIXME: What is the value of the identifier?
        String identifierValue = "";
        Identifier identifier = FhirHelper.generateIdentifier(identifierValue, system, identifierType, null, use);
        return FhirHelper.generateReference("", type, identifier, "");
    }

    public static Reference generateReference(String ref, String type, Identifier identifier, String display) {
        Reference reference = new Reference();
        if (Helper.checkNonEmptyString(ref))
            reference.setReference(ref);
        if (Helper.checkNonEmptyString(type))
            reference.setType(type);
        if (identifier != null)
            reference.setIdentifier(identifier);
        if (Helper.checkNonEmptyString(display))
            reference.setDisplay(display);
        return reference;
    }

    public static Reference generateReference(Identifier identifier, String display) {
        return generateReference("", "", identifier, display);
    }

    public static Reference generateReference(Identifier identifier) {
        return generateReference(identifier, "");
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
