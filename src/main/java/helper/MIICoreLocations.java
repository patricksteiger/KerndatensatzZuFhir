package helper;

/**
 * @see "https://simplifier.net/medizininformatikinitiative-kerndatensatz/core-location-identifier"
 * @author Gerhard.Mayer
 */

public enum MIICoreLocations {
    MRI("Klinikum rechts der Isar"),
    KUM("Klinikum der Universität München"),
    UKT("Universitätsklinikum Tübingen"),
    UKU("Universitätsklinikum Ulm"),
    UKR("Universitätsklinikum Regensburg"),
    UKS("Universitätsklinikum des Saarlandes"),
    UKAU("Universitätsklinikum Augsburg"),
    Charité("Charité - Universitätsmedizin Berlin"),
    UMG("Universitätsmedizin Göttingen"),
    MHH("Medizinische Hochschule Hannover"),
    UKHD("Universitätsklinikum Heidelberg"),
    UKSH("Universitätsklinikum Schleswig-Holstein"),
    UKK("Universitätsklinikum Köln"),
    UKM("Universitätsklinikum Münster"),
    UKW("Universitätsklinikum Würzburg"),
    UKDD("Universitätsklinikum Carl Gustav Carus Dresden"),
    UKEr("Universitätsklinikum Erlangen"),
    UKF("Universitätsklinikum Frankfurt"),
    UKFR("Universitätsklinikum Freiburg"),
    UKGI("Universitätsklinikum Gießen"),
    UKMB("Universitätsklinikum Marburg"),
    UKG("Universitätsmedizin Greifswald"),
    UMMD("Universitätsklinikum Magdeburg"),
    UM("Universitätsmedizin der Johannes Gutenberg-Universität Mainz"),
    UMM("Universitätsklinikum Mannheim"),
    UKA("Uniklinik RWTH Aachen"),
    UKB("Universitätsklinikum Bonn"),
    UME("Universitätsklinikum Essen"),
    UKH("Universitätsklinikum Halle (Saale)"),
    UKE("Universitätsklinikum Hamburg-Eppendorf"),
    UKJ("Universitätsklinikum Jena"),
    UKL("Universitätsklinikum Leipzig"),
    UMR("Universitätsmedizin Rostock"),
    UKD("Universitätsklinikum Düsseldorf"),
    UKRUB("Universitätsklinikum der Ruhr-Universität Bochum");

    private final String displayName;

    MIICoreLocations(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
