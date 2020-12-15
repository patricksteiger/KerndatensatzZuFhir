package model;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import com.opencsv.bean.CsvBindByName;
import constants.URLs;
import enums.ProcedureCategorySnomedMapping;
import helper.*;
import constants.Constants;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Prozedur implements Datablock {
    @CsvBindByName
    private String patNr;
    // OPS Prozedur kodiert
    @CsvBindByName
    private String OPS_Vollst_Prozedurenkode;
    @CsvBindByName
    private String OPS_Seitenlokalisation;
    // SNOMED Prozedur kodiert
    @CsvBindByName
    private String SNOMED_Vollst_Prozedurenkode;
    @CsvBindByName
    private String durchfuehrungsabsicht;
    @CsvBindByName
    private String durchfuehrungsdatum;
    @CsvBindByName
    private String dokumentationsdatum;
    @CsvBindByName
    private String freitextbeschreibung;
    @CsvBindByName
    private String koerperstelle;

    @Override
    public List<Resource> toFhirResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(this.getProcedure());
        return resources;
    }

    public Procedure getProcedure() {
        Procedure procedure = new Procedure();
        // TODO: ID
        //procedure.setId("id");
        // Meta
        procedure.setMeta(this.getMeta());
        // Status
        procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
        // Category, can only be set if OPS is used.
        if (Helper.checkNonEmptyString(this.getOPS_Vollst_Prozedurenkode()))
            procedure.setCategory(this.getCategory());
        // Code
        procedure.setCode(this.getCode());
        // Performed
        procedure.setPerformed(this.getPerformed());
        // BodySite (optional)
        if (Helper.checkNonEmptyString(this.getKoerperstelle()))
            procedure.setBodySite(this.getBodySites());
        // Note (optional)
        if (Helper.checkNonEmptyString(this.getFreitextbeschreibung()))
            procedure.setNote(this.getNotes());
        // Extension: RecordedDate (optional)
        if (Helper.checkNonEmptyString(this.getDokumentationsdatum()))
            procedure.addExtension(this.getRecordedDate());
        // Extension: Durchführungsabsicht (optional)
        if (Helper.checkNonEmptyString(this.getDurchfuehrungsabsicht()))
            procedure.addExtension(this.getDurchführungsabsicht());
        // Subject
        procedure.setSubject(this.getSubject());
        return procedure;
    }

    public Reference getSubject() {
        Reference subject = new Reference();
        Reference assignerRef = FhirHelper.generateSubjectAssignerReference();
        subject.setIdentifier(FhirHelper.generateIdentifier(URLs.LOCAL_PID, this.getPatNr(), assignerRef));
        return subject;
    }

    public Meta getMeta() {
        return FhirHelper.generateMeta(URLs.PROCEDURE_PROFILE_URL, Constants.SOURCE_UKU_SAP_PROZEDUR);
    }

    public CodeableConcept getCategory() {
        ProcedureCategorySnomedMapping mapping = FhirHelper.getSnomedMappingFromOps(this.getOPS_Vollst_Prozedurenkode());
        Coding categoryCode = FhirHelper.generateCoding(mapping.getCode(), URLs.SNOMED_CLINICAL_TERMS, mapping.getDisplay());
        return new CodeableConcept().addCoding(categoryCode);
    }

    public CodeableConcept getCode() {
        CodeableConcept code = new CodeableConcept();
        if (Helper.checkNonEmptyString(this.getSNOMED_Vollst_Prozedurenkode()))
            code.addCoding(this.getCodingSnomed());
        if (Helper.checkNonEmptyString(this.getOPS_Vollst_Prozedurenkode()))
            code.addCoding(this.getCodingOps());
        return code;
    }

    public Coding getCodingOps() {
        Coding ops = FhirHelper.generateCoding(this.getOPS_Vollst_Prozedurenkode(), URLs.OPS_DIMDI_SYSTEM, Constants.EMPTY_DISPLAY, Constants.VERSION_2020); // For OPS: Code, System, Version, Seitenlokalisation
        if (Helper.checkNonEmptyString(this.getOPS_Seitenlokalisation()))
            ops.addExtension(this.getSeitenlokalisation());
        return ops;
    }

    public Extension getSeitenlokalisation() {
        Extension extension = new Extension();
        extension.setUrl(URLs.OPS_SEITENLOKALISATION);
        // TODO: Korrekte Url für system?
        Type value = FhirHelper.generateCoding(this.getOPS_Seitenlokalisation(), URLs.OPS_SEITENLOKALISATION_OID, Constants.EMPTY_DISPLAY, Constants.VERSION_2020);
        extension.setValue(value);
        return extension;
    }

    public Coding getCodingSnomed() {
        return FhirHelper.generateCoding(this.getSNOMED_Vollst_Prozedurenkode(), URLs.SNOMED_CLINICAL_TERMS);
    }

    public DateTimeType getPerformed() {
        Date date = Helper.getDateFromGermanTime(this.getDurchfuehrungsdatum());
        return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
    }

    public List<CodeableConcept> getBodySites() {
        List<CodeableConcept> bodySites = new ArrayList<>();
        CodeableConcept bodySite = new CodeableConcept();
        Coding coding = FhirHelper.generateCoding(this.getKoerperstelle(), URLs.SNOMED_CLINICAL_TERMS);
        bodySite.addCoding(coding);
        bodySites.add(bodySite);
        return bodySites;
    }

    public List<Annotation> getNotes() {
        List<Annotation> notes = new ArrayList<>();
        Annotation note = new Annotation();
        note.setText(this.getFreitextbeschreibung());
        notes.add(note);
        return notes;
    }

    public Extension getRecordedDate() {
        Extension recordedDate = new Extension();
        recordedDate.setUrl(URLs.RECORDED_DATE_URL);
        Date recorded = Helper.getDateFromGermanTime(this.getDokumentationsdatum());
        DateTimeType date = new DateTimeType(recorded, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
        recordedDate.setValue(date);
        return recordedDate;
    }

    public Extension getDurchführungsabsicht() {
       Extension absicht = new Extension();
       absicht.setUrl(URLs.DURCHFUEHRUNGSABSICHT_URL);
       Coding code = FhirHelper.generateCoding(this.getDurchfuehrungsabsicht(), URLs.SNOMED_CLINICAL_TERMS);
       absicht.setValue(code);
       return absicht;
    }

    public String getPatNr() {
        return patNr;
    }

    public void setPatNr(String patNr) {
        this.patNr = patNr;
    }

    public String getOPS_Vollst_Prozedurenkode() {
        return OPS_Vollst_Prozedurenkode;
    }

    public void setOPS_Vollst_Prozedurenkode(String OPS_Vollst_Prozedurenkode) {
        this.OPS_Vollst_Prozedurenkode = OPS_Vollst_Prozedurenkode;
    }

    public String getOPS_Seitenlokalisation() {
        return OPS_Seitenlokalisation;
    }

    public void setOPS_Seitenlokalisation(String OPS_Seitenlokalisation) {
        this.OPS_Seitenlokalisation = OPS_Seitenlokalisation;
    }

    public String getSNOMED_Vollst_Prozedurenkode() {
        return SNOMED_Vollst_Prozedurenkode;
    }

    public void setSNOMED_Vollst_Prozedurenkode(String SNOMED_Vollst_Prozedurenkode) {
        this.SNOMED_Vollst_Prozedurenkode = SNOMED_Vollst_Prozedurenkode;
    }

    public String getDurchfuehrungsabsicht() {
        return durchfuehrungsabsicht;
    }

    public void setDurchfuehrungsabsicht(String durchfuehrungsabsicht) {
        this.durchfuehrungsabsicht = durchfuehrungsabsicht;
    }

    public String getDurchfuehrungsdatum() {
        return durchfuehrungsdatum;
    }

    public void setDurchfuehrungsdatum(String durchfuehrungsdatum) {
        this.durchfuehrungsdatum = durchfuehrungsdatum;
    }

    public String getDokumentationsdatum() {
        return dokumentationsdatum;
    }

    public void setDokumentationsdatum(String dokumentationsdatum) {
        this.dokumentationsdatum = dokumentationsdatum;
    }

    public String getFreitextbeschreibung() {
        return freitextbeschreibung;
    }

    public void setFreitextbeschreibung(String freitextbeschreibung) {
        this.freitextbeschreibung = freitextbeschreibung;
    }

    public String getKoerperstelle() {
        return koerperstelle;
    }

    public void setKoerperstelle(String koerperstelle) {
        this.koerperstelle = koerperstelle;
    }
}
