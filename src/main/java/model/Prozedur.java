package model;

import com.opencsv.bean.CsvBindByName;
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
        // ID
        procedure.setId("id");
        // Meta
        Meta meta = new Meta();
        procedure.setMeta(meta);
        // Status
        procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
        // Category
        CodeableConcept category = new CodeableConcept();
        procedure.setCategory(category);
        // Code
        CodeableConcept code = new CodeableConcept();
        Coding ops = new Coding(); // For OPS: Code, System, Version, Seitenlokalisation
        Coding snomed = new Coding(); // For SNOMED: Code, System
        code.addCoding(ops);
        code.addCoding(snomed);
        procedure.setCode(code);
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
