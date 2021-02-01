package model;

import constants.*;
import enums.MedikationStatus;
import enums.Wirkstofftyp;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;
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

  public MedicationAdministration getMedicationAdministration() {
    MedicationAdministration medicationAdministration = new MedicationAdministration();
    // Meta
    medicationAdministration.setMeta(this.getMedicationAdministrationMeta());
    // Identifier (optional)
    if (Helper.checkNonEmptyString(this.getIdentifikation()))
      medicationAdministration.addIdentifier(this.getMedicationAdministrationIdentifier());
    // Status
    medicationAdministration.setStatus(this.getMedicationAdministrationStatus());
    // Subject (optional)
    if (Helper.checkNonEmptyString(this.getPatNr()))
      medicationAdministration.setSubject(this.getMedicationAdministrationSubject());
    // Effective
    medicationAdministration.setEffective(this.getMedicationAdministrationEffective());
    // Dosage (optional)
    if (this.hasMedicationAdministrationDosage())
      medicationAdministration.setDosage(this.getMedicationAdministrationDosage());
    // Note (optional)
    if (Helper.checkNonEmptyString(this.getHinweis()))
      medicationAdministration.addNote(this.getMedicationAdministrationNode());
    // Request (optional)
    if (Helper.checkNonEmptyString(this.getBezug_verordnung()))
      medicationAdministration.setRequest(this.getMedicationAdministrationRequest());
    // TODO: medication[x]: reference to Medication
    // TODO: How does Behandlungsgrund look like?
    // TODO: Does MedicationAdministration have DateAsserted?
    return medicationAdministration;
  }

  public MedicationStatement getMedicationStatement() {
    MedicationStatement medicationStatement = new MedicationStatement();
    // Meta
    medicationStatement.setMeta(this.getMedicationStatementMeta());
    // Subject
    medicationStatement.setSubject(this.getMedicationStatementSubject());
    // BasedOn (optional)
    if (Helper.checkNonEmptyString(this.getBezug_verordnung()))
      medicationStatement.addBasedOn(this.getMedicationStatementBasedOn());
    // PartOf (optional)
    if (Helper.checkNonEmptyString(this.getBezug_abgabe()))
      medicationStatement.addPartOf(this.getMedicationStatementPartOf());
    // Identifier (optional)
    if (Helper.checkNonEmptyString(this.getIdentifikation()))
      medicationStatement.addIdentifier(this.getMedicationStatementIdentifier());
    // Status
    medicationStatement.setStatus(this.getMedicationStatementStatus());
    // Effective
    medicationStatement.setEffective(this.getMedicationStatementEffective());
    // Dosage (optional)
    if (this.hasMedicationStatementDosage())
      medicationStatement.addDosage(this.getMedicationStatementDosage());
    // Note (optional)
    if (Helper.checkNonEmptyString(this.getHinweis()))
      medicationStatement.addNote(this.getMedicationStatementNote());
    // DateAsserted (optional)
    if (Helper.checkNonEmptyString(this.getDatum_eintrag()))
      medicationStatement.setDateAsserted(this.getMedicationStatementDateAsserted());
    // InformationSource (optional)
    if (Helper.checkNonEmptyString(this.getOrganisationsname()))
      medicationStatement.setInformationSource(this.getMedicationStatementInformationSource());
    // TODO: medication[x]: reference to Medication
    // TODO: How does Behandlungsgrund look like?
    return medicationStatement;
  }

  public Medication getMedication() {
    Medication medication = new Medication();
    // Meta
    medication.setMeta(this.getMedicationMeta());
    // Code (optional)
    if (Helper.checkNonEmptyString(this.getArzneimittel_code()))
      medication.setCode(this.getMedicationCode());
    // Form (optional)
    if (Helper.checkNonEmptyString(this.getDarreichungsform()))
      medication.setForm(this.getMedicationForm());
    // Amount (optional)
    if (Helper.checkNonEmptyString(this.getArzneimittel_wirkstaerke()))
      medication.setAmount(this.getMedicationAmount());
    // Ingredient
    medication.addIngredient(this.getMedicationIngredient());
    return medication;
  }

  public Medication.MedicationIngredientComponent getMedicationIngredient() {
    Medication.MedicationIngredientComponent ingredient =
        new Medication.MedicationIngredientComponent();
    if (Helper.checkNonEmptyString(this.getWirkstoff_code_allgemein()))
      ingredient.addExtension(this.getMedicationIngredientExtension());
    ingredient.setItem(this.getMedicationIngredientItem());
    if (Helper.checkNonEmptyString(this.getWirkstoff_menge()))
      ingredient.setStrength(this.getMedicationIngredientStrength());
    return ingredient;
  }

  public Ratio getMedicationIngredientStrength() {
    ValueAndUnitParsed parsedStrength = ValueAndUnitParsed.fromString(this.getWirkstoff_menge());
    Quantity numerator =
        FhirGenerator.quantity(parsedStrength.getValue(), parsedStrength.getUnit());
    // TODO: Is only numerator for Ratio correct?
    return new Ratio().setNumerator(numerator);
  }

  public CodeableConcept getMedicationIngredientItem() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getWirkstoff_code_aktiv());
    String code = parsedCode.getCode();
    String system = parsedCode.getSystem();
    String display = this.getWirkstoff_name_aktiv();
    if (parsedCode.hasDisplay()) display = parsedCode.getDisplay();
    Coding coding = FhirGenerator.coding(code, system, display);
    return new CodeableConcept().addCoding(coding);
  }

  public Extension getMedicationIngredientExtension() {
    Wirkstofftyp wirkstofftyp = Wirkstofftyp.fromCode(this.getWirkstoff_code_allgemein());
    Coding value =
        FhirGenerator.coding(
            wirkstofftyp.getCode(), wirkstofftyp.getSystem(), wirkstofftyp.getDisplay());
    String url = ExtensionUrl.MEDIKATION_WIRKSTOFFTYP;
    return FhirGenerator.extension(url, value);
  }

  public Ratio getMedicationAmount() {
    ValueAndUnitParsed parseStaerke =
        ValueAndUnitParsed.fromString(this.getArzneimittel_wirkstaerke());
    BigDecimal value = parseStaerke.getValue();
    String unit = parseStaerke.getUnit();
    Quantity numerator = FhirGenerator.quantity(value, unit);
    // TODO: Is only numerator for Ratio correct?
    return new Ratio().setNumerator(numerator);
  }

  public CodeableConcept getMedicationForm() {
    String code = this.getDarreichungsform();
    String system = CodingSystem.EDQM_STANDARD;
    Coding edqm = FhirGenerator.coding(code, system);
    return new CodeableConcept().addCoding(edqm);
  }

  // TODO: How do you separate PHARMA from ATC? ATC always DE?
  public CodeableConcept getMedicationCode() {
    CodeableConcept code = new CodeableConcept();
    if (Helper.checkNonEmptyString(this.getArzneimittel_code())) {
      Coding pharma = this.getMedicationCodePharma();
      code.addCoding(pharma);
    }
    if (Helper.checkNonEmptyString(this.getArzneimittel_code())) {
      Coding atcDE = this.getMedicationCodeAtcDE();
      code.addCoding(atcDE);
    }
    if (Helper.checkNonEmptyString(this.getRezeptur_freitextzeile())) {
      String text = this.getRezeptur_freitextzeile();
      code.setText(text);
    }
    return code;
  }

  public Coding getMedicationCodeAtcDE() {
    String code = this.getArzneimittel_code();
    String system = CodingSystem.ATC_DIMDI;
    String display = this.getArzneimittel_name();
    return FhirGenerator.coding(code, system, display);
  }

  public Coding getMedicationCodePharma() {
    String code = this.getArzneimittel_code();
    String system = CodingSystem.PHARMA_ZENTRAL_NUMMER;
    String display = this.getArzneimittel_name();
    return FhirGenerator.coding(code, system, display);
  }

  public Reference getMedicationAdministrationRequest() {
    String ref = this.getBezug_verordnung();
    return FhirGenerator.reference(ref);
  }

  public Annotation getMedicationAdministrationNode() {
    return this.getMedicationStatementNote();
  }

  public MedicationAdministration.MedicationAdministrationDosageComponent
      getMedicationAdministrationDosage() {
    Dosage statementDosage = this.getMedicationStatementDosage();
    return FhirHelper.getMedicationAdministrationDosageFromDosage(statementDosage);
  }

  public boolean hasMedicationAdministrationDosage() {
    return this.hasMedicationStatementDosage();
  }

  public Reference getMedicationAdministrationSubject() {
    return FhirHelper.getMIIPatientReference(this.getPatNr());
  }

  public Identifier getMedicationAdministrationIdentifier() {
    String value = this.getIdentifikation();
    // FIXME: What is system of MedicationAdministration identifier?
    String system = "";
    return FhirGenerator.identifier(value, system);
  }

  public Type getMedicationAdministrationEffective() {
    return this.getMedicationStatementEffective();
  }

  public MedicationAdministration.MedicationAdministrationStatus
      getMedicationAdministrationStatus() {
    return MedikationStatus.medicationAdministrationStatusFromCode(this.getStatus());
  }

  public Reference getMedicationStatementInformationSource() {
    String ref = this.getOrganisationsname();
    return FhirGenerator.reference(ref);
  }

  public Date getMedicationStatementDateAsserted() {
    Date dateAsserted = Helper.getDateFromISO(this.getDatum_eintrag());
    return dateAsserted;
  }

  public Annotation getMedicationStatementNote() {
    MarkdownType text = new MarkdownType(this.getHinweis());
    return new Annotation(text);
  }

  public Dosage getMedicationStatementDosage() {
    String sequence = this.getDosierung_reihenfolge();
    String text = this.getDosierung_freitext();
    Timing timing = this.getMedicationStatementDosageTiming();
    Type asNeeded = this.getMedicationStatementDosageAsNeeded();
    CodeableConcept route = this.getMedicationStatementDosageRoute();
    List<Dosage.DosageDoseAndRateComponent> doseAndRate =
        this.getMedicationStatementDosageDoseAndRate();
    return FhirGenerator.dosage(sequence, text, timing, asNeeded, route, doseAndRate);
  }

  public boolean hasMedicationStatementDosage() {
    return Helper.checkAnyNonEmptyStrings(
        this.getDosierung_reihenfolge(),
        this.getDosierung_freitext(),
        this.getDosierung_einnahme_bei_bedarf(),
        this.getDosierung_ereignis(),
        this.getDosierung_dosis(),
        this.getDosierung_art_der_anwendung(),
        this.getDosierung_offset(),
        this.getDosierung_phase(),
        this.getDosierung_zeitpunkt(),
        this.getDosierung_periode());
  }

  public List<Dosage.DosageDoseAndRateComponent> getMedicationStatementDosageDoseAndRate() {
    if (!Helper.checkNonEmptyString(this.getDosierung_dosis())) return Helper.listOf();
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(this.getDosierung_dosis());
    Quantity dose = FhirGenerator.quantity(parsed.getValue(), parsed.getUnit());
    Dosage.DosageDoseAndRateComponent doseAndRate = new Dosage.DosageDoseAndRateComponent();
    doseAndRate.setDose(dose);
    return Helper.listOf(doseAndRate);
  }

  public Timing getMedicationStatementDosageTiming() {
    // TODO: Timing for MedicationStatement dosage
    Timing timing = new Timing();
    if (Helper.checkNonEmptyString(this.getDosierung_zeitpunkt())) {
      Date event = Helper.getDateFromISO(this.getDosierung_zeitpunkt());
      timing.addEvent(event);
    }
    return timing;
  }

  public CodeableConcept getMedicationStatementDosageRoute() {
    if (!Helper.checkNonEmptyString(this.getDosierung_art_der_anwendung())) return null;
    ParsedCode parsedCode = ParsedCode.fromString(this.getDosierung_art_der_anwendung());
    String code = parsedCode.getCode();
    // CodeSystem can be SNOMED or EDQM
    String system = parsedCode.getSystem();
    String display = parsedCode.getDisplay();
    Coding snomed = FhirGenerator.coding(code, system, display);
    return new CodeableConcept().addCoding(snomed);
  }

  public Type getMedicationStatementDosageAsNeeded() {
    if (!Helper.checkNonEmptyString(this.getDosierung_einnahme_bei_bedarf())) return null;
    boolean bedarf = Helper.booleanFromString(this.getDosierung_einnahme_bei_bedarf());
    return new BooleanType(bedarf);
  }

  public Type getMedicationStatementEffective() {
    Date startDate = Helper.getDateFromISO(this.getEinnahme_startzeitpunkt());
    DateTimeType start = new DateTimeType(startDate);
    DateTimeType end = new DateTimeType();
    if (Helper.checkNonEmptyString(this.getEinnahme_endzeitpunkt())) {
      Date endDate = Helper.getDateFromISO(this.getEinnahme_endzeitpunkt());
      end.setValue(endDate);
    }
    // Return period if both start and end got set. Otherwise only return start.
    return end.hasValue() ? new Period().setStartElement(start).setEndElement(end) : start;
  }

  public Reference getMedicationStatementBasedOn() {
    String ref = this.getBezug_verordnung();
    return FhirGenerator.reference(ref);
  }

  public MedicationStatement.MedicationStatementStatus getMedicationStatementStatus() {
    return MedikationStatus.medicationStatementStatusFromCode(this.getStatus());
  }

  public Identifier getMedicationStatementIdentifier() {
    String value = this.getIdentifikation();
    // FIXME: What is system of MedicationStatement identifier?
    String system = "";
    return FhirGenerator.identifier(value, system);
  }

  public Reference getMedicationStatementPartOf() {
    String ref = this.getBezug_abgabe();
    return FhirGenerator.reference(ref);
  }

  public Reference getMedicationStatementSubject() {
    return FhirHelper.getMIIPatientReference(this.getPatNr());
  }

  public Meta getMedicationMeta() {
    String profile = MetaProfile.MEDIKATION_MEDICATION;
    String source = MetaSource.MEDIKATION_MEDICATION;
    String versionId = MetaVersionId.MEDIKATION_MEDICATION;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Meta getMedicationAdministrationMeta() {
    String profile = MetaProfile.MEDIKATION_MEDICATION_ADMINISTRATION;
    String source = MetaSource.MEDIKATION_MEDICATION_ADMINISTRATION;
    String versionId = MetaVersionId.MEDIKATION_MEDICATION_ADMINISTRATION;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Meta getMedicationStatementMeta() {
    String profile = MetaProfile.MEDIKATION_MEDICATION_STATEMENT;
    String source = MetaSource.MEDIKATION_MEDICATION_STATEMENT;
    String versionId = MetaVersionId.MEDIKATION_MEDICATION_STATEMENT;
    return FhirGenerator.meta(profile, source, versionId);
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
