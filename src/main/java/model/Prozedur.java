package model;

import com.opencsv.bean.CsvBindByName;
import helper.Constants;
import helper.FhirHelper;
import helper.UrlHelper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
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
        DateTimeType performed = new DateTimeType();
        procedure.setPerformed(performed);
        // BodySite
        List<CodeableConcept> bodySites = new ArrayList<>();
        procedure.setBodySite(bodySites);
        // Note
        List<Annotation> notes = new ArrayList<>();
        procedure.setNote(notes);
        // Extension: RecordedDate, Durchfuehrungsabsicht
        List<Extension> extensions = new ArrayList<>();
        Extension recordedDate = new Extension();
        Extension durchfuehrungsabsicht = new Extension();
        extensions.add(recordedDate);
        extensions.add(durchfuehrungsabsicht);
        procedure.setExtension(extensions);
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
        String snomed = this.getSNOMED_Vollst_Prozedurenkode();
        if (snomed != null && !snomed.isEmpty())
            code.addCoding(this.getCodingSnomed());
        String ops = this.getOPS_Vollst_Prozedurenkode();
        if (ops != null && !ops.isEmpty())
            code.addCoding(this.getCodingOps());
        return code;
    }

    public Coding getCodingOps() {
        Coding ops = FhirHelper.generateCoding(this.getOPS_Vollst_Prozedurenkode(), UrlHelper.OPS_DIMDI_SYSTEM, Constants.EMPTY_DISPLAY, Constants.VERSION_2020); // For OPS: Code, System, Version, Seitenlokalisation
        String seite = this.getOPS_Seitenlokalisation();
        if (seite != null && !seite.isEmpty()) {
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
