package model;

import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public class Medikation implements Datablock {
  private String patNr;
  // Medikationseintrag
  private String identifikation;
  private String status;
  private String hinweis;
  private String behandlungsgrund;
  private String bezug_verordnung;
  private String bezug_abgabe;
  private String datum_eintrag;
  // Arzneimittel/Wirkstoff/Rezeptur
  private String darreichungsform;
  // Arzneimittel/Wirkstoff/Rezeptur - Arzneimittelprodukt
  private String arzneimittel_name;
  private String arzneimittel_code;
  private String arzneimittel_wirkstaerke;
  // Arzneimittel/Wirkstoff/Rezeptur - Rezeptur
  private String rezeptur_freitextzeile;
  // Arzneimittel/Wirkstoff/Rezeptur - Wirkstoff
  private String wirkstoff_name_allgemein;
  private String wirkstoff_code_allgemein;
  private String wirkstoff_name_aktiv;
  private String wirkstoff_code_aktiv;
  private String wirkstoff_menge;
  // Einnahmedauer
  private String einnahme_startzeitpunkt;
  private String einnahme_endzeitpunkt;
  private String einnahme_dauer;
  // Dosierung (Freitext)
  private String dosierung_freitext;
  // Dosierung (strukturiert) (Dosierungsinformationen, Angaben zur Dosierung)
  private String dosierung_reihenfolge;
  private String dosierung_einnahme_bei_bedarf;
  private String dosierung_art_der_anwendung;
  private String dosierung_dosis;
  // Dosierung (strukturiert) - Zeitangabe
  private String dosierung_zeitpunkt;
  // Dosierung (strukturiert) - Zeitangabe - Ereignisbezogene Wiederholung
  private String dosierung_ereignis;
  private String dosierung_offset;
  // Dosierung (strukturiert) - Zeitangabe - Periodisches Intervall
  private String dosierung_phase;
  private String dosierung_periode;
  // Autor/Informant des Eintrags
  private String organisationsname;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(
        this.getMedication(), this.getMedicationAdministration(), this.getMedicationStatement());
  }

  public Medication getMedication() {
    Medication medication = new Medication();
    return medication;
  }

  public MedicationAdministration getMedicationAdministration() {
    MedicationAdministration medicationAdministration = new MedicationAdministration();
    return medicationAdministration;
  }

  public MedicationStatement getMedicationStatement() {
    MedicationStatement medicationStatement = new MedicationStatement();
    return medicationStatement;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getIdentifikation() {
    return identifikation;
  }

  public void setIdentifikation(String identifikation) {
    this.identifikation = identifikation;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getHinweis() {
    return hinweis;
  }

  public void setHinweis(String hinweis) {
    this.hinweis = hinweis;
  }

  public String getBehandlungsgrund() {
    return behandlungsgrund;
  }

  public void setBehandlungsgrund(String behandlungsgrund) {
    this.behandlungsgrund = behandlungsgrund;
  }

  public String getBezug_verordnung() {
    return bezug_verordnung;
  }

  public void setBezug_verordnung(String bezug_verordnung) {
    this.bezug_verordnung = bezug_verordnung;
  }

  public String getBezug_abgabe() {
    return bezug_abgabe;
  }

  public void setBezug_abgabe(String bezug_abgabe) {
    this.bezug_abgabe = bezug_abgabe;
  }

  public String getDatum_eintrag() {
    return datum_eintrag;
  }

  public void setDatum_eintrag(String datum_eintrag) {
    this.datum_eintrag = datum_eintrag;
  }

  public String getDarreichungsform() {
    return darreichungsform;
  }

  public void setDarreichungsform(String darreichungsform) {
    this.darreichungsform = darreichungsform;
  }

  public String getArzneimittel_name() {
    return arzneimittel_name;
  }

  public void setArzneimittel_name(String arzneimittel_name) {
    this.arzneimittel_name = arzneimittel_name;
  }

  public String getArzneimittel_code() {
    return arzneimittel_code;
  }

  public void setArzneimittel_code(String arzneimittel_code) {
    this.arzneimittel_code = arzneimittel_code;
  }

  public String getArzneimittel_wirkstaerke() {
    return arzneimittel_wirkstaerke;
  }

  public void setArzneimittel_wirkstaerke(String arzneimittel_wirkstaerke) {
    this.arzneimittel_wirkstaerke = arzneimittel_wirkstaerke;
  }

  public String getRezeptur_freitextzeile() {
    return rezeptur_freitextzeile;
  }

  public void setRezeptur_freitextzeile(String rezeptur_freitextzeile) {
    this.rezeptur_freitextzeile = rezeptur_freitextzeile;
  }

  public String getWirkstoff_name_allgemein() {
    return wirkstoff_name_allgemein;
  }

  public void setWirkstoff_name_allgemein(String wirkstoff_name_allgemein) {
    this.wirkstoff_name_allgemein = wirkstoff_name_allgemein;
  }

  public String getWirkstoff_code_allgemein() {
    return wirkstoff_code_allgemein;
  }

  public void setWirkstoff_code_allgemein(String wirkstoff_code_allgemein) {
    this.wirkstoff_code_allgemein = wirkstoff_code_allgemein;
  }

  public String getWirkstoff_name_aktiv() {
    return wirkstoff_name_aktiv;
  }

  public void setWirkstoff_name_aktiv(String wirkstoff_name_aktiv) {
    this.wirkstoff_name_aktiv = wirkstoff_name_aktiv;
  }

  public String getWirkstoff_code_aktiv() {
    return wirkstoff_code_aktiv;
  }

  public void setWirkstoff_code_aktiv(String wirkstoff_code_aktiv) {
    this.wirkstoff_code_aktiv = wirkstoff_code_aktiv;
  }

  public String getWirkstoff_menge() {
    return wirkstoff_menge;
  }

  public void setWirkstoff_menge(String wirkstoff_menge) {
    this.wirkstoff_menge = wirkstoff_menge;
  }

  public String getEinnahme_startzeitpunkt() {
    return einnahme_startzeitpunkt;
  }

  public void setEinnahme_startzeitpunkt(String einnahme_startzeitpunkt) {
    this.einnahme_startzeitpunkt = einnahme_startzeitpunkt;
  }

  public String getEinnahme_endzeitpunkt() {
    return einnahme_endzeitpunkt;
  }

  public void setEinnahme_endzeitpunkt(String einnahme_endzeitpunkt) {
    this.einnahme_endzeitpunkt = einnahme_endzeitpunkt;
  }

  public String getEinnahme_dauer() {
    return einnahme_dauer;
  }

  public void setEinnahme_dauer(String einnahme_dauer) {
    this.einnahme_dauer = einnahme_dauer;
  }

  public String getDosierung_freitext() {
    return dosierung_freitext;
  }

  public void setDosierung_freitext(String dosierung_freitext) {
    this.dosierung_freitext = dosierung_freitext;
  }

  public String getDosierung_reihenfolge() {
    return dosierung_reihenfolge;
  }

  public void setDosierung_reihenfolge(String dosierung_reihenfolge) {
    this.dosierung_reihenfolge = dosierung_reihenfolge;
  }

  public String getDosierung_einnahme_bei_bedarf() {
    return dosierung_einnahme_bei_bedarf;
  }

  public void setDosierung_einnahme_bei_bedarf(String dosierung_einnahme_bei_bedarf) {
    this.dosierung_einnahme_bei_bedarf = dosierung_einnahme_bei_bedarf;
  }

  public String getDosierung_art_der_anwendung() {
    return dosierung_art_der_anwendung;
  }

  public void setDosierung_art_der_anwendung(String dosierung_art_der_anwendung) {
    this.dosierung_art_der_anwendung = dosierung_art_der_anwendung;
  }

  public String getDosierung_dosis() {
    return dosierung_dosis;
  }

  public void setDosierung_dosis(String dosierung_dosis) {
    this.dosierung_dosis = dosierung_dosis;
  }

  public String getDosierung_zeitpunkt() {
    return dosierung_zeitpunkt;
  }

  public void setDosierung_zeitpunkt(String dosierung_zeitpunkt) {
    this.dosierung_zeitpunkt = dosierung_zeitpunkt;
  }

  public String getDosierung_ereignis() {
    return dosierung_ereignis;
  }

  public void setDosierung_ereignis(String dosierung_ereignis) {
    this.dosierung_ereignis = dosierung_ereignis;
  }

  public String getDosierung_offset() {
    return dosierung_offset;
  }

  public void setDosierung_offset(String dosierung_offset) {
    this.dosierung_offset = dosierung_offset;
  }

  public String getDosierung_phase() {
    return dosierung_phase;
  }

  public void setDosierung_phase(String dosierung_phase) {
    this.dosierung_phase = dosierung_phase;
  }

  public String getDosierung_periode() {
    return dosierung_periode;
  }

  public void setDosierung_periode(String dosierung_periode) {
    this.dosierung_periode = dosierung_periode;
  }

  public String getOrganisationsname() {
    return organisationsname;
  }

  public void setOrganisationsname(String organisationsname) {
    this.organisationsname = organisationsname;
  }
}
