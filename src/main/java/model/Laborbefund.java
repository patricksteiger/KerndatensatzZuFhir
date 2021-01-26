package model;

import constants.Constants;
import constants.*;
import enums.IdentifierTypeCode;
import enums.Laborbereich;
import helper.FhirHelper;
import helper.Helper;
import helper.ValueAndUnitParsed;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Laborbefund implements Datablock {
  private String patNr;
  private String identifikation;
  private String status;
  private String klinisches_bezugsdatum;
  private String dokumentationsdatum;
  private String geraet;
  // Probenmaterial
  private String probenmaterial_identifikation;
  private String probenmaterial_abnahmezeitpunkt;
  private String probenmaterial_laboreingangszeitpunkt;
  private String probenmaterial_probenart;
  private String probenmaterial_koerperstelle;
  private String probenmaterial_kommentar;
  // Laboruntersuchung (Laboranalyse)
  private String laboruntersuchung_code;
  private String laboruntersuchung_identifikation;
  private String laboruntersuchung_status;
  // Laboruntersuchung - Bereich
  private String laborbereich_code;
  private String laborbereich_bezeichnung;
  // Laboruntersuchung - Gruppe
  private String laborgruppe_code;
  private String laborgruppe_bezeichnung;
  // Laboruntersuchung - Laborparameter
  private String laborparameter_bezeichnung;
  private String laboruntersuchung_klinisches_bezugsdatum;
  private String laboruntersuchung_dokumentationsdatum;
  private String laboruntersuchung_untersuchungszeitpunkt;
  private String laboruntersuchung_ergebnis;
  private String laboruntersuchung_bewertung;
  private String laboruntersuchung_kommentar;
  private String laboruntersuchung_untersuchungsmethode;
  // Laboruntersuchung - Probenmaterial
  private String laboruntersuchung_probenmaterial_identifikation;
  private String laboruntersuchung_probenmaterial_abnahmezeitpunkt;
  private String laboruntersuchung_probenmaterial_laboreingangszeitpunkt;
  private String laboruntersuchung_probenmaterial_probenart;
  private String laboruntersuchung_probenmaterial_koerperstelle;
  private String laboruntersuchung_probenmaterial_kommentar;
  private String laboruntersuchung_probenentnahme_methode;
  // Laboruntersuchung - Referenzbereich (Normalbereich)
  private String laboruntersuchung_referenzbereich_typ;
  private String laboruntersuchung_referenzbereich_obergrenze;
  private String laboruntersuchung_referenzbereich_untergrenze;
  // Laboranforderung
  private String laboranforderung_identifikation;
  private String laboranforderung_status;
  private String laboranforderung_anforderungsdatum;
  // Laboranforderung - Laborparameter
  private String laboranforderung_laborparameter_code;
  private String laboranforderung_laborparameter_bezeichnung;
  // Laboranforderung - Probenmaterial
  private String laboranforderung_probenmaterial_identifikation;
  private String laboranforderung_probenmaterial_abnahmezeitpunkt;
  private String laboranforderung_probenmaterial_laboreingangszeitpunkt;
  private String laboranforderung_probenmaterial_probenart;
  private String laboranforderung_probenmaterial_koerperstelle;
  private String laboranforderung_probenmaterial_kommentar;

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
    // TODO: Was ist die initiale ServiceRequest für basedOn?
    // Status
    diagnosticReport.setStatus(this.getDiagnosticReportStatus());
    // Category
    diagnosticReport.addCategory(this.getDiagnosticReportCategory());
    // Code
    diagnosticReport.setCode(this.getDiagnosticReportCode());
    // Subject
    if (Helper.checkNonEmptyString(this.getPatNr()))
      diagnosticReport.setSubject(this.getDiagnosticReportSubject());
    // Effective
    diagnosticReport.setEffective(this.getDiagnosticReportEffective());
    // Issued
    diagnosticReport.setIssued(this.getDiagnosticReportIssued());
    // Specimen (optional)
    if (Helper.checkNonEmptyString(this.getProbenmaterial_identifikation()))
      diagnosticReport.addSpecimen(this.getDiagnosticReportSpecimen());
    return diagnosticReport;
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
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_dokumentationsdatum()))
      observation.setIssued(this.getObservationIssued());
    // Value
    observation.setValue(this.getObservationValue());
    // Note (optional)
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_kommentar()))
      observation.addNote(this.getObservationNote());
    // Method (optional)
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_untersuchungsmethode()))
      observation.setMethod(this.getObservationMethod());
    // reference Range (optional)
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_untergrenze())
        || Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_obergrenze())
        || Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_typ()))
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
    if (Helper.checkNonEmptyString(this.getLaboranforderung_probenmaterial_identifikation()))
      serviceRequest.addSpecimen(this.getServiceRequestSpecimen());
    return serviceRequest;
  }

  // TODO: How does Specimen look?
  public Specimen getSpecimen() {
    Specimen specimen = new Specimen();
    return specimen;
  }

  public Identifier getServiceRequestIdentifier() {
    String codingCode = CodingCode.PLAC;
    String codingSystem = CodingSystem.IDENTIFIER_TYPE;
    Coding placerv2 = FhirHelper.generateCoding(codingCode, codingSystem);
    String value = this.getIdentifikation();
    // FIXME: What is system of ServiceRequest identifier
    String system = "";
    return FhirHelper.generateIdentifier(value, system, placerv2);
  }

  public Reference getServiceRequestSpecimen() {
    String type = ReferenceType.SPECIMEN;
    // FIXME: What is system of ServiceRequest specimen?
    String system = "";
    Identifier identifier =
        FhirHelper.generateIdentifier(
            this.getLaboranforderung_probenmaterial_identifikation(), system);
    return FhirHelper.generateReference(type, identifier);
  }

  public Date getServiceRequestAuthoredOn() {
    return Helper.getDateFromISO(this.getLaboranforderung_anforderungsdatum());
  }

  public Reference getServiceRequestSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getServiceRequestCode() {
    String code = this.getLaboranforderung_laborparameter_code();
    String system = CodingSystem.LOINC;
    Coding coding = FhirHelper.generateCoding(code, system);
    CodeableConcept serviceCode = new CodeableConcept();
    serviceCode.addCoding(coding);
    if (Helper.checkNonEmptyString(this.getLaboranforderung_laborparameter_bezeichnung()))
      serviceCode.setText(this.getLaboranforderung_laborparameter_bezeichnung());
    return serviceCode;
  }

  public CodeableConcept getServiceRequestCategory() {
    String code = CodingCode.LABORATORY;
    String system = CodingSystem.TERMINOLOGY_OBSERVATION_VATEGORY;
    Coding laboratory = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(laboratory);
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
    return FhirHelper.generateMeta(profile, source, versionId);
  }

  public Reference getDiagnosticReportSpecimen() {
    String type = ReferenceType.SPECIMEN;
    // FIXME: What is system of DiagnosticReport specimen?
    String system = "";
    Identifier identifier =
        FhirHelper.generateIdentifier(this.getProbenmaterial_identifikation(), system);
    return FhirHelper.generateReference(type, identifier);
  }

  public Date getDiagnosticReportIssued() {
    return Helper.getDateFromISO(this.getDokumentationsdatum());
  }

  public DateTimeType getDiagnosticReportEffective() {
    Date date = null;
    if (Helper.checkNonEmptyString(this.getProbenmaterial_abnahmezeitpunkt()))
      date = Helper.getDateFromISO(this.getProbenmaterial_abnahmezeitpunkt());
    else date = Helper.getDateFromISO(this.getProbenmaterial_laboreingangszeitpunkt());
    return FhirHelper.generateDate(date);
  }

  public Reference getDiagnosticReportSubject() {
    String type = ReferenceType.PATIENT;
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirHelper.generateIdentifier(this.getPatNr(), IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(type, subjectId);
  }

  public CodeableConcept getDiagnosticReportCode() {
    return new CodeableConcept().addCoding(this.getDiagnosticReportLabReport());
  }

  public Coding getDiagnosticReportLabReport() {
    String code = CodingCode.LOINC_LAB_REPORT;
    String system = CodingSystem.LOINC;
    return FhirHelper.generateCoding(code, system);
  }

  public DiagnosticReport.DiagnosticReportStatus getDiagnosticReportStatus() {
    return FhirHelper.getDiagnosticReportStatusFromString(this.getStatus());
  }

  public CodeableConcept getDiagnosticReportCategory() {
    CodeableConcept category = new CodeableConcept();
    category.addCoding(this.getDiagnosticReportLoincLab());
    category.addCoding(this.getDiagnosticReportServiceSection());
    return category;
  }

  public Coding getDiagnosticReportLoincLab() {
    String code = CodingCode.LOINC_LAB;
    String system = CodingSystem.LOINC;
    return FhirHelper.generateCoding(code, system);
  }

  public Coding getDiagnosticReportServiceSection() {
    String code = CodingCode.LAB_DIAGNOSTIC_REPORT;
    String system = CodingSystem.DIAGNOSTIC_SERVICE_SECTION;
    return FhirHelper.generateCoding(code, system);
  }

  public Identifier getDiagnosticReportBefund() {
    String value = this.getIdentifikation();
    String system = IdentifierSystem.LOCAL_PID;
    Coding coding =
        FhirHelper.generateCoding(
            CodingCode.BEFUND_DIAGNOSTIC_REPORT, CodingSystem.IDENTIFIER_TYPE);
    CodeableConcept type = new CodeableConcept().addCoding(coding);
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    return FhirHelper.generateIdentifier(value, system, type, assignerRef);
  }

  public Meta getDiagnosticReportMeta() {
    return FhirHelper.generateMeta(
        MetaProfile.LABOR_DIAGNOSTIC_REPORT,
        MetaSource.LABOR_DIAGNOSTIC_REPORT,
        MetaVersionId.LABOR_DIAGNOSTIC_REPORT);
  }

  public Observation.ObservationReferenceRangeComponent getObservationReferenceRange() {
    Observation.ObservationReferenceRangeComponent range =
        new Observation.ObservationReferenceRangeComponent();
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_untergrenze()))
      range.setLow(this.getObservationReferenceRangeLow());
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_obergrenze()))
      range.setHigh(this.getObservationReferenceRangeHigh());
    if (Helper.checkNonEmptyString(this.getLaboruntersuchung_referenzbereich_typ()))
      range.setType(this.getObservationReferenceRangeType());
    return range;
  }

  public CodeableConcept getObservationReferenceRangeType() {
    String code = this.getLaboruntersuchung_referenzbereich_typ();
    String system = CodingSystem.REFERENCE_RANGE_MEANING;
    Coding coding = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(coding);
  }

  public Quantity getObservationReferenceRangeLow() {
    String lower = this.getLaboruntersuchung_referenzbereich_untergrenze();
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(lower);
    return FhirHelper.generateQuantity(
        parsed.getValue(), parsed.getUnit(), Constants.QUANTITY_SYSTEM, Constants.QUANTITY_CODE);
  }

  public Quantity getObservationReferenceRangeHigh() {
    String higher = this.getLaboruntersuchung_referenzbereich_obergrenze();
    ValueAndUnitParsed parsed = ValueAndUnitParsed.fromString(higher);
    return FhirHelper.generateQuantity(
        parsed.getValue(), parsed.getUnit(), Constants.QUANTITY_SYSTEM, Constants.QUANTITY_CODE);
  }

  public CodeableConcept getObservationMethod() {
    String code = this.getLaboruntersuchung_untersuchungsmethode();
    // FIXME: What is system of Observation method?
    String system = "";
    Coding method = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(method);
  }

  public Annotation getObservationNote() {
    Annotation note = new Annotation();
    note.setText(this.getLaboruntersuchung_kommentar());
    return note;
  }

  // TODO: Is there semi quantitive result? (0, +, ++, ...)
  public Quantity getObservationValue() {
    // TODO: How does the ergebnis really look?
    String[] valueQuantity = this.getLaboruntersuchung_ergebnis().split(" ");
    BigDecimal value = new BigDecimal(valueQuantity[0]);
    String unit = valueQuantity[1];
    String system = Constants.QUANTITY_SYSTEM;
    String code = Constants.QUANTITY_CODE;
    return FhirHelper.generateQuantity(value, unit, system, code);
  }

  public Date getObservationIssued() {
    return Helper.getDateFromISO(this.getLaboruntersuchung_dokumentationsdatum());
  }

  public DateTimeType getObservationEffective() {
    Date effective = Helper.getDateFromISO(this.getLaboruntersuchung_untersuchungszeitpunkt());
    return FhirHelper.generateDate(effective);
  }

  public Reference getObservationSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getObservationCode() {
    String codingCode = this.getLaborparameter_bezeichnung();
    String system = CodingSystem.LOINC;
    Coding code = FhirHelper.generateCoding(codingCode, system);
    return new CodeableConcept().addCoding(code);
  }

  public CodeableConcept getObservationCategory() {
    CodeableConcept category = new CodeableConcept();
    // LOINC
    Coding loincCoding = FhirHelper.generateCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC);
    category.addCoding(loincCoding);
    // Observation-Category
    Coding categoryCoding =
        FhirHelper.generateCoding(
            CodingCode.LABORATORY, CodingSystem.OBSERVATION_CATEGORY_TERMINOLOGY);
    category.addCoding(categoryCoding);
    // Laborbereichcode
    if (Helper.checkNonEmptyString(this.getLaborbereich_code())) {
      Laborbereich bereich = Laborbereich.fromCode(this.getLaborbereich_code());
      Coding bereichCoding =
          FhirHelper.generateCoding(bereich.getCode(), bereich.getSystem(), bereich.getDisplay());
      category.addCoding(bereichCoding);
    }
    // Laborgruppencode
    if (Helper.checkNonEmptyString(this.getLaborgruppe_code())) {
      Coding gruppenCoding =
          FhirHelper.generateCoding(this.getLaborgruppe_code(), CodingSystem.LABORGRUPPEN_CODE);
      category.addCoding(gruppenCoding);
    }
    return category;
  }

  public Observation.ObservationStatus getObservationStatus() {
    try {
      return Observation.ObservationStatus.fromCode(this.getLaboruntersuchung_status());
    } catch (Exception e) {
      throw Helper.illegalCode(this.getLaboruntersuchung_status(), "ObservationStatus").get();
    }
  }

  public Identifier getObservationIdentifier() {
    IdentifierTypeCode codingCode = IdentifierTypeCode.OBI;
    Coding observationInstanceV2 =
        FhirHelper.generateCoding(
            codingCode.getCode(), codingCode.getSystem(), codingCode.getDisplay());
    CodeableConcept type = new CodeableConcept().addCoding(observationInstanceV2);
    String value = this.getLaboruntersuchung_identifikation();
    // FIXME: What is system of observation identifier?
    String system = "";
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    return FhirHelper.generateIdentifier(value, system, type, assignerRef);
  }

  public Meta getObservationMeta() {
    String profile = MetaProfile.LABOR_OBSERVATION;
    String source = MetaSource.LABOR_OBSERVATION;
    String versionId = MetaVersionId.LABOR_OBSERVATION;
    return FhirHelper.generateMeta(profile, source, versionId);
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
