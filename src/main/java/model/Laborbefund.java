package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import enums.*;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;

import static helper.FhirParser.*;

public class Laborbefund implements Datablock {
  private final Logger LOGGER = new Logger(Laborbefund.class);
  @CsvBindByName private String patNr;
  @CsvBindByName private String identifikation;
  @CsvBindByName private String status;
  @CsvBindByName private String klinisches_bezugsdatum;
  @CsvBindByName private String dokumentationsdatum;
  @CsvBindByName private String geraet;
  // Probenmaterial
  @CsvBindByName private String probenmaterial_identifikation;
  @CsvBindByName private String probenmaterial_abnahmezeitpunkt;
  @CsvBindByName private String probenmaterial_laboreingangszeitpunkt;
  @CsvBindByName private String probenmaterial_probenart;
  @CsvBindByName private String probenmaterial_koerperstelle;
  @CsvBindByName private String probenmaterial_kommentar;
  // Laboruntersuchung (Laboranalyse)
  @CsvBindByName private String laboruntersuchung_code;
  @CsvBindByName private String laboruntersuchung_identifikation;
  @CsvBindByName private String laboruntersuchung_status;
  // Laboruntersuchung - Bereich
  @CsvBindByName private String laborbereich_code;
  @CsvBindByName private String laborbereich_bezeichnung;
  // Laboruntersuchung - Gruppe
  @CsvBindByName private String laborgruppe_code;
  @CsvBindByName private String laborgruppe_bezeichnung;
  // Laboruntersuchung - Laborparameter
  @CsvBindByName private String laborparameter_bezeichnung;
  @CsvBindByName private String laboruntersuchung_klinisches_bezugsdatum;
  @CsvBindByName private String laboruntersuchung_dokumentationsdatum;
  @CsvBindByName private String laboruntersuchung_untersuchungszeitpunkt;
  @CsvBindByName private String laboruntersuchung_ergebnis;
  @CsvBindByName private String laboruntersuchung_bewertung;
  @CsvBindByName private String laboruntersuchung_kommentar;
  @CsvBindByName private String laboruntersuchung_untersuchungsmethode;
  // Laboruntersuchung - Probenmaterial
  @CsvBindByName private String laboruntersuchung_probenmaterial_identifikation;
  @CsvBindByName private String laboruntersuchung_probenmaterial_abnahmezeitpunkt;
  @CsvBindByName private String laboruntersuchung_probenmaterial_laboreingangszeitpunkt;
  @CsvBindByName private String laboruntersuchung_probenmaterial_probenart;
  @CsvBindByName private String laboruntersuchung_probenmaterial_koerperstelle;
  @CsvBindByName private String laboruntersuchung_probenmaterial_kommentar;
  @CsvBindByName private String laboruntersuchung_probenentnahme_methode;
  // Laboruntersuchung - Referenzbereich (Normalbereich)
  @CsvBindByName private String laboruntersuchung_referenzbereich_typ;
  @CsvBindByName private String laboruntersuchung_referenzbereich_obergrenze;
  @CsvBindByName private String laboruntersuchung_referenzbereich_untergrenze;
  // Laboranforderung
  @CsvBindByName private String laboranforderung_identifikation;
  @CsvBindByName private String laboranforderung_status;
  @CsvBindByName private String laboranforderung_anforderungsdatum;
  // Laboranforderung - Laborparameter
  @CsvBindByName private String laboranforderung_laborparameter_code;
  @CsvBindByName private String laboranforderung_laborparameter_bezeichnung;
  // Laboranforderung - Probenmaterial
  @CsvBindByName private String laboranforderung_probenmaterial_identifikation;
  @CsvBindByName private String laboranforderung_probenmaterial_abnahmezeitpunkt;
  @CsvBindByName private String laboranforderung_probenmaterial_laboreingangszeitpunkt;
  @CsvBindByName private String laboranforderung_probenmaterial_probenart;
  @CsvBindByName private String laboranforderung_probenmaterial_koerperstelle;
  @CsvBindByName private String laboranforderung_probenmaterial_kommentar;

  @Override
  public List<Resource> toFhirResources() {
    // TODO: Specimen needs to be defined
    return Helper.listOf(
        this.getDiagnosticReport(), this.getObservation(), this.getServiceRequest());
  }

