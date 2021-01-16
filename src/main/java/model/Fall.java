package model;

import constants.MetaProfile;
import constants.MetaSource;
import constants.MetaVersionId;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

public class Fall implements Datablock {
  private String patNr;
  // Einrichtungskontakt
  private String einrichtungskontakt_ebene;
  private String einrichtungskontakt_klasse;
  private String einrichtungskontakt_patienten_identifikator;
  private String einrichtungskontakt_aufnahmenummer;
  private String einrichtungskontakt_aufnahmeanlass;
  private String einrichtungskontakt_augnahmegrund;
  private String einrichtungskontakt_beginndatum;
  private String einrichtungskontakt_enddatum;
  private String einrichtungskontakt_entlassungsgrund;
  // Abteilungskontakt
  private String abteilungskontakt_ebene;
  private String abteilungskontakt_klasse;
  private String abteilungskontakt_patienten_identifikator;
  private String abteilungskontakt_augnahmenummer;
  private String abteilungskontakt_fachabteilungsschluessel;
  private String abteilungskontakt_beginndatum;
  private String abteilungskontakt_enddatum;
  // Versorgungsstellenkontakt
  private String versorgungsstellenkontakt_ebene;
  private String versorgungsstellenkontakt_klasse;
  private String versorgungsstellenkontakt_patienten_identifikator;
  private String versorgungsstellenkontakt_aufnahmenummer;
  private String versorgungsstellenkontakt_beginndatum;
  private String versorgungsstellenkontakt_enddatum;
  // Organisationseinheit - Einrichtung
  private String einrichtungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung
  private String abteilungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung - Versorgungsstelle
  private String versorgungsstellenidentifikator;
  // Abrechnungsfall
  private String abrechnungsfallnummer;
  private String abrechnungsfall_startdatum;
  private String abrechnungsfall_enddatum;
  private String abrechnungsfall_zieleinrichtung;
  private String abrechnungsfall_aufnahmenummer;
  private String abrechnungsfall_fallzusammenfuehrung;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getEncounter(), this.getOrganization());
  }

  public Encounter getEncounter() {
    Encounter encounter = new Encounter();
    encounter.setMeta(this.getEncounterMeta());
    return encounter;
  }

  public Organization getOrganization() {
    Organization organization = new Organization();
    return organization;
  }

  public Meta getEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_ENCOUNTER;
    String versionId = MetaVersionId.FALL_ENCOUNTER;
    return FhirHelper.generateMeta(profile, source, versionId);
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getEinrichtungskontakt_ebene() {
    return einrichtungskontakt_ebene;
  }

  public void setEinrichtungskontakt_ebene(String einrichtungskontakt_ebene) {
    this.einrichtungskontakt_ebene = einrichtungskontakt_ebene;
  }

  public String getEinrichtungskontakt_klasse() {
    return einrichtungskontakt_klasse;
  }

  public void setEinrichtungskontakt_klasse(String einrichtungskontakt_klasse) {
    this.einrichtungskontakt_klasse = einrichtungskontakt_klasse;
  }

  public String getEinrichtungskontakt_patienten_identifikator() {
    return einrichtungskontakt_patienten_identifikator;
  }

  public void setEinrichtungskontakt_patienten_identifikator(
      String einrichtungskontakt_patienten_identifikator) {
    this.einrichtungskontakt_patienten_identifikator = einrichtungskontakt_patienten_identifikator;
  }

  public String getEinrichtungskontakt_aufnahmenummer() {
    return einrichtungskontakt_aufnahmenummer;
  }

  public void setEinrichtungskontakt_aufnahmenummer(String einrichtungskontakt_aufnahmenummer) {
    this.einrichtungskontakt_aufnahmenummer = einrichtungskontakt_aufnahmenummer;
  }

  public String getEinrichtungskontakt_aufnahmeanlass() {
    return einrichtungskontakt_aufnahmeanlass;
  }

  public void setEinrichtungskontakt_aufnahmeanlass(String einrichtungskontakt_aufnahmeanlass) {
    this.einrichtungskontakt_aufnahmeanlass = einrichtungskontakt_aufnahmeanlass;
  }

  public String getEinrichtungskontakt_augnahmegrund() {
    return einrichtungskontakt_augnahmegrund;
  }

  public void setEinrichtungskontakt_augnahmegrund(String einrichtungskontakt_augnahmegrund) {
    this.einrichtungskontakt_augnahmegrund = einrichtungskontakt_augnahmegrund;
  }

  public String getEinrichtungskontakt_beginndatum() {
    return einrichtungskontakt_beginndatum;
  }

  public void setEinrichtungskontakt_beginndatum(String einrichtungskontakt_beginndatum) {
    this.einrichtungskontakt_beginndatum = einrichtungskontakt_beginndatum;
  }

  public String getEinrichtungskontakt_enddatum() {
    return einrichtungskontakt_enddatum;
  }

  public void setEinrichtungskontakt_enddatum(String einrichtungskontakt_enddatum) {
    this.einrichtungskontakt_enddatum = einrichtungskontakt_enddatum;
  }

  public String getEinrichtungskontakt_entlassungsgrund() {
    return einrichtungskontakt_entlassungsgrund;
  }

  public void setEinrichtungskontakt_entlassungsgrund(String einrichtungskontakt_entlassungsgrund) {
    this.einrichtungskontakt_entlassungsgrund = einrichtungskontakt_entlassungsgrund;
  }

  public String getAbteilungskontakt_ebene() {
    return abteilungskontakt_ebene;
  }

  public void setAbteilungskontakt_ebene(String abteilungskontakt_ebene) {
    this.abteilungskontakt_ebene = abteilungskontakt_ebene;
  }

  public String getAbteilungskontakt_klasse() {
    return abteilungskontakt_klasse;
  }

  public void setAbteilungskontakt_klasse(String abteilungskontakt_klasse) {
    this.abteilungskontakt_klasse = abteilungskontakt_klasse;
  }

  public String getAbteilungskontakt_patienten_identifikator() {
    return abteilungskontakt_patienten_identifikator;
  }

  public void setAbteilungskontakt_patienten_identifikator(
      String abteilungskontakt_patienten_identifikator) {
    this.abteilungskontakt_patienten_identifikator = abteilungskontakt_patienten_identifikator;
  }

  public String getAbteilungskontakt_augnahmenummer() {
    return abteilungskontakt_augnahmenummer;
  }

  public void setAbteilungskontakt_augnahmenummer(String abteilungskontakt_augnahmenummer) {
    this.abteilungskontakt_augnahmenummer = abteilungskontakt_augnahmenummer;
  }

  public String getAbteilungskontakt_fachabteilungsschluessel() {
    return abteilungskontakt_fachabteilungsschluessel;
  }

  public void setAbteilungskontakt_fachabteilungsschluessel(
      String abteilungskontakt_fachabteilungsschluessel) {
    this.abteilungskontakt_fachabteilungsschluessel = abteilungskontakt_fachabteilungsschluessel;
  }

  public String getAbteilungskontakt_beginndatum() {
    return abteilungskontakt_beginndatum;
  }

  public void setAbteilungskontakt_beginndatum(String abteilungskontakt_beginndatum) {
    this.abteilungskontakt_beginndatum = abteilungskontakt_beginndatum;
  }

  public String getAbteilungskontakt_enddatum() {
    return abteilungskontakt_enddatum;
  }

  public void setAbteilungskontakt_enddatum(String abteilungskontakt_enddatum) {
    this.abteilungskontakt_enddatum = abteilungskontakt_enddatum;
  }

  public String getVersorgungsstellenkontakt_ebene() {
    return versorgungsstellenkontakt_ebene;
  }

  public void setVersorgungsstellenkontakt_ebene(String versorgungsstellenkontakt_ebene) {
    this.versorgungsstellenkontakt_ebene = versorgungsstellenkontakt_ebene;
  }

  public String getVersorgungsstellenkontakt_klasse() {
    return versorgungsstellenkontakt_klasse;
  }

  public void setVersorgungsstellenkontakt_klasse(String versorgungsstellenkontakt_klasse) {
    this.versorgungsstellenkontakt_klasse = versorgungsstellenkontakt_klasse;
  }

  public String getVersorgungsstellenkontakt_patienten_identifikator() {
    return versorgungsstellenkontakt_patienten_identifikator;
  }

  public void setVersorgungsstellenkontakt_patienten_identifikator(
      String versorgungsstellenkontakt_patienten_identifikator) {
    this.versorgungsstellenkontakt_patienten_identifikator =
        versorgungsstellenkontakt_patienten_identifikator;
  }

  public String getVersorgungsstellenkontakt_aufnahmenummer() {
    return versorgungsstellenkontakt_aufnahmenummer;
  }

  public void setVersorgungsstellenkontakt_aufnahmenummer(
      String versorgungsstellenkontakt_aufnahmenummer) {
    this.versorgungsstellenkontakt_aufnahmenummer = versorgungsstellenkontakt_aufnahmenummer;
  }

  public String getVersorgungsstellenkontakt_beginndatum() {
    return versorgungsstellenkontakt_beginndatum;
  }

  public void setVersorgungsstellenkontakt_beginndatum(
      String versorgungsstellenkontakt_beginndatum) {
    this.versorgungsstellenkontakt_beginndatum = versorgungsstellenkontakt_beginndatum;
  }

  public String getVersorgungsstellenkontakt_enddatum() {
    return versorgungsstellenkontakt_enddatum;
  }

  public void setVersorgungsstellenkontakt_enddatum(String versorgungsstellenkontakt_enddatum) {
    this.versorgungsstellenkontakt_enddatum = versorgungsstellenkontakt_enddatum;
  }

  public String getEinrichtungsidentifikator() {
    return einrichtungsidentifikator;
  }

  public void setEinrichtungsidentifikator(String einrichtungsidentifikator) {
    this.einrichtungsidentifikator = einrichtungsidentifikator;
  }

  public String getAbteilungsidentifikator() {
    return abteilungsidentifikator;
  }

  public void setAbteilungsidentifikator(String abteilungsidentifikator) {
    this.abteilungsidentifikator = abteilungsidentifikator;
  }

  public String getVersorgungsstellenidentifikator() {
    return versorgungsstellenidentifikator;
  }

  public void setVersorgungsstellenidentifikator(String versorgungsstellenidentifikator) {
    this.versorgungsstellenidentifikator = versorgungsstellenidentifikator;
  }

  public String getAbrechnungsfallnummer() {
    return abrechnungsfallnummer;
  }

  public void setAbrechnungsfallnummer(String abrechnungsfallnummer) {
    this.abrechnungsfallnummer = abrechnungsfallnummer;
  }

  public String getAbrechnungsfall_startdatum() {
    return abrechnungsfall_startdatum;
  }

  public void setAbrechnungsfall_startdatum(String abrechnungsfall_startdatum) {
    this.abrechnungsfall_startdatum = abrechnungsfall_startdatum;
  }

  public String getAbrechnungsfall_enddatum() {
    return abrechnungsfall_enddatum;
  }

  public void setAbrechnungsfall_enddatum(String abrechnungsfall_enddatum) {
    this.abrechnungsfall_enddatum = abrechnungsfall_enddatum;
  }

  public String getAbrechnungsfall_zieleinrichtung() {
    return abrechnungsfall_zieleinrichtung;
  }

  public void setAbrechnungsfall_zieleinrichtung(String abrechnungsfall_zieleinrichtung) {
    this.abrechnungsfall_zieleinrichtung = abrechnungsfall_zieleinrichtung;
  }

  public String getAbrechnungsfall_aufnahmenummer() {
    return abrechnungsfall_aufnahmenummer;
  }

  public void setAbrechnungsfall_aufnahmenummer(String abrechnungsfall_aufnahmenummer) {
    this.abrechnungsfall_aufnahmenummer = abrechnungsfall_aufnahmenummer;
  }

  public String getAbrechnungsfall_fallzusammenfuehrung() {
    return abrechnungsfall_fallzusammenfuehrung;
  }

  public void setAbrechnungsfall_fallzusammenfuehrung(String abrechnungsfall_fallzusammenfuehrung) {
    this.abrechnungsfall_fallzusammenfuehrung = abrechnungsfall_fallzusammenfuehrung;
  }
}