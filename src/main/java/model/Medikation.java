package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;
import valueSets.Behandlungsgrund;
import valueSets.Wirkstofftyp;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static helper.FhirParser.*;

public class Medikation implements Datablock {
  private final Logger LOGGER = new Logger(Medikation.class);
  @CsvBindByName private String patNr;
  // Medikationseintrag
  @CsvBindByName private String identifikation;
  @CsvBindByName private String status;
  @CsvBindByName private String hinweis;
  @CsvBindByName private String behandlungsgrund;
  @CsvBindByName private String bezug_verordnung;
  @CsvBindByName private String bezug_abgabe;
  @CsvBindByName private String datum_eintrag;
  // Arzneimittel/Wirkstoff/Rezeptur
  @CsvBindByName private String darreichungsform;
  // Arzneimittel/Wirkstoff/Rezeptur - Arzneimittelprodukt
  @CsvBindByName private String arzneimittel_name;
  @CsvBindByName private String arzneimittel_code;
  @CsvBindByName private String arzneimittel_wirkstaerke;
  // Arzneimittel/Wirkstoff/Rezeptur - Rezeptur
  @CsvBindByName private String rezeptur_freitextzeile;
  // Arzneimittel/Wirkstoff/Rezeptur - Wirkstoff
  @CsvBindByName private String wirkstoff_name_allgemein;
  @CsvBindByName private String wirkstoff_code_allgemein;
  @CsvBindByName private String wirkstoff_name_aktiv;
  @CsvBindByName private String wirkstoff_code_aktiv;
  @CsvBindByName private String wirkstoff_menge;
  // Einnahmedauer
  @CsvBindByName private String einnahme_startzeitpunkt;
  @CsvBindByName private String einnahme_endzeitpunkt;
  @CsvBindByName private String einnahme_dauer;
  // Dosierung (Freitext)
  @CsvBindByName private String dosierung_freitext;
  // Dosierung (strukturiert) (Dosierungsinformationen, Angaben zur Dosierung)
  @CsvBindByName private String dosierung_reihenfolge;
  @CsvBindByName private String dosierung_einnahme_bei_bedarf;
  @CsvBindByName private String dosierung_art_der_anwendung;
  @CsvBindByName private String dosierung_dosis;
  // Dosierung (strukturiert) - Zeitangabe
  @CsvBindByName private String dosierung_zeitpunkt;
  // Dosierung (strukturiert) - Zeitangabe - Ereignisbezogene Wiederholung
  @CsvBindByName private String dosierung_ereignis;
  @CsvBindByName private String dosierung_offset;
  // Dosierung (strukturiert) - Zeitangabe - Periodisches Intervall
  @CsvBindByName private String dosierung_phase;
  @CsvBindByName private String dosierung_periode;
  // Autor/Informant des Eintrags
  @CsvBindByName private String organisationsname;

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
    medicationAdministration.addIdentifier(this.getMedicationAdministrationIdentifier());
    // Status
    medicationAdministration.setStatus(this.getMedicationAdministrationStatus());
    // Subject (optional)
    medicationAdministration.setSubject(this.getMedicationAdministrationSubject());
    // Effective
    medicationAdministration.setEffective(this.getMedicationAdministrationEffective());
    // Dosage (optional)
    medicationAdministration.setDosage(this.getMedicationAdministrationDosage());
    // Note (optional)
    medicationAdministration.addNote(this.getMedicationAdministrationNote());
    // Request (optional)
    medicationAdministration.setRequest(this.getMedicationAdministrationRequest());
    // Medication
    medicationAdministration.setMedication(this.getMedicationAdministrationMedication());
    // ReasonCode (optional)
    medicationAdministration.addReasonCode(this.getMedicationAdministrationReasonCode());
    return medicationAdministration;
  }

  public MedicationStatement getMedicationStatement() {
    MedicationStatement medicationStatement = new MedicationStatement();
    // Meta
    medicationStatement.setMeta(this.getMedicationStatementMeta());
    // Subject
    medicationStatement.setSubject(this.getMedicationStatementSubject());
    // BasedOn (optional)
    medicationStatement.addBasedOn(this.getMedicationStatementBasedOn());
    // PartOf (optional)
    medicationStatement.addPartOf(this.getMedicationStatementPartOf());
    // Identifier (optional)
    medicationStatement.addIdentifier(this.getMedicationStatementIdentifier());
    // Status
    medicationStatement.setStatus(this.getMedicationStatementStatus());
    // Effective
    medicationStatement.setEffective(this.getMedicationStatementEffective());
    // Dosage (optional)
    medicationStatement.addDosage(this.getMedicationStatementDosage());
    // Note (optional)
    medicationStatement.addNote(this.getMedicationStatementNote());
    // DateAsserted (optional)
    medicationStatement.setDateAsserted(this.getMedicationStatementDateAsserted());
    // InformationSource (optional)
    medicationStatement.setInformationSource(this.getMedicationStatementInformationSource());
    // Medication
    medicationStatement.setMedication(this.getMedicationStatementMedication());
    // ReasonCode (optional)
    medicationStatement.addReasonCode(this.getMedicationStatementReasonCode());
    return medicationStatement;
  }

  public Medication getMedication() {
    Medication medication = new Medication();
    // Meta
    medication.setMeta(this.getMedicationMeta());
    // Code (optional)
    medication.setCode(this.getMedicationCode());
    // Form (optional)
    medication.setForm(this.getMedicationForm());
    // Amount (optional)
    medication.setAmount(this.getMedicationAmount());
    // Ingredient
    medication.addIngredient(this.getMedicationIngredient());
    return medication;
  }

  public Reference getMedicationStatementMedication() {
    return this.getMedicationAdministrationMedication();
  }

  public CodeableConcept getMedicationStatementReasonCode() {
    return this.getMedicationAdministrationReasonCode();
  }

  // TODO: medication could also be CodeableConcept containing code of medication
  public Reference getMedicationAdministrationMedication() {
    String ref = MIIReference.REF_MEDICATION;
    return FhirGenerator.reference(ref);
  }
  // TODO: How does Behandlungsgrund look like?
  public CodeableConcept getMedicationAdministrationReasonCode() {
    String code = this.getBehandlungsgrund();
    LoggingData data =
        LoggingData.of(LOGGER, "getMedicationAdministrationReasonCode", "behandlungsgrund");
    return optionalCodeFromValueSet(code, Behandlungsgrund::fromCode, data);
  }

  public Medication.MedicationIngredientComponent getMedicationIngredient() {
    Medication.MedicationIngredientComponent ingredient =
        new Medication.MedicationIngredientComponent();
    ingredient.addExtension(this.getMedicationIngredientExtension());
    CodeableConcept item = this.getMedicationIngredientItem();
    if (Constants.isEmptyValue(item)) {
      return LOGGER.error("getMedicationIngredient", "Item needs to be set for Ingredient!");
    }
    ingredient.setItem(item);
    ingredient.setStrength(this.getMedicationIngredientStrength());
    return ingredient;
  }

  public Ratio getMedicationIngredientStrength() {
    String menge = this.getWirkstoff_menge();
    LoggingData data = LoggingData.of(LOGGER, "getMedicationIngredientStrength", "wirkstoff_menge");
    return optionalRatio(menge, data);
  }

  public CodeableConcept getMedicationIngredientItem() {
    String code = this.getWirkstoff_code_aktiv();
    String display = this.getWirkstoff_name_aktiv();
    LoggingData data =
        LoggingData.of(LOGGER, "getMedicationIngredientItem", "wirkstoff_code_aktiv");
    return codeWithDefaultDisplay(code, display, data);
  }

  public Extension getMedicationIngredientExtension() {
    String code = this.getWirkstoff_code_allgemein();
    String url = ExtensionUrl.MEDIKATION_WIRKSTOFFTYP;
    LoggingData data =
        LoggingData.of(LOGGER, "getMedicationIngredientExtension", "wirkstoff_code_allgemein");
    return optionalExtensionWithCodingFromValueSet(code, url, Wirkstofftyp::fromCode, data);
  }

  public Ratio getMedicationAmount() {
    String wirkstaerke = this.getArzneimittel_wirkstaerke();
    LoggingData data = LoggingData.of(LOGGER, "getMedicationAmount", "arzneimittel_wirkstaerke");
    return optionalRatio(wirkstaerke, data);
  }

  public CodeableConcept getMedicationForm() {
    String code = this.getDarreichungsform();
    String system = CodingSystem.EDQM_STANDARD;
    LoggingData data = LoggingData.of(LOGGER, "getMedicationForm", "darreichungsform");
    return optionalCodeFromSystemWithValidation(code, system, DoseFormUvIps::validate, data);
  }

  public CodeableConcept getMedicationCode() {
    String code = this.getArzneimittel_code();
    String text = this.getMedicationCodeText();
    // Generally system is expected to be AtcDE. Only Change if PZN is given explicitly.
    Function<ParsedCode, Coding> chooser =
        parsedCode ->
            parsedCode.getSystem().equals(CodingSystem.PHARMA_ZENTRAL_NUMMER)
                ? this.getMedicationCodePharma()
                : this.getMedicationCodeAtcDE();
    return optionalCodeWithCodingAndOptionalText(code, text, chooser);
  }

  public String getMedicationCodeText() {
    return this.getRezeptur_freitextzeile();
  }

  public Coding getMedicationCodeAtcDE() {
    String code = this.getArzneimittel_code();
    String system = CodingSystem.ATC_DIMDI;
    String display = this.getArzneimittel_name();
    return optionalCodingFromSystemWithDefaultDisplay(code, system, display);
  }

  public Coding getMedicationCodePharma() {
    String code = this.getArzneimittel_code();
    String system = CodingSystem.PHARMA_ZENTRAL_NUMMER;
    String display = this.getArzneimittel_name();
    return optionalCodingFromSystemWithDefaultDisplay(code, system, display);
  }

  public Reference getMedicationAdministrationRequest() {
    String verordnung = this.getBezug_verordnung();
    return optionalReference(verordnung);
  }

  public Annotation getMedicationAdministrationNote() {
    return this.getMedicationStatementNote();
  }

  public MedicationAdministration.MedicationAdministrationDosageComponent
      getMedicationAdministrationDosage() {
    String text = this.getMedicationAdministrationDosageText();
    // Dosage is not yet structurally defined. Only text is set currently.
    // CodeableConcept route = this.getMedicationAdministrationDosageRoute();
    if (Helper.checkEmptyString(text) /*&& Constants.isEmptyValue(route)*/) {
      return Constants.getEmptyValue();
    }
    return new MedicationAdministration.MedicationAdministrationDosageComponent().setText(text);
    // .setRoute(route);
  }

  public CodeableConcept getMedicationAdministrationDosageRoute() {
    return this.getMedicationStatementDosageRoute();
  }

  public String getMedicationAdministrationDosageText() {
    return this.getDosierung_freitext();
  }

  public Reference getMedicationAdministrationSubject() {
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public Identifier getMedicationAdministrationIdentifier() {
    String id = this.getIdentifikation();
    return optionalIdentifier(id);
  }

  // TODO: Administration or Statement effective?
  public Type getMedicationAdministrationEffective() {
    return this.getMedicationStatementEffective();
  }

  public MedicationAdministration.MedicationAdministrationStatus
      getMedicationAdministrationStatus() {
    String code = this.getStatus();
    LoggingData data = LoggingData.of(LOGGER, "getMedicationAdministrationStatus", "status");
    return medicationAdministrationStatus(code, data);
  }

  public Reference getMedicationStatementInformationSource() {
    String ref = this.getOrganisationsname();
    return optionalReference(ref);
  }

  public Date getMedicationStatementDateAsserted() {
    String eintragsDatum = this.getDatum_eintrag();
    LoggingData data =
        LoggingData.of(LOGGER, "getMedicationStatementDateAsserted", "datum_eintrag");
    return optionalDate(eintragsDatum, data);
  }

  public Annotation getMedicationStatementNote() {
    return optionalAnnotation(getHinweis());
  }

  // Dosage is currently not structurally defined. Only text is set.
  public Dosage getMedicationStatementDosage() {
    /*String sequence = this.getDosierung_reihenfolge();
    Timing timing = this.getMedicationStatementDosageTiming();
    Type asNeeded = this.getMedicationStatementDosageAsNeeded();
    CodeableConcept route = this.getMedicationStatementDosageRoute();
    List<Dosage.DosageDoseAndRateComponent> doseAndRate =
        this.getMedicationStatementDosageDoseAndRate();
    if (Helper.checkAllEmptyString(sequence, text)
        && Helper.checkAllNull(timing, asNeeded, route, doseAndRate)) {
      return Constants.getEmptyValue();
    }*/
    String text = this.getDosierung_freitext();
    if (Helper.checkEmptyString(text)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.dosage(text);
  }

  public List<Dosage.DosageDoseAndRateComponent> getMedicationStatementDosageDoseAndRate() {
    String dosis = this.getDosierung_dosis();
    if (Helper.checkEmptyString(dosis)) {
      return Constants.getEmptyValue();
    }
    Optional<Quantity> quantity = ParsedQuantity.fromString(dosis);
    if (!quantity.isPresent()) {
      return LOGGER.error("getMedicationStatementDosageDoseAndRate", "dosierung_dosis", dosis);
    }
    Quantity dose = quantity.get();
    Dosage.DosageDoseAndRateComponent doseAndRate = new Dosage.DosageDoseAndRateComponent();
    doseAndRate.setDose(dose);
    return Helper.listOf(doseAndRate);
  }

  public Timing getMedicationStatementDosageTiming() {
    // TODO: Timing for MedicationStatement dosage
    Timing timing = new Timing();
    timing.addEvent(this.getMedicationStatementDosageTimingEvent());
    timing.addEvent(this.getMedicationStatementDosageTimingPhase());
    timing.setRepeat(this.getMedicationStatementDosageTimingRepeat());
    return timing;
  }

  public Date getMedicationStatementDosageTimingEvent() {
    String zeitpunkt = this.getDosierung_zeitpunkt();
    if (Helper.checkEmptyString(zeitpunkt)) {
      return Constants.getEmptyValue();
    }
    return Helper.getDateFromISO(zeitpunkt)
        .orElseGet(
            LOGGER.errorSupplier(
                "getMedicationStatementDosageTimingEvent", "dosierung_zeitpunkt", zeitpunkt));
  }

  public Date getMedicationStatementDosageTimingPhase() {
    String phase = this.getDosierung_phase();
    if (Helper.checkEmptyString(phase)) {
      return Constants.getEmptyValue();
    }
    return Helper.getDateFromISO(phase)
        .orElseGet(
            LOGGER.errorSupplier(
                "getMedicationStatementDosageTimingPhase", "dosierung_phase", phase));
  }

  public Timing.TimingRepeatComponent getMedicationStatementDosageTimingRepeat() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getDosierung_ereignis());
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    Timing.TimingRepeatComponent repeat = new Timing.TimingRepeatComponent();
    // TODO: ereignis can be when, dayOfWeek, timeOfDay, ... which is it? How can we distinguish?
    // TODO: Is offset an int or quantity in MII?
    String offset = this.getDosierung_offset();
    Optional<Quantity> parsedQuantity = ParsedQuantity.fromString(offset);
    String parsedValue = parsedQuantity.get().primitiveValue();
    int value = Integer.parseInt(parsedValue);
    repeat.setOffset(value);
    // TODO: How does Periode look like?
    return repeat;
  }

  public CodeableConcept getMedicationStatementDosageRoute() {
    // CodeSystem can be SNOMED or EDQM
    ParsedCode parsedCode = ParsedCode.fromString(this.getDosierung_art_der_anwendung());
    return parsedCode.hasEmptyCode()
        ? Constants.getEmptyValue()
        : FhirGenerator.codeableConcept(parsedCode);
  }

  public Type getMedicationStatementDosageAsNeeded() {
    String einnahmeBeiBedarf = this.getDosierung_einnahme_bei_bedarf();
    if (Helper.checkEmptyString(einnahmeBeiBedarf)) {
      return Constants.getEmptyValue();
    }
    return Helper.booleanFromString(einnahmeBeiBedarf)
        .map(FhirGenerator::booleanType)
        .orElseGet(
            LOGGER.errorSupplier(
                "getMedicationStatementDosageAsNeeded",
                "dosierung_einnahme_bei_bedarf",
                einnahmeBeiBedarf));
  }

  public Type getMedicationStatementEffective() {
    String startzeitpunkt = this.getEinnahme_startzeitpunkt();
    LoggingData startdata =
        LoggingData.of(LOGGER, "getMedicationStatementEffective", "einnahme_startzeitpunkt");
    String endzeitpunkt = this.getEinnahme_endzeitpunkt();
    LoggingData endData =
        LoggingData.of(LOGGER, "getMedicationStatementEffective", "einnahme_endzeitpunkt");
    return dateTimeTypeOrPeriod(startzeitpunkt, endzeitpunkt, startdata, endData);
  }

  public Reference getMedicationStatementBasedOn() {
    String verordnung = this.getBezug_verordnung();
    return optionalReference(verordnung);
  }

  public MedicationStatement.MedicationStatementStatus getMedicationStatementStatus() {
    String code = this.getStatus();
    LoggingData data = LoggingData.of(LOGGER, "getMedicationStatementStatus", "status");
    return medicationStatementStatus(code, data);
  }

  public Identifier getMedicationStatementIdentifier() {
    String id = this.getIdentifikation();
    return optionalIdentifier(id);
  }

  public Reference getMedicationStatementPartOf() {
    String abgabe = this.getBezug_abgabe();
    return optionalReference(abgabe);
  }

  public Reference getMedicationStatementSubject() {
    return this.getMedicationAdministrationSubject();
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
