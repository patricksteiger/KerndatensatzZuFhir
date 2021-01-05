package model;

import constants.*;
import enums.VersichertenCode;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

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
        // Meta
        patient.setMeta(this.getMeta());
        // Identifier - GKV
        if (Helper.checkNonEmptyString(this.getVersichertenId_gkv()))
            patient.addIdentifier(this.getGKV());
        if (Helper.checkNonEmptyString(this.getPatient_pid()))
            patient.addIdentifier(this.getPID());
        return patient;
    }

    public Meta getMeta() {
        return FhirHelper.generateMeta(MetaProfile.PATIENT, MetaSource.PATIENT, MetaVersionId.PATIENT);
    }

    public Identifier getGKV() {
        VersichertenCode gkv = VersichertenCode.GKV;
        Coding gkvCoding = FhirHelper.generateCoding(gkv.getCode(), CodingSystem.VERSICHERTEN_ID_GKV, gkv.getDisplay());
        CodeableConcept type = new CodeableConcept().addCoding(gkvCoding);
        // Still needs VersicherungsReference
        return FhirHelper.generateIdentifier(this.getVersichertenId_gkv(), IdentifierSystem.VERSICHERTEN_ID_GKV, type);
    }

    public Identifier getPID() {
        Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
        String system = IdentifierSystem.PID;
        String value = this.getPatient_pid();
        Coding pidCoding = FhirHelper.generateCoding("MR", CodingSystem.PID);
        return FhirHelper.generateIdentifier(value, system, new CodeableConcept().addCoding(pidCoding), FhirHelper.getUKUAssignerReference(), use);
    }

    public ResearchSubject getResearchSubject() {
        ResearchSubject researchSubject = new ResearchSubject();
        return researchSubject;
    }

    public Observation getObservation() {
        Observation observation = new Observation();
        return observation;
    }
    public String getPatNr() {
        return patNr;
    }

    public void setPatNr(String patNr) {
        this.patNr = patNr;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getFamilienname() {
        return familienname;
    }

    public void setFamilienname(String familienname) {
        this.familienname = familienname;
    }

    public String getVorsatzwort() {
        return vorsatzwort;
    }

    public void setVorsatzwort(String vorsatzwort) {
        this.vorsatzwort = vorsatzwort;
    }

    public String getPraefix() {
        return praefix;
    }

    public void setPraefix(String praefix) {
        this.praefix = praefix;
    }

    public String getArt_des_praefix() {
        return art_des_praefix;
    }

    public void setArt_des_praefix(String art_des_praefix) {
        this.art_des_praefix = art_des_praefix;
    }

    public String getGeburtsname() {
        return geburtsname;
    }

    public void setGeburtsname(String geburtsname) {
        this.geburtsname = geburtsname;
    }

    public String getAdmininistratives_geschlecht() {
        return admininistratives_geschlecht;
    }

    public void setAdmininistratives_geschlecht(String admininistratives_geschlecht) {
        this.admininistratives_geschlecht = admininistratives_geschlecht;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getPostfachnummer() {
        return postfachnummer;
    }

    public void setPostfachnummer(String postfachnummer) {
        this.postfachnummer = postfachnummer;
    }

    public String getPostfach_wohnort() {
        return postfach_wohnort;
    }

    public void setPostfach_wohnort(String postfach_wohnort) {
        this.postfach_wohnort = postfach_wohnort;
    }

    public String getPostfach_plz() {
        return postfach_plz;
    }

    public void setPostfach_plz(String postfach_plz) {
        this.postfach_plz = postfach_plz;
    }

    public String getPostfach_land() {
        return postfach_land;
    }

    public void setPostfach_land(String postfach_land) {
        this.postfach_land = postfach_land;
    }

    public String getStrassenanschrift_land() {
        return strassenanschrift_land;
    }

    public void setStrassenanschrift_land(String strassenanschrift_land) {
        this.strassenanschrift_land = strassenanschrift_land;
    }

    public String getStrassenanschrift_plz() {
        return strassenanschrift_plz;
    }

    public void setStrassenanschrift_plz(String strassenanschrift_plz) {
        this.strassenanschrift_plz = strassenanschrift_plz;
    }

    public String getStrassenanschrift_wohnort() {
        return strassenanschrift_wohnort;
    }

    public void setStrassenanschrift_wohnort(String strassenanschrift_wohnort) {
        this.strassenanschrift_wohnort = strassenanschrift_wohnort;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPatient_verstorben() {
        return patient_verstorben;
    }

    public void setPatient_verstorben(String patient_verstorben) {
        this.patient_verstorben = patient_verstorben;
    }

    public String getTodeszeitpunkt() {
        return todeszeitpunkt;
    }

    public void setTodeszeitpunkt(String todeszeitpunkt) {
        this.todeszeitpunkt = todeszeitpunkt;
    }

    public String getInformationsquelle() {
        return informationsquelle;
    }

    public void setInformationsquelle(String informationsquelle) {
        this.informationsquelle = informationsquelle;
    }

    public String getLetzter_lebendzeitpunkt() {
        return letzter_lebendzeitpunkt;
    }

    public void setLetzter_lebendzeitpunkt(String letzter_lebendzeitpunkt) {
        this.letzter_lebendzeitpunkt = letzter_lebendzeitpunkt;
    }

    public String getPatient_pid() {
        return patient_pid;
    }

    public void setPatient_pid(String patient_pid) {
        this.patient_pid = patient_pid;
    }

    public String getPatient_pid_kontext() {
        return patient_pid_kontext;
    }

    public void setPatient_pid_kontext(String patient_pid_kontext) {
        this.patient_pid_kontext = patient_pid_kontext;
    }

    public String getVersichertenId_gkv() {
        return versichertenId_gkv;
    }

    public void setVersichertenId_gkv(String versichertenId_gkv) {
        this.versichertenId_gkv = versichertenId_gkv;
    }

    public String getVersichertennummer_pkv() {
        return versichertennummer_pkv;
    }

    public void setVersichertennummer_pkv(String versichertennummer_pkv) {
        this.versichertennummer_pkv = versichertennummer_pkv;
    }

    public String getInstitutionskennzeichen_krankenkasse() {
        return institutionskennzeichen_krankenkasse;
    }

    public void setInstitutionskennzeichen_krankenkasse(String institutionskennzeichen_krankenkasse) {
        this.institutionskennzeichen_krankenkasse = institutionskennzeichen_krankenkasse;
    }

    public String getVersicherungstyp() {
        return versicherungstyp;
    }

    public void setVersicherungstyp(String versicherungstyp) {
        this.versicherungstyp = versicherungstyp;
    }

    public String getBezeichnung_studie() {
        return bezeichnung_studie;
    }

    public void setBezeichnung_studie(String bezeichnung_studie) {
        this.bezeichnung_studie = bezeichnung_studie;
    }

    public String getSubjekt_identifizierungscode() {
        return subjekt_identifizierungscode;
    }

    public void setSubjekt_identifizierungscode(String subjekt_identifizierungscode) {
        this.subjekt_identifizierungscode = subjekt_identifizierungscode;
    }

    public String getRechtsgrundlage() {
        return rechtsgrundlage;
    }

    public void setRechtsgrundlage(String rechtsgrundlage) {
        this.rechtsgrundlage = rechtsgrundlage;
    }

    public String getTeilnahme_beginn() {
        return teilnahme_beginn;
    }

    public void setTeilnahme_beginn(String teilnahme_beginn) {
        this.teilnahme_beginn = teilnahme_beginn;
    }

    public String getTeilnahme_ende() {
        return teilnahme_ende;
    }

    public void setTeilnahme_ende(String teilnahme_ende) {
        this.teilnahme_ende = teilnahme_ende;
    }

    public String getTeilnahme_status() {
        return teilnahme_status;
    }

    public void setTeilnahme_status(String teilnahme_status) {
        this.teilnahme_status = teilnahme_status;
    }

}
