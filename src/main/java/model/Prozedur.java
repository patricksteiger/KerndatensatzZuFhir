package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.URLs;
import enums.DurchfuehrungsabsichtCode;
import enums.ProcedureCategorySnomedMapping;
import enums.SeitenlokalisationCode;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        // ID
        //procedure.setId(Constants.UKU_FHIR_PROCEDURE);
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
        // Extension: RecordedDate (optional), only needed if Dokumentationsdatum != Durchfuehrungsdatum
        if (Helper.checkNonEmptyString(this.getDokumentationsdatum())
                && !this.getDokumentationsdatum().equals(this.getDurchfuehrungsdatum()))
            procedure.addExtension(this.getRecordedDate());
        // Extension: Durchfuehrungsabsicht (optional)
        if (Helper.checkNonEmptyString(this.getKernDurchfuehrungsabsicht()))
            procedure.addExtension(this.getDurchfuehrungsabsicht());
        // Subject
        if (Helper.checkNonEmptyString(this.getPatNr()))
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
        return FhirHelper.generateMeta(URLs.PROCEDURE_PROFILE_URL, Constants.SOURCE_UKU_SAP_PROZEDUR, Constants.UKU_FHIR_PROCEDURE);
    }

    /**
     * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/81934/"
     */
    public CodeableConcept getCategory() {
        ProcedureCategorySnomedMapping mapping = ProcedureCategorySnomedMapping.getSnomedMappingByOpsCode(this.getOPS_Vollst_Prozedurenkode());
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

    /**
     * @see "https://simplifier.net/basisprofil-de-r4/extension-seitenlokalisation"
     */
    public Extension getSeitenlokalisation() {
        SeitenlokalisationCode seitenCode = SeitenlokalisationCode.getSeitenlokalisationByCode(this.getOPS_Seitenlokalisation());
        Coding value = FhirHelper.generateCoding(seitenCode.getCode(), seitenCode.getCodeSystem(), seitenCode.getDisplay());
        return FhirHelper.generateExtension(URLs.OPS_SEITENLOKALISATION, value);
    }

    public Coding getCodingSnomed() {
        return FhirHelper.generateCoding(this.getSNOMED_Vollst_Prozedurenkode(), URLs.SNOMED_CLINICAL_TERMS);
    }

    public DateTimeType getPerformed() {
        Date date = Helper.getDateFromGermanTime(this.getDurchfuehrungsdatum());
        return FhirHelper.generateDate(date);
    }

    /**
     * @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/80349/"
     */
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
        Date recorded = Helper.getDateFromGermanTime(this.getDokumentationsdatum());
        DateTimeType date = FhirHelper.generateDate(recorded);
        return FhirHelper.generateExtension(URLs.RECORDED_DATE_URL, date);
    }

    /**
     * @see "https://simplifier.net/medizininformatikinitiative-modulprozeduren/durchfuehrungsabsicht"
     */
    public Extension getDurchfuehrungsabsicht() {
        DurchfuehrungsabsichtCode durchfuehrungsabsichtCode = DurchfuehrungsabsichtCode.getDurchfuehrungsabsichtByCode(this.getKernDurchfuehrungsabsicht());
        Coding code = FhirHelper.generateCoding(durchfuehrungsabsichtCode.getCode(), URLs.SNOMED_CLINICAL_TERMS, durchfuehrungsabsichtCode.getDisplay());
        return FhirHelper.generateExtension(URLs.DURCHFUEHRUNGSABSICHT_URL, code);
    }

    public void setDurchfuehrungsabsicht(String durchfuehrungsabsicht) {
        this.durchfuehrungsabsicht = durchfuehrungsabsicht;
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

    public String getKernDurchfuehrungsabsicht() {
        return durchfuehrungsabsicht;
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
