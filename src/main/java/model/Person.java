package model;

import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResearchSubject;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public class Person implements Datablock {
    private String patNr;
    // Name
    private String vorname;
    private String nachname;
    private String familienname;
    private String vorsatzwort;
    private String praefix;
    private String art_des_praefix;
    private String geburtsname;
    // Demographie
    private String admininistratives_geschlecht;
    private String geburtsdatum;
    // Demographie - Adresse
    private String postfachnummer;
    private String postfach_wohnort;
    private String postfach_plz;
    private String postfach_land;
    private String strassenanschrift_land;
    private String strassenanschrift_plz;
    private String strassenanschrift_wohnort;
    private String strasse;
    // Demographie - Vitalstatus
    private String patient_verstorben;
    private String todeszeitpunkt;
    private String informationsquelle;
    private String letzter_lebendzeitpunkt;
    // PatientIn
    private String patient_pid;
    private String patient_pid_kontext;
    private String versichertenId_gkv;
    private String versichertennummer_pkv;
    private String institutionskennzeichen_krankenkasse;
    private String versicherungstyp;
    // ProbandIn
    private String bezeichnung_studie;
    private String subjekt_identifizierungscode;
    private String rechtsgrundlage;
    private String teilnahme_beginn;
    private String teilnahme_ende;
    private String teilnahme_status;


    @Override
    public List<Resource> toFhirResources() {
        return Helper.listOf(this.getPatient(), this.getResearchSubject(), this.getObservation());
    }

    public Patient getPatient() {
        Patient patient = new Patient();
        return patient;
    }

    public ResearchSubject getResearchSubject() {
        ResearchSubject researchSubject = new ResearchSubject();
        return researchSubject;
    }

    public Observation getObservation() {
        Observation observation = new Observation();
        return observation;
    }
}
