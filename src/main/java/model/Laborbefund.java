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
    if (Helper.checkEmptyString(value)) {
      return LOGGER.emptyValue("getServiceRequestIdentifier", "identifikation");
    }
    IdentifierTypeCode plac = IdentifierTypeCode.PLAC;
    Coding placerv2 = FhirGenerator.coding(plac);
    String system = IdentifierSystem.EMPTY;
    return FhirGenerator.identifier(value, system, placerv2);
  }

  // Reference is only set if it links to an existing Specimen-Resource. This can't be known here.
  public Reference getServiceRequestSpecimen() {
    return Constants.getEmptyValue();
  }

  public Date getServiceRequestAuthoredOn() {
    String anforderungsdatum = this.getLaboranforderung_anforderungsdatum();
    return Helper.getDateFromISO(anforderungsdatum)
        .orElseGet(
            LOGGER.errorSupplier(
                "getServiceRequestAuthoredOn",
                "laboranforderung_anforderungsdatum",
                anforderungsdatum));
  }

  public Reference getServiceRequestSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getServiceRequestCode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboranforderung_laborparameter_code());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return LOGGER.emptyValue("getServiceRequestCode", "laboranforderung_laborparameter_code");
    }
    String system = parsedCode.getSystem();
    String display = parsedCode.getDisplay();
    Coding coding = FhirGenerator.coding(code, system, display);
    String text = this.getServiceRequestCodeText();
    return FhirGenerator.codeableConcept(coding).setText(text);
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
    String id = this.getProbenmaterial_identifikation();
    if (Helper.checkEmptyString(id)) {
      return Constants.getEmptyValue();
    }
    String type = ReferenceType.SPECIMEN;
    String system = IdentifierSystem.EMPTY;
    Identifier identifier = FhirGenerator.identifier(id, system);
    return FhirGenerator.reference(type, identifier);
  }

  public Date getDiagnosticReportIssued() {
    String dokumentationsdatum = this.getDokumentationsdatum();
    return Helper.getDateFromISO(dokumentationsdatum)
        .orElseGet(
            LOGGER.errorSupplier(
                "getDiagnosticReportIssued", "dokumentationsdatum", dokumentationsdatum));
  }

  public DateTimeType getDiagnosticReportEffective() {
    String abnahmezeitpunkt = this.getProbenmaterial_abnahmezeitpunkt();
    // Abnahmezeitpunkt is general value needed
    if (Helper.checkNonEmptyString(abnahmezeitpunkt)) {
      return Helper.getDateFromISO(abnahmezeitpunkt)
          .map(FhirGenerator::dateTimeType)
          .orElseGet(
              LOGGER.errorSupplier(
                  "getDiagnosticReportEffective",
                  "probenmaterial_abnahmezeitpunkt",
                  abnahmezeitpunkt));
    } else { // Laboreingangszeitpunkt is set if Abnahmezeitpunkt isn't present
      String laboreingangszeitpunkt = this.getProbenmaterial_laboreingangszeitpunkt();
      return Helper.getDateFromISO(laboreingangszeitpunkt)
          .map(FhirGenerator::dateTimeType)
          .orElseGet(
              LOGGER.errorSupplier(
                  "getDiagnosticReportEffective",
                  "Abnahmezeitpunkt or Laboreingangszeitpunkt have to be set!"));
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
    ParsedCode parsedCode = ParsedCode.fromString(this.getStatus());
    String code = parsedCode.getCode();
    return FhirHelper.getDiagnosticReportStatusFromString(code)
        .orElseGet(LOGGER.errorSupplier("getDiagnosticReportStatus", "status", code));
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
    return FhirGenerator.coding(code, system);
  }

  public Coding getDiagnosticReportServiceSection() {
    String code = CodingCode.LAB_DIAGNOSTIC_REPORT;
    String system = CodingSystem.DIAGNOSTIC_SERVICE_SECTION;
    return FhirGenerator.coding(code, system);
  }

  public Identifier getDiagnosticReportBefund() {
    String value = this.getIdentifikation();
    if (Helper.checkEmptyString(value)) {
      return LOGGER.emptyValue("getDiagnosticReportBefund", "identifikation");
    }
    String system = IdentifierSystem.EMPTY;
    IdentifierTypeCode fill = IdentifierTypeCode.FILL;
    Coding coding = FhirGenerator.coding(fill);
    CodeableConcept type = FhirGenerator.codeableConcept(coding);
    String ref = MIIReference.MII_ORGANIZATION;
    Reference assignerRef = FhirGenerator.reference(ref);
    return FhirGenerator.identifier(value, system, type, assignerRef);
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
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboruntersuchung_referenzbereich_typ());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    return ReferenceRangeMeaning.fromCode(code)
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationReferenceRangeType", "laboruntersuchung_referenzbereich_typ", code));
  }

  public Quantity getObservationReferenceRangeLow() {
    String lower = this.getLaboruntersuchung_referenzbereich_untergrenze();
    if (Helper.checkEmptyString(lower)) {
      return Constants.getEmptyValue();
    }
    return ParsedQuantity.fromString(lower)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationReferenceRangeLow",
                "laboruntersuchung_referenzbereich_untergrenze",
                lower));
  }

  public Quantity getObservationReferenceRangeHigh() {
    String higher = this.getLaboruntersuchung_referenzbereich_obergrenze();
    if (Helper.checkEmptyString(higher)) {
      return Constants.getEmptyValue();
    }
    return ParsedQuantity.fromString(higher)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationReferenceRangeHigh",
                "laboruntersuchung_referenzbereich_obergrenze",
                higher));
  }

  public CodeableConcept getObservationMethod() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboruntersuchung_untersuchungsmethode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = parsedCode.getSystem();
    String display = parsedCode.getDisplay();
    Coding method = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(method);
  }

  public Annotation getObservationNote() {
    String kommentar = this.getLaboruntersuchung_kommentar();
    if (Helper.checkEmptyString(kommentar)) {
      return Constants.getEmptyValue();
    }
    return new Annotation().setText(kommentar);
  }

  public Type getObservationValue() {
    CodeableConcept semiQuantitative = this.getObservationValueCodeableConcept();
    return semiQuantitative == Constants.getEmptyValue()
        ? this.getObservationValueQuantity()
        : semiQuantitative;
  }

  public CodeableConcept getObservationValueCodeableConcept() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboruntersuchung_ergebnis());
    return SemiQuantitativesLaborergebnis.fromCode(parsedCode.getCode())
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElse(Constants.getEmptyValue());
  }

  public Quantity getObservationValueQuantity() {
    String ergebnis = this.getLaboruntersuchung_ergebnis();
    // ergebnis is treated as required here, because if both ValueCodeableConcept and this is empty
    // an error would be missed, since Value is required and 1 has to be set. Therefore
    // ValueCodeableConcept needs to be checked first.
    return ParsedQuantity.fromString(ergebnis)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationValue",
                "laboruntersuchung_ergebnis has to be a semi-quantitative or whole quantity!"));
  }

  public Date getObservationIssued() {
    String dokumentationsdatum = this.getLaboruntersuchung_dokumentationsdatum();
    if (Helper.checkEmptyString(dokumentationsdatum)) {
      return Constants.getEmptyValue();
    }
    return Helper.getDateFromISO(dokumentationsdatum)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationIssued",
                "laboruntersuchung_dokumentationsdatum",
                dokumentationsdatum));
  }

  public DateTimeType getObservationEffective() {
    String untersuchungszeitpunkt = this.getLaboruntersuchung_untersuchungszeitpunkt();
    return Helper.getDateFromISO(untersuchungszeitpunkt)
        .map(FhirGenerator::dateTimeType)
        .orElseGet(
            LOGGER.errorSupplier(
                "getObservationEffective",
                "laboruntersuchung_untersuchungszeitpunkt",
                untersuchungszeitpunkt));
  }

  public Reference getObservationSubject() {
    return this.getDiagnosticReportSubject();
  }

  public CodeableConcept getObservationCode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboruntersuchung_code());
    String codingCode = parsedCode.getCode();
    if (Helper.checkEmptyString(codingCode)) {
      return LOGGER.emptyValue("getObservationCode", "laborparameter_bezeichnung");
    }
    String system = CodingSystem.LOINC;
    String display = parsedCode.getDisplay();
    Coding code = FhirGenerator.coding(codingCode, system, display);
    return FhirGenerator.codeableConcept(code).setText(this.getLaborparameter_bezeichnung());
  }

  public CodeableConcept getObservationCategory() {
    Coding loinc = this.getObservationCategoryLoinc();
    Coding category = this.getObservationCategoryCategory();
    Coding bereich = this.getObservationCategoryBereich();
    Coding gruppe = this.getObservationCategoryGruppen();
    return FhirGenerator.codeableConcept(loinc, category, bereich, gruppe);
  }

  public Coding getObservationCategoryGruppen() {
    ParsedCode parsedGruppe = ParsedCode.fromString(this.getLaborgruppe_code());
    String code = parsedGruppe.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    return Laborstruktur.fromCode(code)
        .map(FhirGenerator::coding)
        .orElseGet(LOGGER.errorSupplier("getObservationCategoryGruppen", "laborgruppe_code", code));
  }

  public Coding getObservationCategoryBereich() {
    ParsedCode parsedBereich = ParsedCode.fromString(this.getLaborbereich_code());
    String code = parsedBereich.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    return Laborbereich.fromCode(code)
        .map(FhirGenerator::coding)
        .orElseGet(
            LOGGER.errorSupplier("getObservationCategoryBereich", "laborbereich_code", code));
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

  // TODO: status von https://terminology.hl7.org/2.1.0/CodeSystem-v3-ActStatus.html oder doch
  // gleich observationstatus?
  public Observation.ObservationStatus getObservationStatus() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLaboruntersuchung_status());
    String code = parsedCode.getCode();
    try {
      return Observation.ObservationStatus.fromCode(code);
    } catch (Exception e) {
      return (Observation.ObservationStatus)
          LOGGER.errorSupplier("getObservationStatus", "laboruntersuchung_status", code).get();
    }
  }

  public Identifier getObservationIdentifier() {
    String value = this.getLaboruntersuchung_identifikation();
    if (Helper.checkEmptyString(value)) {
      return (Identifier)
          LOGGER
              .emptyValueSupplier("getObservationIdentifier", "laboruntersuchung_identifikation")
              .get();
    }
    IdentifierTypeCode codingCode = IdentifierTypeCode.OBI;
    Coding observationInstanceV2 = FhirGenerator.coding(codingCode);
    CodeableConcept type = FhirGenerator.codeableConcept(observationInstanceV2);
    String system = IdentifierSystem.EMPTY;
    String ref = MIIReference.MII_ORGANIZATION;
    Reference assignerRef = FhirGenerator.reference(ref);
    return FhirGenerator.identifier(value, system, type, assignerRef);
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
