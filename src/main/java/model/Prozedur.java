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

import static helper.FhirParser.*;

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

  // TODO: Is patNr really needed?
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
    LoggingData data = LoggingData.of(LOGGER, "getCategory", "OPS_Vollst_Prozedurenkode");
    return optionalCodeFromValueSet(ops, ProcedureCategorySnomedMapping::fromOpsCode, data);
  }

  public CodeableConcept getCode() {
    Coding snomed = this.getCodingSnomed();
    Coding ops = this.getCodingOps();
    return Helper.checkAllNull(snomed, ops)
        ? LOGGER.error("getCode", "Either SNOMED- or OPS-Code need to be defined!")
        : FhirGenerator.codeableConcept(snomed, ops);
  }

  public Coding getCodingOps() {
    String ops = this.getOPS_Vollst_Prozedurenkode();
    String system = CodingSystem.OPS_DIMDI;
    String version = Constants.VERSION_2020;
    Extension seite = this.getSeitenlokalisation();
    return optionalCodeFromSystemWithVersionAndExtension(ops, system, version, seite);
  }

  /** @see "https://simplifier.net/basisprofil-de-r4/extension-seitenlokalisation" */
  public Extension getSeitenlokalisation() {
    String ops = this.getOPS_Seitenlokalisation();
    String url = ExtensionUrl.OPS_SEITENLOKALISATION;
    LoggingData data = LoggingData.of(LOGGER, "getSeitenlokalisation", "OPS_Seitenlokalisation");
    return optionalExtensionWithCodingFromValueSet(
        ops, url, SeitenlokalisationCode::fromCode, data);
  }

  public Coding getCodingSnomed() {
    String snomed = this.getSNOMED_Vollst_Prozedurenkode();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    return optionalCodingFromSystem(snomed, system);
  }

  public DateTimeType getPerformed() {
    String datum = this.getDurchfuehrungsdatum();
    LoggingData data = LoggingData.of(LOGGER, "getPerformed", "durchfuehrungsdatum");
    return dateTimeType(datum, data);
  }

  /** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/80349/" */
  public CodeableConcept getBodySite() {
    String code = this.getKoerperstelle();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    return optionalCodeFromSystem(code, system);
  }

  public Annotation getNote() {
    String text = this.getFreitextbeschreibung();
    return optionalAnnotation(text);
  }

  public Extension getRecordedDate() {
    String dokuDatum = this.getDokumentationsdatum();
    if (Helper.checkEmptyString(dokuDatum) || dokuDatum.equals(this.getDurchfuehrungsdatum())) {
      return Constants.getEmptyValue();
    }
    String url = ExtensionUrl.RECORDED_DATE;
    LoggingData data = LoggingData.of(LOGGER, "getRecordedDate", "dokumentationsdatum");
    return extensionWithDateTimeType(dokuDatum, url, data);
  }

  /**
   * @see "https://simplifier.net/medizininformatikinitiative-modulprozeduren/durchfuehrungsabsicht"
   */
  public Extension getDurchfuehrungsabsicht() {
    String code = this.getKernDurchfuehrungsabsicht();
    String url = ExtensionUrl.DURCHFUEHRUNGSABSICHT;
    LoggingData data = LoggingData.of(LOGGER, "getDurchfuehrungsabsicht", "durchfuehrungsabsicht");
    return optionalExtensionWithCodingFromValueSet(
        code, url, DurchfuehrungsabsichtCode::fromCode, data);
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
