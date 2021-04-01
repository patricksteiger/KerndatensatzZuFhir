package enums;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

public enum Laborstruktur implements Code {
  ALLGEMEINER_LABORBEFUND("1", "Allgemeiner Laborbefund"),
  PROBENINFORMATION("10", "Probeninformation"),
  BLUTGRUPPENSEROLOGIE_L1("100", "Blutgruppenserologie"),
  BLUTGRUPPENSEROLOGIE_L2("01850", "Blutgruppenserologie"),
  HLA("01860", "HLA-Diagnostik"),
  HPA("01870", "HPA-Diagnostik"),
  BLUTGASANALYTIK("200", "Blutgasanalytik"),
  BLUTGASANALYSE_ARTERIELL("02060", "Blutgasanalyse arteriell"),
  HB_DERIVATE_ARTERIELL("02070", "Hb-Derivate arteriell"),
  BLUTGASANALYSE_VENOES("02080", "Blutgasanalyse venös"),
  HB_DERIVATE_VENOES("02090", "Hb-Derivate venös"),
  BLUTGASANALYSE_KAPILLAER("02100", "Blutgasanalyse kapillär"),
  HB_DERIVATE_KAPILLAER("02110", "Hb-Derivate kapillär"),
  HB_DERIVATE_GEMISCHTVENOES("02120", "Hb-Derivate, gemischtvenös (Rechtsherzkatheter)"),
  BGA_SONSTIGES("02130", "BGA Sonstiges"),
  HAEMATOLOGIE("300", "Hämatologie"),
  BLUTBILD("03010", "Blutbild"),
  KNOCHENMARK_MORPHOLOGIE("03020", "Knochenmark Morphologie"),
  IMMUNPHAENOTYPISIERUNG("03030", "Immunphänotypisierung"),
  HAEMATOLOGIE_SONSTIGES("03050", "Hämatologie Sonstiges"),
  GERINNUNG("400", "Gerinnung/Hämostaseologie"),
  HAEMOSTASEOLOGIE_GLOBALTESTS("04140", "Hämostaseologie Globaltests"),
  EINZELFAKTORANALYSEN("04150", "Einzelfaktoranalysen"),
  THROMBOPHILIE_TESTS("04160", "Thrombophilie Tests"),
  GERINNUNG_SONSTIGES("04170", "Gerinnung Sonstiges"),
  KLINISCHE_CHEMIE_L1("500", "Klinische Chemie/Proteindiagnostik"),
  KLINISCHE_CHEMIE_L2("05180", "Klinische Chemie"),
  PROTEINDIAGNOSTIK("05190", "Proteindiagnostik"),
  SPURENELEMENTE("05200", "Spurenelemente"),
  SONDERMATERIALIEN("05210", "Sondermaterialien"),
  UNTERSCH_BEI_STOFFWECHSELERKRANKUNGEN("05220", "Unters. bei Stoffwechselerkrankungen"),
  HORMONE_L1("600", "Hormone/Vitamine/Tumormarker"),
  HORMONE_L2("06330", "Hormone"),
  VITAMINE("06340", "Vitamine"),
  TUMORMARKER("06350", "Tumormarker"),
  TOXIKOLOGIE("900", "Toxikologie"),
  URIN("07230", "Urin-Screening"),
  BLUT("07240", "Blut"),
  TOXIKOLOGIE_SONSTIGES("07250", "Toxikologie Sonstiges"),
  MEDIKAMENTE("1000", "Medikamente"),
  ANTIBIOTIKA("08260", "Antibiotika"),
  VIROSTATIKA("08270", "Virostatika"),
  ANTIEPILEKTIKA("08280", "Antiepileptika"),
  PSYCHOPHARMAKA("08290", "Psychopharmaka"),
  KARDIAKA("08300", "Kardiaka"),
  IMMUNSUPPRESSIVA("08310", "Immunsuppressiva"),
  MEDIKAMENTE_SONSTIGES("08320", "Medikamente Sonstiges"),
  INFEKTIONSDIAGNOSTIK("1100", "Infektionsdiagnostik"),
  VIROLOGIE("10780", "Virologie"),
  BAKTERIOLOGIE("10790", "Bakteriologie"),
  MYKOLOGIE("10800", "Mykologie"),
  PARASITOLOGIE("10810", "Parasitologie"),
  MAKRO_MIKRO_SKOPIE("10820", "Makroskopie/Mikroskopie"),
  AUTOIMMUNDIAGNOSTIK("1300", "Autoimmundiagnostik"),
  RHEUMATOIDE_AUTOANTIKOERPER("11360", "Rheumatoide Arthritis-assoziierte Autoantikörper"),
  KOLLAGENOSE_AUTOANTIKOERPER("11370", "Kollagenose-assoziierte Autoantikörper"),
  MYOSITIS_AUTOANTIKOERPER("11380", "Myositis-assoziierte Autoantikörper"),
  LEBERERKRANKUNGEN_AUTOANTIKOERPER("11390", "Lebererkrankungen-assoziierte Autoantikörper"),
  PERNIZIOESE_ANAEMIE_AUTOANTIKOERPER("11400", "Perniziöse Anämie assoziierte Autoantikörper"),
  VASCULITIS_AUTOANTIKOERPER("11410", "Vasculitis-assoziierte Antikörper"),
  GOODPASTURE_SYNDROM_AUTOANTIKOERPER("11420", "Goodpasture Syndrom assoziierte Autoantikörper"),
  IBD_AUTOANTIKOERPER("11430", "IBD assoziierte Autoantikörper"),
  ZOELIAKIE_AUTOANTIKOERPER("11440", "Zöliakie assoziierte Autoantikörper"),
  DIABETES_MELLITUS_AUTOANTIKOERPER("11450", "Diabetes Mellitus assoziierte Autoantikörper"),
  PARANEOPLASIE_AUTOANTIKOERPER("11460", "Paraneoplasie assoziierte Autoantikörper"),
  SONSTIGE_AUTOANTIKOERPER("11470", "Sonstige Autoantikörper"),
  ALLERGIEDIAGNOSTIK("1800", "Allergiediagnostik"),
  GLOBALMARKER("12010", "Globalmarker"),
  INHALATIONSALLERGENE_IGE("12020", "Inhalationsallergene IgE"),
  NAHRUNGSMITTELALLERGENE_IGE("12030", "Nahrungsmittelallergene IgE"),
  UMWELT_BERUFS_ALLERGENE_IGE("12040", "Umwelt- und Berufsallergene IgE"),
  MEDIKAMENTE_IGE("12050", "Medikamente IgE"),
  INSEKTEN_INSEKTENGIFTE_IGE("12060", "Insekten und Insektengifte IgE"),
  SONSTIGE_IGE("12070", "Sonstige IgE"),
  ALLERGIECHIP_IGE("12080", "Allergiechip IgE"),
  IGG_ANTIGENE("12090", "IgG Antigene"),
  URINDIAGNOSTIK("1400", "Urindiagnostik"),
  URINSTREIFEN("13730", "Urinstreifen"),
  URINSEDIMENT("13740", "Urinsediment"),
  URINCHEMIE("13750", "Urinchemie"),
  STUHLDIAGNOSTIK_L1("1500", "Stuhldiagnostik"),
  STUHLDIAGNOSTIK_L2("14760", "Stuhldiagnostik"),
  LIQUORDIAGNOSTIK_L1("1600", "Liquordiagnostik"),
  LIQUORDIAGNOSTIK_L2("15770", "Liquordiagnostik"),
  GENETISCHE_DIAGNOSTIK("2300", "Genetische Diagnostik"),
  PHARMAKOGENETIK("16830", "Pharmakogenetik"),
  HUMANGENETIK("16840", "Humangenetik"),
  HAEMATOLOGISCHE_GENETIK("16841", "Hämatologische Genetik"),
  SONSTIGE_L1("2500", "Sonstige"),
  ZYTOLOGIE("17880", "Zytologie"),
  SONSTIGE_L2("17890", "Sonstige"),
  BEFUNDBEWERTUNG("20", "Befundbewertung");

  private final String code;
  private final String display;

  Laborstruktur(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<Laborstruktur> fromCode(String code) {
    return Helper.codeFromString(Laborstruktur.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.LABORGRUPPEN_CODE;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
