package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import enums.DurchfuehrungsabsichtCode;
import enums.ProcedureCategorySnomedMapping;
import enums.SeitenlokalisationCode;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.List;
import java.util.Optional;

public class Prozedur implements Datablock {
  private final Logger LOGGER = new Logger(Prozedur.class);
  @CsvBindByName private String patNr;
  // OPS Prozedur kodiert
  @CsvBindByName private String OPS_Vollst_Prozedurenkode;
  @CsvBindByName private String OPS_Seitenlokalisation;
  // SNOMED Prozedur kodiert
  @CsvBindByName private String SNOMED_Vollst_Prozedurenkode;
  @CsvBindByName private String durchfuehrungsabsicht;
  @CsvBindByName private String durchfuehrungsdatum;
  @CsvBindByName private String dokumentationsdatum;
  @CsvBindByName private String freitextbeschreibung;
  @CsvBindByName private String koerperstelle;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getProcedure());
  }

  public Procedure getProcedure() {
    Procedure procedure = new Procedure();
    // Meta
    procedure.setMeta(this.getMeta());
    // Status
    procedure.setStatus(this.getStatus());
    // Category, can only be set if OPS is used.
    procedure.setCategory(this.getCategory());
    // Code
    procedure.setCode(this.getCode());
    // Performed
    procedure.setPerformed(this.getPerformed());
    // BodySites (optional), currently includes Koerperstelle
    procedure.addBodySite(this.getBodySite());
    // Notes (optional), currently includes Freitextbeschreibung
    procedure.addNote(this.getNote());
    // Extension: RecordedDate (optional), only needed if Dokumentationsdatum != Durchfuehrungsdatum
    procedure.addExtension(this.getRecordedDate());
    // Extension: Durchfuehrungsabsicht (optional)
    procedure.addExtension(this.getDurchfuehrungsabsicht());
    // Subject
    procedure.setSubject(this.getSubject());
    return procedure;
  }

  public Procedure.ProcedureStatus getStatus() {
    return Procedure.ProcedureStatus.COMPLETED;
  }

  public Reference getSubject() {
    String patientenNummer = this.getPatNr();
    if (Helper.checkEmptyString(patientenNummer)) {
      LOGGER.error("getSubject", "patNr can't be empty!");
    }
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirGenerator.identifier(patientenNummer, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirGenerator.reference(subjectId);
  }

  public Meta getMeta() {
    return FhirGenerator.meta(
        MetaProfile.PROZEDUR_PROCEDURE,
        MetaSource.PROZEDUR_PROCEDURE,
        MetaVersionId.PROZEDUR_PROCEDURE);
  }

  /** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/81934/" */
  public CodeableConcept getCategory() {
    String ops = this.getOPS_Vollst_Prozedurenkode();
    ParsedCode parsedCode = ParsedCode.fromString(ops);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    return ProcedureCategorySnomedMapping.fromOpsCode(parsedCode.getCode())
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElseGet(LOGGER.errorSupplier("getCategory", "OPS_Vollst_Prozedurenkode", ops));
  }

  public CodeableConcept getCode() {
    Coding snomed = this.getCodingSnomed();
    Coding ops = this.getCodingOps();
    if (Helper.checkAllNull(snomed, ops)) {
      return LOGGER.error("getCode", "Either SNOMED- or OPS-Code need to be defined!");
    }
    return FhirGenerator.codeableConcept(snomed, ops);
  }

  public Coding getCodingOps() {
    String system = CodingSystem.OPS_DIMDI;
    ParsedCode parsedCode = ParsedCode.fromString(this.getOPS_Vollst_Prozedurenkode(), system);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    String version = Constants.VERSION_2020;
    Coding ops = FhirGenerator.coding(parsedCode, version);
    ops.addExtension(this.getSeitenlokalisation());
    return ops;
  }

  /** @see "https://simplifier.net/basisprofil-de-r4/extension-seitenlokalisation" */
  public Extension getSeitenlokalisation() {
    String code = this.getOPS_Seitenlokalisation();
    ParsedCode parsedCode = ParsedCode.fromString(code);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    String url = ExtensionUrl.OPS_SEITENLOKALISATION;
    return SeitenlokalisationCode.fromCode(parsedCode.getCode())
        .map(FhirGenerator::coding)
        .map(c -> FhirGenerator.extension(url, c))
        .orElseGet(LOGGER.errorSupplier("getSeitenlokalisation", "OPS_Seitenlokalisation", code));
  }

  public Coding getCodingSnomed() {
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    ParsedCode parsedCode = ParsedCode.fromString(this.getSNOMED_Vollst_Prozedurenkode(), system);
    return parsedCode.hasEmptyCode() ? Constants.getEmptyValue() : FhirGenerator.coding(parsedCode);
  }

  public DateTimeType getPerformed() {
    String datum = this.getDurchfuehrungsdatum();
    return Helper.getDateFromISO(datum)
        .map(FhirGenerator::dateTimeType)
        .orElseGet(LOGGER.errorSupplier("getPerformed", "durchfuehrungsdatum", datum));
  }

  /** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/80349/" */
  public CodeableConcept getBodySite() {
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    ParsedCode parsedCode = ParsedCode.fromString(this.getKoerperstelle(), system);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    Coding coding = FhirGenerator.coding(parsedCode);
    return FhirGenerator.codeableConcept(coding);
  }

  public Annotation getNote() {
    String text = this.getFreitextbeschreibung();
    if (Helper.checkEmptyString(text)) {
      return Constants.getEmptyValue();
    }
    return new Annotation().setText(text);
  }

  public Extension getRecordedDate() {
    String dokuDatum = this.getDokumentationsdatum();
    if (Helper.checkEmptyString(dokuDatum) || dokuDatum.equals(this.getDurchfuehrungsdatum())) {
      return Constants.getEmptyValue();
    }
    Optional<DateTimeType> date = Helper.getDateFromISO(dokuDatum).map(FhirGenerator::dateTimeType);
    if (!date.isPresent()) {
      return LOGGER.error("getRecordedDate", "dokumentationsdatum", dokuDatum);
    }
    String url = ExtensionUrl.RECORDED_DATE;
    return FhirGenerator.extension(url, date.get());
  }

  /**
   * @see "https://simplifier.net/medizininformatikinitiative-modulprozeduren/durchfuehrungsabsicht"
   */
  public Extension getDurchfuehrungsabsicht() {
    String code = this.getKernDurchfuehrungsabsicht();
    ParsedCode parsedCode = ParsedCode.fromString(code);
    if (parsedCode.hasEmptyCode()) {
      return Constants.getEmptyValue();
    }
    String url = ExtensionUrl.DURCHFUEHRUNGSABSICHT;
    return DurchfuehrungsabsichtCode.fromCode(parsedCode.getCode())
        .map(FhirGenerator::coding)
        .map(c -> FhirGenerator.extension(url, c))
        .orElseGet(LOGGER.errorSupplier("getDurchfuehrungsabsicht", "durchfuehrungsabsicht", code));
  }

  public void setDurchfuehrungsabsicht(String durchfuehrungsabsicht) {
    this.durchfuehrungsabsicht = durchfuehrungsabsicht;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getOPS_Vollst_Prozedurenkode() {
    return OPS_Vollst_Prozedurenkode;
  }

  public void setOPS_Vollst_Prozedurenkode(String OPS_Vollst_Prozedurenkode) {
    this.OPS_Vollst_Prozedurenkode = OPS_Vollst_Prozedurenkode;
  }

  public String getOPS_Seitenlokalisation() {
    return OPS_Seitenlokalisation;
  }

  public void setOPS_Seitenlokalisation(String OPS_Seitenlokalisation) {
    this.OPS_Seitenlokalisation = OPS_Seitenlokalisation;
  }

  public String getSNOMED_Vollst_Prozedurenkode() {
    return SNOMED_Vollst_Prozedurenkode;
  }

  public void setSNOMED_Vollst_Prozedurenkode(String SNOMED_Vollst_Prozedurenkode) {
    this.SNOMED_Vollst_Prozedurenkode = SNOMED_Vollst_Prozedurenkode;
  }

  public String getKernDurchfuehrungsabsicht() {
    return durchfuehrungsabsicht;
  }

  public String getDurchfuehrungsdatum() {
    return durchfuehrungsdatum;
  }

  public void setDurchfuehrungsdatum(String durchfuehrungsdatum) {
    this.durchfuehrungsdatum = durchfuehrungsdatum;
  }

  public String getDokumentationsdatum() {
    return dokumentationsdatum;
  }

  public void setDokumentationsdatum(String dokumentationsdatum) {
    this.dokumentationsdatum = dokumentationsdatum;
  }

  public String getFreitextbeschreibung() {
    return freitextbeschreibung;
  }

  public void setFreitextbeschreibung(String freitextbeschreibung) {
    this.freitextbeschreibung = freitextbeschreibung;
  }

  public String getKoerperstelle() {
    return koerperstelle;
  }

  public void setKoerperstelle(String koerperstelle) {
    this.koerperstelle = koerperstelle;
  }
}