  public DiagnosticReport getDiagnosticReport() {
    DiagnosticReport diagnosticReport = new DiagnosticReport();
    // Meta
    diagnosticReport.setMeta(this.getDiagnosticReportMeta());
    // Identifier Befund
    diagnosticReport.addIdentifier(this.getDiagnosticReportBefund());
    // BasedOn
    diagnosticReport.addBasedOn(this.getDiagnosticReportBasedOn());
    // Status
    diagnosticReport.setStatus(this.getDiagnosticReportStatus());
    // Category
    diagnosticReport.addCategory(this.getDiagnosticReportCategory());
    // Code
    diagnosticReport.setCode(this.getDiagnosticReportCode());
    // Subject
    diagnosticReport.setSubject(this.getDiagnosticReportSubject());
    // Effective
    diagnosticReport.setEffective(this.getDiagnosticReportEffective());
    // Issued
    diagnosticReport.setIssued(this.getDiagnosticReportIssued());
    // Specimen (optional)
    diagnosticReport.addSpecimen(this.getDiagnosticReportSpecimen());
    return diagnosticReport;
  }

  public Reference getDiagnosticReportBasedOn() {
    // TODO: Where is initial Service Request coming from?
    String ref = MIIReference.MII_SERVICE_REQUEST;
    return FhirGenerator.reference(ref);
  }

  public Observation getObservation() {
    Observation observation = new Observation();
    // Meta
    observation.setMeta(this.getObservationMeta());
    // Identifier
    observation.addIdentifier(this.getObservationIdentifier());
    // Status
    observation.setStatus(this.getObservationStatus());
    // Category
    observation.addCategory(this.getObservationCategory());
    // Code
    observation.setCode(this.getObservationCode());
    // Subject
    observation.setSubject(this.getObservationSubject());
    // Effective
    observation.setEffective(this.getObservationEffective());
    // Issued (optional)
    observation.setIssued(this.getObservationIssued());
    // Value
    observation.setValue(this.getObservationValue());
    // Interpretation (optional)
    observation.addInterpretation(this.getObservationInterpretation());
    // Note (optional)
    observation.addNote(this.getObservationNote());
    // Method (optional)
    observation.setMethod(this.getObservationMethod());
    // Reference Range (optional)
    observation.addReferenceRange(this.getObservationReferenceRange());
    return observation;
  }

  public ServiceRequest getServiceRequest() {
    ServiceRequest serviceRequest = new ServiceRequest();
    // Meta
    serviceRequest.setMeta(this.getServiceRequestMeta());
    // Identifier
    serviceRequest.addIdentifier(this.getServiceRequestIdentifier());
    // Status
    serviceRequest.setStatus(this.getServiceRequestStatus());
    // Intent
    serviceRequest.setIntent(this.getServiceRequestIntent());
    // Category
    serviceRequest.addCategory(this.getServiceRequestCategory());
    // Code
    serviceRequest.setCode(this.getServiceRequestCode());
    // Subject
    serviceRequest.setSubject(this.getServiceRequestSubject());
    // AuthoredOn
    serviceRequest.setAuthoredOn(this.getServiceRequestAuthoredOn());
    // Specimen (optional)
    serviceRequest.addSpecimen(this.getServiceRequestSpecimen());
    return serviceRequest;
  }

  // TODO: How does Specimen look?
  public Specimen getSpecimen() {
    Specimen specimen = new Specimen();
    return specimen;
  }

  public Identifier getServiceRequestIdentifier() {
    String value = this.getIdentifikation();
    IdentifierTypeCode plac = IdentifierTypeCode.PLAC;
    LoggingData data = LoggingData.of(LOGGER, "getServiceRequestIdentifier", "identifikation");
    return identifierWithCoding(value, plac, data);
  }

  // Reference is only set if it links to an existing Specimen-Resource. This can't be known here.
  public Reference getServiceRequestSpecimen() {
    return Constants.getEmptyValue();
  }

  public Date getServiceRequestAuthoredOn() {
    String anforderungsdatum = this.getLaboranforderung_anforderungsdatum();
    LoggingData data =
        LoggingData.of(LOGGER, "getServiceRequestAuthoredOn", "laboranforderung_anforderungsdatum");
    return date(anforderungsdatum, data);
  }

  public Reference getServiceRequestSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getServiceRequestCode() {
    String code = this.getLaboranforderung_laborparameter_code();
    String text = this.getServiceRequestCodeText();
    LoggingData data =
        LoggingData.of(LOGGER, "getServiceRequestCode", "laboranforderung_laborparameter_code");
    return codeWithOptionalText(code, text, data);
  }

  public String getServiceRequestCodeText() {
    String bezeichnung = this.getLaboranforderung_laborparameter_bezeichnung();
    if (Helper.checkEmptyString(bezeichnung)) {
      return Constants.getEmptyValue();
    }
    return bezeichnung;
  }

