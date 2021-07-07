package valueSets;

import helper.Helper;
import interfaces.Code;

import java.util.Optional;

/**
 * @see "https://simplifier.net/medizininformatikinitiative-kerndatensatz/core-location-identifier"
 * @author Gerhard.Mayer
 */
public enum MIICoreLocations implements Code {
  MRI("MRI", "Klinikum rechts der Isar"),
  KUM("KUM", "Klinikum der Universität München"),
  UKT("UKT", "Universitätsklinikum Tübingen"),
  UKU("UKU", "Universitätsklinikum Ulm"),
  UKR("UKR", "Universitätsklinikum Regensburg"),
  UKS("UKS", "Universitätsklinikum des Saarlandes"),
  UKAU("UKAU", "Universitätsklinikum Augsburg"),
  CHARITE("Charité", "Charité - Universitätsmedizin Berlin"),
  UMG("UMG", "Universitätsmedizin Göttingen"),
  MHH("MHH", "Medizinische Hochschule Hannover"),
  UKHD("UKHD", "Universitätsklinikum Heidelberg"),
  UKSH("UKSH", "Universitätsklinikum Schleswig-Holstein"),
  UKK("UKK", "Universitätsklinikum Köln"),
  UKM("UKM", "Universitätsklinikum Münster"),
  UKW("UKW", "Universitätsklinikum Würzburg"),
  UKDD("UKDD", "Universitätsklinikum Carl Gustav Carus Dresden"),
  UKER("UKEr", "Universitätsklinikum Erlangen"),
  UKF("UKF", "Universitätsklinikum Frankfurt"),
  UKFR("UKFR", "Universitätsklinikum Freiburg"),
  UKGI("UKGI", "Universitätsklinikum Gießen"),
  UKMR("UKMR", "Universitätsklinikum Marburg"),
  UKG("UKG", "Universitätsmedizin Greifswald"),
  UMMD("UMMD", "Universitätsklinikum Magdeburg"),
  UM("UM", "Universitätsmedizin der Johannes Gutenberg-Universität Mainz"),
  UMM("UMM", "Universitätsklinikum Mannheim"),
  UKA("UKA", "Uniklinik RWTH Aachen"),
  UKB("UKB", "Universitätsklinikum Bonn"),
  UME("UME", "Universitätsklinikum Essen"),
  UKH("UKH", "Universitätsklinikum Halle (Saale)"),
  UKE("UKE", "Universitätsklinikum Hamburg-Eppendorf"),
  UKJ("UKJ", "Universitätsklinikum Jena"),
  UKL("UKL", "Universitätsklinikum Leipzig"),
  UMR("UMR", "Universitätsmedizin Rostock"),
  UKD("UKD", "Universitätsklinikum Düsseldorf"),
  UKRUB("UKRUB", "Universitätsklinikum der Ruhr-Universität Bochum");

  private final String display;
  private final String code;

  MIICoreLocations(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<MIICoreLocations> fromCode(String code) {
    return Helper.codeFromString(MIICoreLocations.values(), code);
  }

  public String getCode() {
    return code;
  }

  public String getSystem() {
    return "https://www.medizininformatik-initiative.de/fhir/core/CodeSystem/core-location-identifier";
  }

  public String getDisplay() {
    return display;
  }

  @Override
  public String toString() {
    return this.display;
  }
}
