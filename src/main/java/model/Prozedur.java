package model;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import com.opencsv.bean.CsvBindByName;
import helper.Constants;
import helper.FhirHelper;
import helper.Helper;
import helper.UrlHelper;
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
        // Category
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
        Reference subject = new Reference();
        procedure.setSubject(subject);
        return procedure;
    }

    public Meta getMeta() {
        Meta meta = FhirHelper.generateMeta(UrlHelper.PROCEDURE_PROFILE_URL);
        return meta;
    }

    public CodeableConcept getCategory() {
        Coding categoryCode = FhirHelper.generateCoding(this.getSNOMED_Vollst_Prozedurenkode(), UrlHelper.SNOMED_CLINICAL_TERMS);
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
        Coding ops = FhirHelper.generateCoding(this.getOPS_Vollst_Prozedurenkode(), UrlHelper.OPS_DIMDI_SYSTEM, Constants.EMPTY_DISPLAY, Constants.VERSION_2020); // For OPS: Code, System, Version, Seitenlokalisation
        String seite = this.getOPS_Seitenlokalisation();
        if (Helper.checkNonEmptyString(seite)) {
            Extension extension = new Extension();
            extension.setUrl(UrlHelper.OPS_SEITENLOKALISATION);
            // TODO: Korrekte Url für system?
            Type value = FhirHelper.generateCoding(seite, UrlHelper.OPS_SEITENLOKALISATION, Constants.EMPTY_DISPLAY, Constants.VERSION_2020);
            extension.setValue(value);
            ops.addExtension(extension);
        }
        return ops;
    }

    public Coding getCodingSnomed() {
        Coding snomed = FhirHelper.generateCoding(this.getSNOMED_Vollst_Prozedurenkode(), UrlHelper.SNOMED_CLINICAL_TERMS);
        return snomed;
    }

    public DateTimeType getPerformed() {
        Date date = Helper.getDateFromGermanTime(this.getDurchfuehrungsdatum());
        return new DateTimeType(date, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
    }

    public List<CodeableConcept> getBodySites() {
        List<CodeableConcept> bodySites = new ArrayList<>();
        CodeableConcept bodySite = new CodeableConcept();
        Coding coding = FhirHelper.generateCoding(this.getKoerperstelle(), UrlHelper.BODYSITE_URL);
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
        recordedDate.setUrl(UrlHelper.RECORDED_DATE_URL);
        Date recorded = Helper.getDateFromGermanTime(this.getDokumentationsdatum());
        DateTimeType date = new DateTimeType(recorded, TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
        recordedDate.setValue(date);
        return recordedDate;
    }

    public Extension getDurchführungsabsicht() {
       Extension absicht = new Extension();
       absicht.setUrl(UrlHelper.DURCHFUEHRUNGSABSICHT_URL);
       Coding code = FhirHelper.generateCoding(this.getDurchfuehrungsabsicht(), UrlHelper.DURCHFUEHRUNGSABSICHT_URL);
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