  public CodeableConcept getServiceRequestCategory() {
    String code = CodingCode.LABORATORY;
    String system = CodingSystem.TERMINOLOGY_OBSERVATION_VATEGORY;
    Coding laboratory = FhirGenerator.coding(code, system);
    return FhirGenerator.codeableConcept(laboratory);
  }

  public ServiceRequest.ServiceRequestIntent getServiceRequestIntent() {
    return ServiceRequest.ServiceRequestIntent.ORDER;
  }

  public ServiceRequest.ServiceRequestStatus getServiceRequestStatus() {
    return ServiceRequest.ServiceRequestStatus.COMPLETED;
  }

  public Meta getServiceRequestMeta() {
    String profile = MetaProfile.LABOR_SERVICE_REQUEST;
    String source = MetaSource.LABOR_SERVICE_REQUEST;
    String versionId = MetaVersionId.LABOR_SERVICE_REQUEST;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Reference getDiagnosticReportSpecimen() {
    String type = ReferenceType.SPECIMEN;
    String id = this.getProbenmaterial_identifikation();
    return optionalReferenceWithIdentifier(type, id);
  }

  public Date getDiagnosticReportIssued() {
    String dokumentationsdatum = this.getDokumentationsdatum();
    LoggingData data = LoggingData.of(LOGGER, "getDiagnosticReportIssued", "dokumentationsdatum");
    return date(dokumentationsdatum, data);
  }

  public DateTimeType getDiagnosticReportEffective() {
    String abnahmezeitpunkt = this.getProbenmaterial_abnahmezeitpunkt();
    // Abnahmezeitpunkt is general value needed
    if (Helper.checkNonEmptyString(abnahmezeitpunkt)) {
      LoggingData data =
          LoggingData.of(LOGGER, "getDiagnosticReportEffective", "probenmaterial_abnahmezeitpunkt");
      return dateTimeType(abnahmezeitpunkt, data);
    } else { // Laboreingangszeitpunkt is set if Abnahmezeitpunkt isn't present
      String laboreingangszeitpunkt = this.getProbenmaterial_laboreingangszeitpunkt();
      LoggingData data =
          LoggingData.of(
              LOGGER, "getDiagnosticReportEffective", "probenmaterial_laboreingangszeitpunkt");
      return dateTimeType(laboreingangszeitpunkt, data);
    }
  }

  public Reference getDiagnosticReportSubject() {
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public CodeableConcept getDiagnosticReportCode() {
    return FhirGenerator.codeableConcept(this.getDiagnosticReportLabReport());
  }

  public Coding getDiagnosticReportLabReport() {
    String code = CodingCode.LOINC_LAB_REPORT;
    String system = CodingSystem.LOINC;
    return FhirGenerator.coding(code, system);
  }

  public DiagnosticReport.DiagnosticReportStatus getDiagnosticReportStatus() {
    String code = this.getStatus();
    ParsedCode parsedCode = ParsedCode.fromString(code);
    return FhirHelper.getDiagnosticReportStatusFromString(parsedCode.getCode())
        .orElseGet(LOGGER.errorSupplier("getDiagnosticReportStatus", "status", code));
  }

  public CodeableConcept getDiagnosticReportCategory() {
    return FhirGenerator.codeableConcept(
        this.getDiagnosticReportLoincLab(), this.getDiagnosticReportServiceSection());
  }

  public Coding getDiagnosticReportLoincLab() {
    String code = CodingCode.LOINC_LAB;
    String system = CodingSystem.LOINC;
    return FhirGenerator.coding(code, system);
  }

  public Coding getDiagnosticReportServiceSection() {
    String code = CodingCode.LAB_DIAGNOSTIC_REPORT;
    String system = CodingSystem.DIAGNOSTIC_SERVICE_SECTION;
    return FhirGenerator.coding(code, system);
  }

  public Identifier getDiagnosticReportBefund() {
    String value = this.getIdentifikation();
    IdentifierTypeCode fill = IdentifierTypeCode.FILL;
    String ref = MIIReference.MII_ORGANIZATION;
    LoggingData data = LoggingData.of(LOGGER, "getDiagnosticReportBefund", "identifikation");
    return identifierWithCodeAndReference(value, fill, ref, data);
  }

  public Meta getDiagnosticReportMeta() {
    return FhirGenerator.meta(
        MetaProfile.LABOR_DIAGNOSTIC_REPORT,
        MetaSource.LABOR_DIAGNOSTIC_REPORT,
        MetaVersionId.LABOR_DIAGNOSTIC_REPORT);
  }

  public Observation.ObservationReferenceRangeComponent getObservationReferenceRange() {
    Quantity untergrenze = this.getObservationReferenceRangeLow();
    Quantity obergrenze = this.getObservationReferenceRangeHigh();
    CodeableConcept typ = this.getObservationReferenceRangeType();
    if (Helper.checkAllNull(untergrenze, obergrenze, typ)) {
      return Constants.getEmptyValue();
    }
    Observation.ObservationReferenceRangeComponent range =
        new Observation.ObservationReferenceRangeComponent();
    range.setLow(untergrenze);
    range.setHigh(obergrenze);
    range.setType(typ);
    return range;
  }

  public CodeableConcept getObservationReferenceRangeType() {
    String code = this.getLaboruntersuchung_referenzbereich_typ();
    LoggingData data =
        LoggingData.of(
            LOGGER, "getObservationReferenceRangeType", "laboruntersuchung_referenzbereich_typ");
    return optionalCodeFromValueSet(code, ReferenceRangeMeaning::fromCode, data);
  }

  public Quantity getObservationReferenceRangeLow() {
    String lower = this.getLaboruntersuchung_referenzbereich_untergrenze();
    LoggingData data =
        LoggingData.of(
            LOGGER,
            "getObservationReferenceRangeLow",
            "laboruntersuchung_referenzbereich_untergrenze");
    return optionalQuantity(lower, data);
  }

  public Quantity getObservationReferenceRangeHigh() {
    String higher = this.getLaboruntersuchung_referenzbereich_obergrenze();
    LoggingData data =
        LoggingData.of(
            LOGGER,
            "getObservationReferenceRangeHigh",
            "laboruntersuchung_referenzbereich_obergrenze");
    return optionalQuantity(higher, data);
  }

  public CodeableConcept getObservationMethod() {
    String code = this.getLaboruntersuchung_untersuchungsmethode();
    return optionalCode(code);
  }

  public Annotation getObservationNote() {
    String kommentar = this.getLaboruntersuchung_kommentar();
    return optionalAnnotation(kommentar);
  }

  public CodeableConcept getObservationInterpretation() {
    String interpretation = this.getLaboruntersuchung_bewertung();
    LoggingData data =
        LoggingData.of(LOGGER, "getObservationInterpretation", "laboruntersuchung_bewertung");
    return optionalCodeFromValueSet(interpretation, ObservationInterpretation::fromCode, data);
  }

  public Type getObservationValue() {
    CodeableConcept semiQuantitative = this.getObservationValueCodeableConcept();
    return Constants.isEmptyValue(semiQuantitative)
        ? this.getObservationValueQuantity()
        : semiQuantitative;
  }

  public CodeableConcept getObservationValueCodeableConcept() {
    String code = this.getLaboruntersuchung_ergebnis();
    return optionalCodeFromValueSet(code, SemiQuantitativesLaborergebnis::fromCode);
  }

  public Quantity getObservationValueQuantity() {
    String ergebnis = this.getLaboruntersuchung_ergebnis();
    LoggingData data = LoggingData.of(LOGGER, "getObservationValue", "laboruntersuchung_ergebnis");
    // ergebnis is treated as required here, because if both ValueCodeableConcept and this is empty
    // an error would be missed, since Value is required and 1 has to be set. Therefore
    // ValueCodeableConcept needs to be checked first.
    return quantity(ergebnis, data);
  }

  public Date getObservationIssued() {
    String dokumentationsdatum = this.getLaboruntersuchung_dokumentationsdatum();
    LoggingData data =
        LoggingData.of(LOGGER, "getObservationIssued", "laboruntersuchung_dokumentationsdatum");
    return optionalDate(dokumentationsdatum, data);
  }

  // TODO: Add Extension with Specimen or laboratory datetime
  public DateTimeType getObservationEffective() {
    String abnahmezeitpunkt = this.getProbenmaterial_abnahmezeitpunkt();
    // Abnahmezeitpunkt is general value needed
    if (Helper.checkNonEmptyString(abnahmezeitpunkt)) {
      LoggingData data =
          LoggingData.of(LOGGER, "getDiagnosticReportEffective", "probenmaterial_abnahmezeitpunkt");
      Extension specimen = this.getObservationEffectiveSpecimen();
      return dateTimeTypeWithExtension(abnahmezeitpunkt, data, specimen);
    } else { // Laboreingangszeitpunkt is set if Abnahmezeitpunkt isn't present
      String laboreingangszeitpunkt = this.getProbenmaterial_laboreingangszeitpunkt();
      LoggingData data =
          LoggingData.of(
              LOGGER, "getDiagnosticReportEffective", "probenmaterial_laboreingangszeitpunkt");
      Extension laboratory = this.getObservationEffectiveLaboratory();
      return dateTimeTypeWithExtension(laboreingangszeitpunkt, data, laboratory);
    }
  }

  public Extension getObservationEffectiveSpecimen() {
    QuelleKlinischesBezugsdatum specimen = QuelleKlinischesBezugsdatum.SPECIMEN;
    String url = ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM;
    return extensionWithCoding(specimen, url);
  }

  public Extension getObservationEffectiveLaboratory() {
    QuelleKlinischesBezugsdatum lab = QuelleKlinischesBezugsdatum.LABORATORY;
    String url = ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM;
    return extensionWithCoding(lab, url);
  }

  public Reference getObservationSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getObservationCode() {
    String code = this.getLaboruntersuchung_code();
    String system = CodingSystem.LOINC;
    String text = this.getLaborparameter_bezeichnung();
    LoggingData data = LoggingData.of(LOGGER, "getObservationCode", "laborparameter_bezeichnung");
    return codeFromSystemWithOptionalText(code, system, text, data);
  }

  public CodeableConcept getObservationCategory() {
    Coding loinc = this.getObservationCategoryLoinc();
    Coding category = this.getObservationCategoryCategory();
    Coding bereich = this.getObservationCategoryBereich();
    Coding gruppe = this.getObservationCategoryGruppen();
    return FhirGenerator.codeableConcept(loinc, category, bereich, gruppe);
  }

  public Coding getObservationCategoryGruppen() {
    String code = this.getLaborgruppe_code();
    LoggingData data = LoggingData.of(LOGGER, "getObservationCategoryGruppen", "laborgruppe_code");
    return optionalCodingFromValueSet(code, Laborstruktur::fromCode, data);
  }

  public Coding getObservationCategoryBereich() {
    String code = this.getLaborbereich_code();
    LoggingData data = LoggingData.of(LOGGER, "getObservationCategoryBereich", "laborbereich_code");
    return optionalCodingFromValueSet(code, Laborbereich::fromCode, data);
  }

  public Coding getObservationCategoryCategory() {
    String code = CodingCode.LABORATORY;
    String system = CodingSystem.OBSERVATION_CATEGORY_TERMINOLOGY;
    return FhirGenerator.coding(code, system);
  }

  public Coding getObservationCategoryLoinc() {
    String code = CodingCode.LOINC_LAB;
    String system = CodingSystem.LOINC;
    return FhirGenerator.coding(code, system);
  }

  public Observation.ObservationStatus getObservationStatus() {
    String code = this.getLaboruntersuchung_status();
    ParsedCode parsedCode = ParsedCode.fromString(code);
    return FhirHelper.getObservationStatusFromCode(parsedCode.getCode())
        .orElseGet(LOGGER.errorSupplier("getObservationStatus", "laboruntersuchung_status", code));
  }

  public Identifier getObservationIdentifier() {
    String value = this.getLaboruntersuchung_identifikation();
    IdentifierTypeCode obi = IdentifierTypeCode.OBI;
    String ref = MIIReference.MII_ORGANIZATION;
    LoggingData data =
        LoggingData.of(LOGGER, "getObservationIdentifier", "laboruntersuchung_identifikation");
    return identifierWithCodeAndReference(value, obi, ref, data);
  }

  public Meta getObservationMeta() {
    String profile = MetaProfile.LABOR_OBSERVATION;
    String source = MetaSource.LABOR_OBSERVATION;
    String versionId = MetaVersionId.LABOR_OBSERVATION;
    return FhirGenerator.meta(profile, source, versionId);
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

  public String getKlinisches_bezugsdatum() {
    return klinisches_bezugsdatum;
  }

  public void setKlinisches_bezugsdatum(String klinisches_bezugsdatum) {
    this.klinisches_bezugsdatum = klinisches_bezugsdatum;
  }

  public String getDokumentationsdatum() {
    return dokumentationsdatum;
  }

  public void setDokumentationsdatum(String dokumentationsdatum) {
    this.dokumentationsdatum = dokumentationsdatum;
  }

  public String getGeraet() {
    return geraet;
  }

  public void setGeraet(String geraet) {
    this.geraet = geraet;
  }

  public String getProbenmaterial_identifikation() {
    return probenmaterial_identifikation;
  }

  public void setProbenmaterial_identifikation(String probenmaterial_identifikation) {
    this.probenmaterial_identifikation = probenmaterial_identifikation;
  }

  public String getProbenmaterial_abnahmezeitpunkt() {
    return probenmaterial_abnahmezeitpunkt;
  }

  public void setProbenmaterial_abnahmezeitpunkt(String probenmaterial_abnahmezeitpunkt) {
    this.probenmaterial_abnahmezeitpunkt = probenmaterial_abnahmezeitpunkt;
  }

  public String getProbenmaterial_laboreingangszeitpunkt() {
    return probenmaterial_laboreingangszeitpunkt;
  }

  public void setProbenmaterial_laboreingangszeitpunkt(
      String probenmaterial_laboreingangszeitpunkt) {
    this.probenmaterial_laboreingangszeitpunkt = probenmaterial_laboreingangszeitpunkt;
  }

  public String getProbenmaterial_probenart() {
    return probenmaterial_probenart;
  }

  public void setProbenmaterial_probenart(String probenmaterial_probenart) {
    this.probenmaterial_probenart = probenmaterial_probenart;
  }

  public String getProbenmaterial_koerperstelle() {
    return probenmaterial_koerperstelle;
  }

  public void setProbenmaterial_koerperstelle(String probenmaterial_koerperstelle) {
    this.probenmaterial_koerperstelle = probenmaterial_koerperstelle;
  }

  public String getProbenmaterial_kommentar() {
    return probenmaterial_kommentar;
  }

  public void setProbenmaterial_kommentar(String probenmaterial_kommentar) {
    this.probenmaterial_kommentar = probenmaterial_kommentar;
  }

  public String getLaboruntersuchung_code() {
    return laboruntersuchung_code;
  }

  public void setLaboruntersuchung_code(String laboruntersuchung_code) {
    this.laboruntersuchung_code = laboruntersuchung_code;
  }

  public String getLaboruntersuchung_identifikation() {
    return laboruntersuchung_identifikation;
  }

  public void setLaboruntersuchung_identifikation(String laboruntersuchung_identifikation) {
    this.laboruntersuchung_identifikation = laboruntersuchung_identifikation;
  }

  public String getLaboruntersuchung_status() {
    return laboruntersuchung_status;
  }

  public void setLaboruntersuchung_status(String laboruntersuchung_status) {
    this.laboruntersuchung_status = laboruntersuchung_status;
  }

  public String getLaborbereich_code() {
    return laborbereich_code;
  }

  public void setLaborbereich_code(String laborbereich_code) {
    this.laborbereich_code = laborbereich_code;
  }

  public String getLaborbereich_bezeichnung() {
    return laborbereich_bezeichnung;
  }

  public void setLaborbereich_bezeichnung(String laborbereich_bezeichnung) {
    this.laborbereich_bezeichnung = laborbereich_bezeichnung;
  }

  public String getLaborgruppe_code() {
    return laborgruppe_code;
  }

  public void setLaborgruppe_code(String laborgruppe_code) {
    this.laborgruppe_code = laborgruppe_code;
  }

  public String getLaborgruppe_bezeichnung() {
    return laborgruppe_bezeichnung;
  }

  public void setLaborgruppe_bezeichnung(String laborgruppe_bezeichnung) {
    this.laborgruppe_bezeichnung = laborgruppe_bezeichnung;
  }

  public String getLaborparameter_bezeichnung() {
    return laborparameter_bezeichnung;
  }

  public void setLaborparameter_bezeichnung(String laborparameter_bezeichnung) {
    this.laborparameter_bezeichnung = laborparameter_bezeichnung;
  }

  public String getLaboruntersuchung_klinisches_bezugsdatum() {
    return laboruntersuchung_klinisches_bezugsdatum;
  }

  public void setLaboruntersuchung_klinisches_bezugsdatum(
      String laboruntersuchung_klinisches_bezugsdatum) {
    this.laboruntersuchung_klinisches_bezugsdatum = laboruntersuchung_klinisches_bezugsdatum;
  }

  public String getLaboruntersuchung_dokumentationsdatum() {
    return laboruntersuchung_dokumentationsdatum;
  }

  public void setLaboruntersuchung_dokumentationsdatum(
      String laboruntersuchung_dokumentationsdatum) {
    this.laboruntersuchung_dokumentationsdatum = laboruntersuchung_dokumentationsdatum;
  }

  public String getLaboruntersuchung_untersuchungszeitpunkt() {
    return laboruntersuchung_untersuchungszeitpunkt;
  }

  public void setLaboruntersuchung_untersuchungszeitpunkt(
      String laboruntersuchung_untersuchungszeitpunkt) {
    this.laboruntersuchung_untersuchungszeitpunkt = laboruntersuchung_untersuchungszeitpunkt;
  }

  public String getLaboruntersuchung_ergebnis() {
    return laboruntersuchung_ergebnis;
  }

  public void setLaboruntersuchung_ergebnis(String laboruntersuchung_ergebnis) {
    this.laboruntersuchung_ergebnis = laboruntersuchung_ergebnis;
  }

  public String getLaboruntersuchung_bewertung() {
    return laboruntersuchung_bewertung;
  }

  public void setLaboruntersuchung_bewertung(String laboruntersuchung_bewertung) {
    this.laboruntersuchung_bewertung = laboruntersuchung_bewertung;
  }

  public String getLaboruntersuchung_kommentar() {
    return laboruntersuchung_kommentar;
  }

  public void setLaboruntersuchung_kommentar(String laboruntersuchung_kommentar) {
    this.laboruntersuchung_kommentar = laboruntersuchung_kommentar;
  }

  public String getLaboruntersuchung_untersuchungsmethode() {
    return laboruntersuchung_untersuchungsmethode;
  }

  public void setLaboruntersuchung_untersuchungsmethode(
      String laboruntersuchung_untersuchungsmethode) {
    this.laboruntersuchung_untersuchungsmethode = laboruntersuchung_untersuchungsmethode;
  }

  public String getLaboruntersuchung_probenmaterial_identifikation() {
    return laboruntersuchung_probenmaterial_identifikation;
  }

  public void setLaboruntersuchung_probenmaterial_identifikation(
      String laboruntersuchung_probenmaterial_identifikation) {
    this.laboruntersuchung_probenmaterial_identifikation =
        laboruntersuchung_probenmaterial_identifikation;
  }

  public String getLaboruntersuchung_probenmaterial_abnahmezeitpunkt() {
    return laboruntersuchung_probenmaterial_abnahmezeitpunkt;
  }

  public void setLaboruntersuchung_probenmaterial_abnahmezeitpunkt(
      String laboruntersuchung_probenmaterial_abnahmezeitpunkt) {
    this.laboruntersuchung_probenmaterial_abnahmezeitpunkt =
        laboruntersuchung_probenmaterial_abnahmezeitpunkt;
  }

  public String getLaboruntersuchung_probenmaterial_laboreingangszeitpunkt() {
    return laboruntersuchung_probenmaterial_laboreingangszeitpunkt;
  }

  public void setLaboruntersuchung_probenmaterial_laboreingangszeitpunkt(
      String laboruntersuchung_probenmaterial_laboreingangszeitpunkt) {
    this.laboruntersuchung_probenmaterial_laboreingangszeitpunkt =
        laboruntersuchung_probenmaterial_laboreingangszeitpunkt;
  }

  public String getLaboruntersuchung_probenmaterial_probenart() {
    return laboruntersuchung_probenmaterial_probenart;
  }

  public void setLaboruntersuchung_probenmaterial_probenart(
      String laboruntersuchung_probenmaterial_probenart) {
    this.laboruntersuchung_probenmaterial_probenart = laboruntersuchung_probenmaterial_probenart;
  }

  public String getLaboruntersuchung_probenmaterial_koerperstelle() {
    return laboruntersuchung_probenmaterial_koerperstelle;
  }

  public void setLaboruntersuchung_probenmaterial_koerperstelle(
      String laboruntersuchung_probenmaterial_koerperstelle) {
    this.laboruntersuchung_probenmaterial_koerperstelle =
        laboruntersuchung_probenmaterial_koerperstelle;
  }

  public String getLaboruntersuchung_probenmaterial_kommentar() {
    return laboruntersuchung_probenmaterial_kommentar;
  }

  public void setLaboruntersuchung_probenmaterial_kommentar(
      String laboruntersuchung_probenmaterial_kommentar) {
    this.laboruntersuchung_probenmaterial_kommentar = laboruntersuchung_probenmaterial_kommentar;
  }

  public String getLaboruntersuchung_probenentnahme_methode() {
    return laboruntersuchung_probenentnahme_methode;
  }

  public void setLaboruntersuchung_probenentnahme_methode(
      String laboruntersuchung_probenentnahme_methode) {
    this.laboruntersuchung_probenentnahme_methode = laboruntersuchung_probenentnahme_methode;
  }

  public String getLaboruntersuchung_referenzbereich_typ() {
    return laboruntersuchung_referenzbereich_typ;
  }

  public void setLaboruntersuchung_referenzbereich_typ(
      String laboruntersuchung_referenzbereich_typ) {
    this.laboruntersuchung_referenzbereich_typ = laboruntersuchung_referenzbereich_typ;
  }

  public String getLaboruntersuchung_referenzbereich_obergrenze() {
    return laboruntersuchung_referenzbereich_obergrenze;
  }

  public void setLaboruntersuchung_referenzbereich_obergrenze(
      String laboruntersuchung_referenzbereich_obergrenze) {
    this.laboruntersuchung_referenzbereich_obergrenze =
        laboruntersuchung_referenzbereich_obergrenze;
  }

  public String getLaboruntersuchung_referenzbereich_untergrenze() {
    return laboruntersuchung_referenzbereich_untergrenze;
  }

  public void setLaboruntersuchung_referenzbereich_untergrenze(
      String laboruntersuchung_referenzbereich_untergrenze) {
    this.laboruntersuchung_referenzbereich_untergrenze =
        laboruntersuchung_referenzbereich_untergrenze;
  }

  public String getLaboranforderung_identifikation() {
    return laboranforderung_identifikation;
  }

  public void setLaboranforderung_identifikation(String laboranforderung_identifikation) {
    this.laboranforderung_identifikation = laboranforderung_identifikation;
  }

  public String getLaboranforderung_status() {
    return laboranforderung_status;
  }

  public void setLaboranforderung_status(String laboranforderung_status) {
    this.laboranforderung_status = laboranforderung_status;
  }

  public String getLaboranforderung_anforderungsdatum() {
    return laboranforderung_anforderungsdatum;
  }

  public void setLaboranforderung_anforderungsdatum(String laboranforderung_anforderungsdatum) {
    this.laboranforderung_anforderungsdatum = laboranforderung_anforderungsdatum;
  }

  public String getLaboranforderung_laborparameter_code() {
    return laboranforderung_laborparameter_code;
  }

  public void setLaboranforderung_laborparameter_code(String laboranforderung_laborparameter_code) {
    this.laboranforderung_laborparameter_code = laboranforderung_laborparameter_code;
  }

  public String getLaboranforderung_laborparameter_bezeichnung() {
    return laboranforderung_laborparameter_bezeichnung;
  }

  public void setLaboranforderung_laborparameter_bezeichnung(
      String laboranforderung_laborparameter_bezeichnung) {
    this.laboranforderung_laborparameter_bezeichnung = laboranforderung_laborparameter_bezeichnung;
  }

  public String getLaboranforderung_probenmaterial_identifikation() {
    return laboranforderung_probenmaterial_identifikation;
  }

  public void setLaboranforderung_probenmaterial_identifikation(
      String laboranforderung_probenmaterial_identifikation) {
    this.laboranforderung_probenmaterial_identifikation =
        laboranforderung_probenmaterial_identifikation;
  }

  public String getLaboranforderung_probenmaterial_abnahmezeitpunkt() {
    return laboranforderung_probenmaterial_abnahmezeitpunkt;
  }

  public void setLaboranforderung_probenmaterial_abnahmezeitpunkt(
      String laboranforderung_probenmaterial_abnahmezeitpunkt) {
    this.laboranforderung_probenmaterial_abnahmezeitpunkt =
        laboranforderung_probenmaterial_abnahmezeitpunkt;
  }

  public String getLaboranforderung_probenmaterial_laboreingangszeitpunkt() {
    return laboranforderung_probenmaterial_laboreingangszeitpunkt;
  }

  public void setLaboranforderung_probenmaterial_laboreingangszeitpunkt(
      String laboranforderung_probenmaterial_laboreingangszeitpunkt) {
    this.laboranforderung_probenmaterial_laboreingangszeitpunkt =
        laboranforderung_probenmaterial_laboreingangszeitpunkt;
  }

  public String getLaboranforderung_probenmaterial_probenart() {
    return laboranforderung_probenmaterial_probenart;
  }

  public void setLaboranforderung_probenmaterial_probenart(
      String laboranforderung_probenmaterial_probenart) {
    this.laboranforderung_probenmaterial_probenart = laboranforderung_probenmaterial_probenart;
  }

  public String getLaboranforderung_probenmaterial_koerperstelle() {
    return laboranforderung_probenmaterial_koerperstelle;
  }

  public void setLaboranforderung_probenmaterial_koerperstelle(
      String laboranforderung_probenmaterial_koerperstelle) {
    this.laboranforderung_probenmaterial_koerperstelle =
        laboranforderung_probenmaterial_koerperstelle;
  }

  public String getLaboranforderung_probenmaterial_kommentar() {
    return laboranforderung_probenmaterial_kommentar;
  }

  public void setLaboranforderung_probenmaterial_kommentar(
      String laboranforderung_probenmaterial_kommentar) {
    this.laboranforderung_probenmaterial_kommentar = laboranforderung_probenmaterial_kommentar;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }
}
