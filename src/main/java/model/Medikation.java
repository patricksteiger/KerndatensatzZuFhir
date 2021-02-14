package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import enums.Behandlungsgrund;
import enums.MedikationStatus;
import enums.Wirkstofftyp;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    // TODO: medication[x]: reference to Medication
    // TODO: How does Behandlungsgrund look like?
    medicationAdministration.addReasonCode(this.getMedicationAdministrationReasonCode());
    // TODO: Does MedicationAdministration have DateAsserted?
    return medicationAdministration;
  }

  public CodeableConcept getMedicationAdministrationReasonCode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getBehandlungsgrund());
    String behandlungsgrund = parsedCode.getCode();
    if (Helper.checkEmptyString(behandlungsgrund)) {
      return Constants.getEmptyValue();
    }
    return Behandlungsgrund.fromCode(behandlungsgrund)
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElse(
            LOGGER.error(
                "getMedicationAdministrationReasonCode", "behandlungsgrund", behandlungsgrund));
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
    // TODO: medication[x]: reference to Medication
    // TODO: How does Behandlungsgrund look like?
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

  public Medication.MedicationIngredientComponent getMedicationIngredient() {
    Medication.MedicationIngredientComponent ingredient =
        new Medication.MedicationIngredientComponent();
    ingredient.addExtension(this.getMedicationIngredientExtension());
    CodeableConcept item = this.getMedicationIngredientItem();
    if (item == Constants.getEmptyValue()) {
      return LOGGER.error("getMedicationIngredient", "Item needs to be set for Ingredient!");
    }
    ingredient.setItem(item);
    ingredient.setStrength(this.getMedicationIngredientStrength());
    return ingredient;
  }

  public Ratio getMedicationIngredientStrength() {
    String menge = this.getWirkstoff_menge();
    if (Helper.checkEmptyString(menge)) {
      return Constants.getEmptyValue();
    }
    ValueAndUnitParsed parsedStrength = ValueAndUnitParsed.fromString(menge);
    String parsedValue = parsedStrength.getValue();
    String parsedUnit = parsedStrength.getUnit();
    BigDecimal value = new BigDecimal(parsedValue);
    Quantity numerator = FhirGenerator.quantity(value, parsedUnit);
    // TODO: Is only numerator for Ratio correct?
    return new Ratio().setNumerator(numerator);
  }

  public CodeableConcept getMedicationIngredientItem() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getWirkstoff_code_aktiv());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return LOGGER.emptyValue("getMedicationIngredientItem", "wirkstoff_code_aktiv");
    }
    String system = parsedCode.getSystem();
    String display = this.getWirkstoff_name_aktiv();
    if (parsedCode.hasDisplay()) display = parsedCode.getDisplay();
    Coding coding = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(coding);
  }

  public Extension getMedicationIngredientExtension() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getWirkstoff_code_allgemein());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    Coding value =
        Wirkstofftyp.fromCode(code)
            .map(FhirGenerator::coding)
            .orElse(
                LOGGER.error("getMedicationIngredientExtension", "wirkstoff_code_allgemein", code));
    String url = ExtensionUrl.MEDIKATION_WIRKSTOFFTYP;
    return FhirGenerator.extension(url, value);
  }

  public Ratio getMedicationAmount() {
    String wirkstaerke = this.getArzneimittel_wirkstaerke();
    if (Helper.checkEmptyString(wirkstaerke)) {
      return Constants.getEmptyValue();
    }
    ValueAndUnitParsed parseStaerke = ValueAndUnitParsed.fromString(wirkstaerke);
    String parsedValue = parseStaerke.getValue();
    BigDecimal value = new BigDecimal(parsedValue);
    String unit = parseStaerke.getUnit();
    Quantity numerator = FhirGenerator.quantity(value, unit);
    // TODO: Is only numerator for Ratio correct?
    return new Ratio().setNumerator(numerator);
  }

  public CodeableConcept getMedicationForm() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getDarreichungsform());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.EDQM_STANDARD;
    String display = parsedCode.getDisplay();
    Coding edqm = FhirGenerator.coding(code, system, display);
    return new CodeableConcept().addCoding(edqm);
  }

  // TODO: How do you separate PHARMA from ATC? ATC always DE?
  public CodeableConcept getMedicationCode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getArzneimittel_code());
    if (Helper.checkEmptyString(parsedCode.getCode())) {
      return Constants.getEmptyValue();
    }
    CodeableConcept code = new CodeableConcept();
    // PHARMA
    Coding pharma = this.getMedicationCodePharma();
    code.addCoding(pharma);
    // ATC
    Coding atcDE = this.getMedicationCodeAtcDE();
    code.addCoding(atcDE);
    // Text
    code.setText(this.getMedicationCodeText());
    return code;
  }

  public String getMedicationCodeText() {
    return this.getRezeptur_freitextzeile();
  }

  public Coding getMedicationCodeAtcDE() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getArzneimittel_code());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ATC_DIMDI;
    String display = this.getArzneimittel_name();
    return FhirGenerator.coding(code, system, display);
  }

  public Coding getMedicationCodePharma() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getArzneimittel_code());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.PHARMA_ZENTRAL_NUMMER;
    String display = this.getArzneimittel_name();
    return FhirGenerator.coding(code, system, display);
  }

  public Reference getMedicationAdministrationRequest() {
    String verordnung = this.getBezug_verordnung();
    if (Helper.checkEmptyString(verordnung)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.reference(verordnung);
  }

  public Annotation getMedicationAdministrationNote() {
    return this.getMedicationStatementNote();
  }

  public MedicationAdministration.MedicationAdministrationDosageComponent
      getMedicationAdministrationDosage() {
    if (this.hasNoMedicationAdministrationDosage()) {
      return Constants.getEmptyValue();
    }
    MedicationAdministration.MedicationAdministrationDosageComponent dosageComponent =
        new MedicationAdministration.MedicationAdministrationDosageComponent();
    dosageComponent.setText(this.getMedicationAdministrationDosageText());
    dosageComponent.setRoute(this.getMedicationAdministrationDosageRoute());
    return dosageComponent;
  }

  public CodeableConcept getMedicationAdministrationDosageRoute() {
    return this.getMedicationStatementDosageRoute();
  }

  public String getMedicationAdministrationDosageText() {
    return this.getDosierung_freitext();
  }

  public boolean hasNoMedicationAdministrationDosage() {
    return this.hasNoMedicationStatementDosage();
  }

  public Reference getMedicationAdministrationSubject() {
    String patientenNr = this.getPatNr();
    if (Helper.checkEmptyString(patientenNr)) {
      return Constants.getEmptyValue();
    }
    String ref = MIIReference.getPatient(patientenNr);
    return FhirGenerator.reference(ref);
  }

  public Identifier getMedicationAdministrationIdentifier() {
    String value = this.getIdentifikation();
    if (Helper.checkEmptyString(value)) {
      return Constants.getEmptyValue();
    }
    // FIXME: What is system of MedicationAdministration identifier?
    String system = Constants.EMPTY_IDENTIFIER_SYSTEM;
    return FhirGenerator.identifier(value, system);
  }

  public Type getMedicationAdministrationEffective() {
    return this.getMedicationStatementEffective();
  }

  public MedicationAdministration.MedicationAdministrationStatus
      getMedicationAdministrationStatus() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getStatus());
    String code = parsedCode.getCode();
    return MedikationStatus.medicationAdministrationStatusFromCode(code)
        .orElse(LOGGER.error("getMedicationAdministrationStatus", "status", code));
  }

  public Reference getMedicationStatementInformationSource() {
    String ref = this.getOrganisationsname();
    if (Helper.checkEmptyString(ref)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.reference(ref);
  }

  public Date getMedicationStatementDateAsserted() {
    String eintragsDatum = this.getDatum_eintrag();
    if (Helper.checkEmptyString(eintragsDatum)) {
      return Constants.getEmptyValue();
    }
    return Helper.getDateFromISO(eintragsDatum)
        .orElse(LOGGER.error("getMedicationStatementDateAsserted", "datum_eintrag", eintragsDatum));
  }

  public Annotation getMedicationStatementNote() {
    String hinweis = this.getHinweis();
    if (Helper.checkEmptyString(hinweis)) {
      return Constants.getEmptyValue();
    }
    return new Annotation().setText(hinweis);
  }

  // TODO: Is route, site and method the same?
  public Dosage getMedicationStatementDosage() {
    if (this.hasNoMedicationStatementDosage()) {
      return Constants.getEmptyValue();
    }
    String sequence = this.getDosierung_reihenfolge();
    String text = this.getDosierung_freitext();
    Timing timing = this.getMedicationStatementDosageTiming();
    Type asNeeded = this.getMedicationStatementDosageAsNeeded();
    CodeableConcept route = this.getMedicationStatementDosageRoute();
    List<Dosage.DosageDoseAndRateComponent> doseAndRate =
        this.getMedicationStatementDosageDoseAndRate();
    return FhirGenerator.dosage(sequence, text, timing, asNeeded, route, doseAndRate);
  }

  public boolean hasNoMedicationStatementDosage() {
    return Helper.checkAllEmptyString(
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
    String dosis = this.getDosierung_dosis();
    if (Helper.checkEmptyString(dosis)) {
      return Helper.listOf();
    }
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(dosis);
    String parsedValue = parsed.getValue();
    String parsedUnit = parsed.getUnit();
    BigDecimal value = new BigDecimal(parsedValue);
    Quantity dose = FhirGenerator.quantity(value, parsedUnit);
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
        .orElse(
            LOGGER.error(
                "getMedicationStatementDosageTimingEvent", "dosierung_zeitpunkt", zeitpunkt));
  }

  public Date getMedicationStatementDosageTimingPhase() {
    String phase = this.getDosierung_phase();
    if (Helper.checkEmptyString(phase)) {
      return Constants.getEmptyValue();
    }
    return Helper.getDateFromISO(phase)
        .orElse(LOGGER.error("getMedicationStatementDosageTimingPhase", "dosierung_phase", phase));
  }

  public Timing.TimingRepeatComponent getMedicationStatementDosageTimingRepeat() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getDosierung_ereignis());
    String ereignis = parsedCode.getCode();
    if (Helper.checkEmptyString(ereignis)) {
      return Constants.getEmptyValue();
    }
    Timing.TimingRepeatComponent repeat = new Timing.TimingRepeatComponent();
    // TODO: ereignis can be when, dayOfWeek, timeOfDay, ... which is it? How can we distinguish?
    ValueAndUnitParsed offset = ValueAndUnitParsed.fromString(this.getDosierung_offset());
    String parsedValue = offset.getValue();
    int value = Integer.parseInt(parsedValue);
    repeat.setOffset(value);
    // TODO: How does Periode look like?
    return repeat;
  }

  public CodeableConcept getMedicationStatementDosageRoute() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getDosierung_art_der_anwendung());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    // CodeSystem can be SNOMED or EDQM
    String system = parsedCode.getSystem();
    String display = parsedCode.getDisplay();
    Coding coding = FhirGenerator.coding(code, system, display);
    return new CodeableConcept().addCoding(coding);
  }

  public Type getMedicationStatementDosageAsNeeded() {
    String einnahmeBeiBedarf = this.getDosierung_einnahme_bei_bedarf();
    if (Helper.checkEmptyString(einnahmeBeiBedarf)) {
      return Constants.getEmptyValue();
    }
    return Helper.booleanFromString(einnahmeBeiBedarf)
        .map(FhirGenerator::booleanType)
        .orElse(
            LOGGER.error(
                "getMedicationStatementDosageAsNeeded",
                "dosierung_einnahme_bei_bedarf",
                einnahmeBeiBedarf));
  }

  public Type getMedicationStatementEffective() {
    String startzeitpunkt = this.getEinnahme_startzeitpunkt();
    if (Helper.checkEmptyString(startzeitpunkt)) {
      return LOGGER.emptyValue("getMedicationStatementEffective", "einnahme_startzeitpunkt");
    }
    DateTimeType start =
        Helper.getDateFromISO(startzeitpunkt)
            .map(FhirGenerator::dateTimeType)
            .orElse(
                LOGGER.error(
                    "getMedicationStatementEffective", "einnahme_startzeitpunkt", startzeitpunkt));
    DateTimeType end = new DateTimeType();
    String endzeitpunkt = this.getEinnahme_endzeitpunkt();
    if (Helper.checkNonEmptyString(endzeitpunkt)) {
      Date endDate =
          Helper.getDateFromISO(endzeitpunkt)
              .orElse(
                  LOGGER.error(
                      "getMedicationStatementEffective", "einnahme_endzeitpunkt", endzeitpunkt));
      end.setValue(endDate);
    }
    // Return period if both start and end got set. Otherwise only return start.
    return end.hasValue() ? new Period().setStartElement(start).setEndElement(end) : start;
  }

  public Reference getMedicationStatementBasedOn() {
    String verordnung = this.getBezug_verordnung();
    if (Helper.checkEmptyString(verordnung)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.reference(verordnung);
  }

  public MedicationStatement.MedicationStatementStatus getMedicationStatementStatus() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getStatus());
    String code = parsedCode.getCode();
    return MedikationStatus.medicationStatementStatusFromCode(code)
        .orElse(LOGGER.error("getMedicationStatementStatus", "status", code));
  }

  public Identifier getMedicationStatementIdentifier() {
    String value = this.getIdentifikation();
    if (Helper.checkEmptyString(value)) {
      return Constants.getEmptyValue();
    }
    // FIXME: What is system of MedicationStatement identifier?
    String system = Constants.EMPTY_IDENTIFIER_SYSTEM;
    return FhirGenerator.identifier(value, system);
  }

  public Reference getMedicationStatementPartOf() {
    String abgabe = this.getBezug_abgabe();
    if (Helper.checkEmptyString(abgabe)) {
      return Constants.getEmptyValue();
    }
    return FhirGenerator.reference(abgabe);
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
