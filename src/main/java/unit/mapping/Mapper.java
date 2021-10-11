package unit.mapping;

import unit.ucum.Ucum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

public class Mapper {

  private static final BigDecimal EMPTY = ONE;
  private static final BigDecimal ONE_POW_MINUS_SIX = new BigDecimal("1.0E-6");
  private static final BigDecimal TEN_THOUSANDTH = new BigDecimal("1.0E-4");
  private static final BigDecimal THOUSANDTH = new BigDecimal("0.001");
  private static final BigDecimal HUNDREDTH = new BigDecimal("0.01");
  private static final BigDecimal TENTH = new BigDecimal("0.1");
  private static final BigDecimal ZINC = new BigDecimal("0.06538");
  private static final BigDecimal CREATIN = new BigDecimal("8.8403354");
  private static final BigDecimal SIXTY = new BigDecimal("60");
  private static final BigDecimal HUNDRED = new BigDecimal("100");
  private static final BigDecimal THOUSAND = new BigDecimal("1000");
  private static final BigDecimal MILLION = new BigDecimal("1000000");
  private static final Map<String, UnitMapping> mappings = generateMappings();

  private Mapper() {}

  public static Optional<UnitMapping> getUcumMappingFromLocalUnit(String localCode) {
    return Optional.ofNullable(mappings.get(localCode))
        .or(() -> getUcumMappingFromUcumCode(localCode));
  }

  private static Optional<UnitMapping> getUcumMappingFromUcumCode(String ucumCode) {
    return Ucum.validate(ucumCode) ? Optional.of(new UnitMapping(ucumCode, ONE)) : Optional.empty();
  }

  // TODO: some local codes are mapped to creatin-codes, even though its not in their name
  // TODO: codes with unknown conversions
  // TODO: should the conversion be dependant on loinc-code given in resource Ulm-Mapping_checklist?
  public static Map<String, UnitMapping> generateMappings() {
    Map<String, UnitMapping> result = new HashMap<>();
    result.put("%", new UnitMapping("%", ONE));
    result.put("µg/d", new UnitMapping("ug/(24.h)", ONE));
    result.put("µg/dl", new UnitMapping("ug/dL", ONE));
    result.put("µg/l", new UnitMapping("ug/L", ONE));
    result.put("µg/ml", new UnitMapping("ug/L", THOUSAND));
    result.put("µmol/d", new UnitMapping("ug/(24.h)", ONE));
    result.put("µmol/l", new UnitMapping("umol/L", ONE));
    result.put("µmol/mol", new UnitMapping("umol/mol{creat}", ONE));
    result.put("Anz./µl", new UnitMapping("/uL", ONE));
    result.put("B.E", new UnitMapping("", ONE));
    result.put("fl", new UnitMapping("fL", ONE));
    result.put("g/dl", new UnitMapping("g/dL", ONE));
    result.put("g/g Krea", new UnitMapping("mg/g{creat}", THOUSAND));
    result.put("g/l", new UnitMapping("mg/dL", HUNDRED));
    result.put("g/mol K.", new UnitMapping("mg/mmol{creat}", ONE));
    result.put("Giga/l", new UnitMapping("10*3/uL", ONE));
    result.put("IU/l", new UnitMapping("[IU]/L", ONE));
    result.put("IU/ml", new UnitMapping("U/mL", ONE));
    result.put("kU/l", new UnitMapping("k[IU]/L", ONE));
    result.put("l/l", new UnitMapping("%", HUNDRED));
    result.put("mg/d", new UnitMapping("mg/(24.h)", ONE));
    result.put("mg/dl", new UnitMapping("mg/dL", ONE));
    result.put("mg/l", new UnitMapping("mg/L", ONE));
    result.put("mg/lFEU", new UnitMapping("ng/mL{FEU}", THOUSAND));
    result.put("mIU/l", new UnitMapping("m[IU]/L", ONE));
    result.put("mIU/ml", new UnitMapping("[IU]/L", ONE));
    result.put("mm", new UnitMapping("mm", ONE));
    result.put("mm Hg", new UnitMapping("mm[Hg]", ONE));
    result.put("mm/h", new UnitMapping("mm/h", ONE));
    result.put("mmol/d", new UnitMapping("mmol/(24.h)", ONE));
    result.put("mmol/l", new UnitMapping("mmol/L", ONE));
    result.put("mmol/mol", new UnitMapping("mmol/mol{creat}", ONE));
    result.put("mol/molK", new UnitMapping("mmol/mol{creat}", THOUSAND));
    result.put("mosm/kg", new UnitMapping("mosm/kg", ONE));
    result.put("mU/l", new UnitMapping("u[IU]/mL", ONE));
    result.put("ng/l", new UnitMapping("pg/mL", ONE));
    result.put("ng/ml", new UnitMapping("ug/L", ONE));
    result.put("nmol/l", new UnitMapping("nmol/L", ONE));
    result.put("pg", new UnitMapping("pg", ONE));
    result.put("pg/ml", new UnitMapping("ug/L", THOUSANDTH));
    result.put("pmol/l", new UnitMapping("pmol/L", ONE));
    result.put("Ratio", new UnitMapping("{ratio}", ONE));
    result.put("sec.", new UnitMapping("s", ONE));
    result.put("Tera/l", new UnitMapping("10*6/uL", ONE));
    result.put("Titer", new UnitMapping("{titer}", ONE));
    result.put("U/l", new UnitMapping("U/L", ONE));
    result.put("U/ml", new UnitMapping("[arb'U]/mL", ONE));
    result.put("Zell./µl", new UnitMapping("/uL", ONE));
    return result;
  }

  // TODO local-code 952 is still doubled

  /**
   * Local codes that are not supported: 952
   *
   * @return
   */
  public static Map<LocalCodeWithUnit, LoincMapping> generateLoincMappings() {
    var mappings = new HashMap<LocalCodeWithUnit, LoincMapping>();
    mappings.put(
        new LocalCodeWithUnit("10001", "mmol/l"),
        new LoincMapping("2947-0", "Sodium [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10002", "mmol/l"),
        new LoincMapping("6298-4", "Potassium [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10003", "mmol/l"),
        new LoincMapping("1996-8", "Calcium [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10004", "mmol/l"),
        new LoincMapping("2593-2", "Magnesium [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10005", "mmol/l"),
        new LoincMapping("2069-3", "Chloride [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10007", "mmol/l"),
        new LoincMapping("24519-1", "Phosphate [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10008", "mmol/l"),
        new LoincMapping("22700-9", "Urea [Moles/volume] in Urine", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10009", "µmol/l"),
        new LoincMapping("59826-8", "Creatinine [Moles/volume] in Blood", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10010", "µmol/l"),
        new LoincMapping("14933-6", "Urate [Moles/volume] in Serum or Plasma", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10011", "mg/dl"),
        new LoincMapping("2345-7", "Glucose [Mass/volume] in Serum or Plasma", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10012", "µmol/l"),
        new LoincMapping(
            "14631-6", "Bilirubin.total [Moles/volume] in Serum or Plasma", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10013", "µmol/l"),
        new LoincMapping(
            "14629-0", "Bilirubin.direct [Moles/volume] in Serum or Plasma", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10015", "kU/l"),
        new LoincMapping(
            "2098-2",
            "Cholinesterase [Enzymatic activity/volume] in Serum or Plasma",
            THOUSAND,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10018", "U/l"),
        new LoincMapping(
            "1920-8",
            "Aspartate aminotransferase [Enzymatic activity/volume] in Serum or Plasma",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10019", "U/l"),
        new LoincMapping(
            "76625-3",
            "Alanine aminotransferase [Enzymatic activity/volume] in Blood",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10020", "U/l"),
        new LoincMapping(
            "2324-2",
            "Gamma glutamyl transferase [Enzymatic activity/volume] in Serum or Plasma",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10021", "U/l"),
        new LoincMapping(
            "2157-6",
            "Creatine kinase [Enzymatic activity/volume] in Serum or Plasma",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10022", "U/l"),
        new LoincMapping(
            "14804-9",
            "Lactate dehydrogenase [Enzymatic activity/volume] in Serum or Plasma by Lactate to pyruvate reaction",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10023", "µg/l"),
        new LoincMapping("49551-5", "Creatine kinase.MB [Mass/volume] in Blood", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10024", "U/l"),
        new LoincMapping(
            "2367-1",
            "Glutamate dehydrogenase [Enzymatic activity/volume] in Serum or Plasma",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10025", "µmol/l"),
        new LoincMapping(
            "14914-6", "Testosterone Free [Moles/volume] in Serum or Plasma", MILLION, "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10026", "U/l"),
        new LoincMapping(
            "1783-0", "Alkaline phosphatase [Enzymatic activity/volume] in Blood", ONE, "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10027", "U/l"),
        new LoincMapping(
            "73958-1",
            "Cholesterol esterase [Enzymatic activity/volume] in Dried blood spot",
            SIXTY,
            "nmol/h/mL"));
    mappings.put(
        new LocalCodeWithUnit("10028", "µg/l"),
        new LoincMapping("24373-3", "Ferritin [Mass/volume] in Blood", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10029", "U/l"),
        new LoincMapping(
            "77146-9",
            "Amylase [Enzymatic activity/volume] in Serum, Plasma or Blood",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10030", "mmol/l"),
        new LoincMapping(
            "22748-8", "Cholesterol in LDL [Moles/volume] in Serum or Plasma", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10031", "mmol/l"),
        new LoincMapping(
            "14646-4", "Cholesterol in HDL [Moles/volume] in Serum or Plasma", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10032", "mmol/l"),
        new LoincMapping(
            "14647-2", "Cholesterol [Moles/volume] in Serum or Plasma", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10033", "mmol/l"),
        new LoincMapping(
            "14927-8", "Triglyceride [Moles/volume] in Serum or Plasma", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10034", "g/l"),
        new LoincMapping("2885-2", "Protein [Mass/volume] in Serum or Plasma", TENTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10035", "g/l"),
        new LoincMapping("1751-7", "Albumin [Mass/volume] in Serum or Plasma", TENTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10036", "mg/l"),
        new LoincMapping("4635-9", "Free Hemoglobin [Mass/volume] in Serum", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10037", "mg/l"),
        new LoincMapping(
            "1988-5", "C reactive protein [Mass/volume] in Serum or Plasma", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("10038", "mmol/l"),
        new LoincMapping(
            "39469-2",
            "Cholesterol in LDL [Moles/volume] in Serum or Plasma by calculation",
            ONE,
            "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10039", "%"),
        new LoincMapping(
            "39354-6", "Cholinesterase in Serum or Plasma --dibucaine/Cholinesterase", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("10041", "µg/l"),
        new LoincMapping("2639-3", "Myoglobin [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10042", "ng/l"),
        new LoincMapping(
            "6598-7", "Troponin T.cardiac [Mass/volume] in Serum or Plasma", THOUSANDTH, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("10043", "mg/l"),
        new LoincMapping("33863-2", "Cystatin C [Mass/volume] in Serum or Plasma", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("10044", "g/l"),
        new LoincMapping(
            "4542-7", "Haptoglobin [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10045", "g/l"),
        new LoincMapping("2458-8", "IgA [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10046", "g/l"),
        new LoincMapping("2465-3", "IgG [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10047", "g/l"),
        new LoincMapping("2472-9", "IgM [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10048", "g/l"),
        new LoincMapping(
            "4485-9", "Complement C3 [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10049", "g/l"),
        new LoincMapping(
            "4498-2", "Complement C4 [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10050", "mg/l"),
        new LoincMapping(
            "30248-9",
            "Transferrin receptor.soluble [Mass/volume] in Serum or Plasma",
            ONE,
            "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("10051", "g/l"),
        new LoincMapping(
            "3034-6", "Transferrin [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10052", ""),
        new LoincMapping(
            "5196-1",
            "Hepatitis B virus surface Ag [Presence] in Serum or Plasma by Immunoassay",
            EMPTY,
            ""));
    mappings.put(
        new LocalCodeWithUnit("10053", ""),
        new LoincMapping(
            "5197-9",
            "Hepatitis B virus surface Ag [Presence] in Serum by Radioimmunoassay (RIA)",
            EMPTY,
            ""));
    mappings.put(
        new LocalCodeWithUnit("10054", "µg/l"),
        new LoincMapping(
            "75241-0",
            "Procalcitonin [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10056", "g/l"),
        new LoincMapping(
            "29146-8", "Alpha 1 antitrypsin [Mass/volume] in Body fluid", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10057", "U/ml"),
        new LoincMapping(
            "5370-2", "Streptolysin O Ab [Units/volume] in Serum or Plasma", ONE, "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("10058", "mg/dl"),
        new LoincMapping(
            "1869-7", "Apolipoprotein A-I [Mass/volume] in Serum or Plasma", HUNDREDTH, "g/L"));
    mappings.put(
        new LocalCodeWithUnit("10059", "mg/dl"),
        new LoincMapping(
            "1884-6", "Apolipoprotein B [Mass/volume] in Serum or Plasma", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10060", "g/l"),
        new LoincMapping(
            "2064-4", "Ceruloplasmin [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10061", "mg/dl"),
        new LoincMapping(
            "51002-4", "Lipoprotein midband A [Mass/volume] in Serum or Plasma", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10062", "g/l"),
        new LoincMapping("14338-8", "Prealbumin [Mass/volume] in Serum or Plasma", TENTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10063", "U/ml"),
        new LoincMapping(
            "11572-5", "Rheumatoid factor [Units/volume] in Serum or Plasma", ONE, "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("10064", "mg/l"),
        new LoincMapping(
            "1952-1", "Beta-2-Microglobulin [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10100", "mmol/l"),
        new LoincMapping(
            "14334-7", "Lithium [Moles/volume] in Serum or Plasma", THOUSANDTH, "mol/L"));
    mappings.put(
        new LocalCodeWithUnit("10101", "g/l"),
        new LoincMapping("2885-2", "Protein [Mass/volume] in Serum or Plasma", TENTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10151", "µmol/l"),
        new LoincMapping("1839-0", "Ammonia [Moles/volume] in Blood", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10152", "mg/l"),
        new LoincMapping("3432-2", "Carbamazepine [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10153", "µg/l"),
        new LoincMapping("10535-3", "Digoxin [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10154", "mg/l"),
        new LoincMapping("3298-7", "Acetaminophen [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10155", "mg/l"),
        new LoincMapping("3948-7", "Phenobarbital [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10156", "mg/l"),
        new LoincMapping("3968-5", "Phenytoin [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10157", "mg/l"),
        new LoincMapping("4049-3", "Theophylline [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10158", "mg/l"),
        new LoincMapping("4086-5", "Valproate [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10159", "mg/l"),
        new LoincMapping("35668-3", "Gentamicin [Mass/volume] in Serum or Plasma", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("10160", "mg/l"),
        new LoincMapping("20578-1", "Vancomycin [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("10161", "µg/l"),
        new LoincMapping("74097-7", "Tacrolimus [Mass/volume] in Blood by LC/MS/MS", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10162", "µg/l"),
        new LoincMapping(
            "74096-9", "Tacrolimus [Mass/volume] in Blood by Immunoassay", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10163", "µg/l"),
        new LoincMapping("29247-4", "Sirolimus [Mass/volume] in Blood", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10164", "µg/l"),
        new LoincMapping("50544-6", "Everolimus [Mass/volume] in Blood", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("10165", "µg/l"),
        new LoincMapping(
            "14978-1", "Cyclosporine [Mass/volume] in Blood by Immunoassay", ONE, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("10167", "mg/l"),
        new LoincMapping("4024-6", "Salicylates [Mass/volume] in Serum or Plasma", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10168", "µg/l"),
        new LoincMapping(
            "55805-6", "Cyclosporine [Mass/volume] in Blood by LC/MS/MS", ONE, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("10171", "mg/l"),
        new LoincMapping("35669-1", "Amikacin [Mass/volume] in Serum or Plasma", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("10262", "mg/dl"),
        new LoincMapping("2339-0", "Glucose [Mass/volume] in Blood", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10263", "mg/dl"),
        new LoincMapping("2339-0", "Glucose [Mass/volume] in Blood", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10523", "mmol/l"),
        new LoincMapping("2004-0", "Calcium [Moles/volume] in Urine", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10525", "mmol/l"),
        new LoincMapping("13539-2", "Phosphate [Moles/volume] in Urine", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10526", "mmol/l"),
        new LoincMapping("14683-7", "Creatinine [Moles/volume] in Urine", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10527", "mol/molK"),
        new LoincMapping(
            "33951-5", "Sodium/Creatinine [Molar ratio] in Urine", ONE, "mol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("10528", "mol/molK"),
        new LoincMapping(
            "33949-9", "Potassium/Creatinine [Molar ratio] in Urine", ONE, "mol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("10530", "mol/molK"),
        new LoincMapping(
            "34265-9", "Chloride/Creatinine [Molar ratio] in Urine", THOUSAND, "mmol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("10551", "mmol/d"),
        new LoincMapping("2956-1", "Sodium [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10553", "mmol/d"),
        new LoincMapping("2829-0", "Potassium [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10555", "mmol/d"),
        new LoincMapping("14637-3", "Calcium [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10557", "mmol/d"),
        new LoincMapping("2079-2", "Chloride [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10559", "mmol/d"),
        new LoincMapping("14881-7", "Phosphate [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10561", "mmol/l"),
        new LoincMapping("14879-1", "Phosphate [Moles/volume] in Serum or Plasma", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10562", "mg/dl"),
        new LoincMapping("2350-7", "Glucose [Mass/volume] in Urine", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10563", "mg/d"),
        new LoincMapping("2351-5", "Glucose [Mass/time] in 24 hour Urine", THOUSANDTH, "g/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10565", "mmol/d"),
        new LoincMapping("25550-5", "Urea [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10567", "mmol/d"),
        new LoincMapping("14935-1", "Urate [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10569", "mmol/d"),
        new LoincMapping(
            "14684-5", "Creatinine [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10572", "µmol/l"),
        new LoincMapping("14682-9", "Creatinine [Moles/volume] in Serum or Plasma", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("10579", "mmol/l"),
        new LoincMapping("2598-1", "Magnesium [Moles/volume] in Urine", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10580", "mmol/d"),
        new LoincMapping("2599-9", "Magnesium [Moles/time] in 24 hour Urine", ONE, "mmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("10588", "mg/l"),
        new LoincMapping("2888-6", "Protein [Mass/volume] in Urine", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10619", "mg/l"),
        new LoincMapping("2888-6", "Protein [Mass/volume] in Urine", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10621", "g/mol K."),
        new LoincMapping(
            "34366-5", "Protein/Creatinine [Ratio] in Urine", THOUSANDTH, "g/mmol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("10622", "g/mol K."),
        new LoincMapping(
            "34366-5", "Protein/Creatinine [Ratio] in Urine", THOUSANDTH, "g/mmol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("10623", "g/g Krea"),
        new LoincMapping(
            "2890-2", "Protein/Creatinine [Mass Ratio] in Urine", THOUSAND, "mg/g{creat}"));
    mappings.put(
        new LocalCodeWithUnit("1070", "Zell./µl"),
        new LoincMapping(
            "57845-0",
            "Leukocytes [#/volume] in Body fluid by Automated count",
            THOUSANDTH,
            "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("1071", "Zell./µl"),
        new LoincMapping(
            "55793-4", "Nucleated cells [#/volume] in Body fluid", THOUSANDTH, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("1072", "Tera/l"),
        new LoincMapping("26455-6", "Erythrocytes [#/volume] in Body fluid", THOUSAND, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("1073", "%"),
        new LoincMapping(
            "71696-9",
            "Polymorphonuclear cells/100 leukocytes in Body fluid by Automated count",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("1074", "Zell./µl"),
        new LoincMapping(
            "71698-5",
            "Polymorphonuclear cells [#/volume] in Body fluid by Automated count",
            THOUSANDTH,
            "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("1075", "%"),
        new LoincMapping(
            "71697-7",
            "Mononuclear cells/100 leukocytes in Body fluid by Automated count",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("10750", "mg/l"),
        new LoincMapping("2881-1", "Protein [Mass/volume] in Body fluid", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("10751", "mg/dl"),
        new LoincMapping("2344-0", "Glucose [Mass/volume] in Body fluid", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("10752", "mmol/l"),
        new LoincMapping("14165-5", "Lactate [Moles/volume] in Body fluid", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("10753", "U/l"),
        new LoincMapping(
            "14803-1",
            "Lactate dehydrogenase [Enzymatic activity/volume] in Body fluid by Lactate to pyruvate reaction",
            ONE,
            "U/L"));
    mappings.put(
        new LocalCodeWithUnit("10754", "mg/l"),
        new LoincMapping("1747-5", "Albumin [Mass/volume] in Body fluid", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("1076", "Zell./µl"),
        new LoincMapping(
            "71689-4",
            "Mononuclear cells [#/volume] in Body fluid by Automated count",
            THOUSANDTH,
            "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("1121", "%"),
        new LoincMapping("26505-8", "Segmented neutrophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("1123", "%"),
        new LoincMapping("26450-7", "Eosinophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("1124", "%"),
        new LoincMapping("30180-4", "Basophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("1125", "%"),
        new LoincMapping("26478-8", "Lymphocytes/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("1126", "%"),
        new LoincMapping("26485-3", "Monocytes/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("1142", "%"),
        new LoincMapping("4679-7", "Reticulocytes/100 erythrocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("117", "g/l"),
        new LoincMapping(
            "2685-6",
            "Alpha-1-acid glycoprotein [Mass/volume] in Serum or Plasma",
            HUNDRED,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("118", "g/l"),
        new LoincMapping(
            "1835-8", "Alpha-2-Macroglobulin [Mass/volume] in Serum or Plasma", ONE, "g/l"));
    mappings.put(
        new LocalCodeWithUnit("127", "ng/l"),
        new LoincMapping(
            "33211-4", "Interleukin 8 [Mass/volume] in Serum or Plasma", ONE, "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("133", "mg/l"),
        new LoincMapping(
            "80515-0",
            "Immunoglobulin light chains.kappa.free [Mass/volume] in Serum by Nephelometry",
            THOUSANDTH,
            "mg/mL"));
    mappings.put(
        new LocalCodeWithUnit("134", "mg/l"),
        new LoincMapping(
            "80516-8",
            "Immunoglobulin light chains.lambda.free [Mass/volume] in Serum by Nephelometry",
            THOUSANDTH,
            "mg/mL"));
    mappings.put(
        new LocalCodeWithUnit("135", ""),
        new LoincMapping(
            "48378-4",
            "Immunoglobulin light chains.kappa.free/Immunoglobulin light chains.lambda.free [Mass Ratio] in Serum",
            ONE,
            ""));
    mappings.put(
        new LocalCodeWithUnit("139", "mg/l"),
        new LoincMapping(
            "23905-3", "Mycophenolate [Mass/volume] in Serum or Plasma", ONE, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("142", "nmol/l"),
        new LoincMapping("32554-8", "Thiamine [Moles/volume] in Blood", ONE, "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("144", "nmol/l"),
        new LoincMapping(
            "74442-5", "Pyridoxal phosphate [Moles/volume] in Blood", THOUSANDTH, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("159", "%"),
        new LoincMapping("17856-6", "Hemoglobin A1c/Hemoglobin.total in Blood by HPLC", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("182", "g/l"),
        new LoincMapping("2465-3", "IgG [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("212", "µg/l"),
        new LoincMapping("3559-2", "Digitoxin [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("230", "%"),
        new LoincMapping("2614-6", "Methemoglobin/Hemoglobin.total in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("231", "%"),
        new LoincMapping("20563-3", "Carboxyhemoglobin/Hemoglobin.total in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("233", "g/l"),
        new LoincMapping("2466-1", "IgG subclass 1 [Mass/volume] in Serum", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("234", "g/l"),
        new LoincMapping("2467-9", "IgG subclass 2 [Mass/volume] in Serum", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("235", "g/l"),
        new LoincMapping("2468-7", "IgG subclass 3 [Mass/volume] in Serum", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("236", "g/l"),
        new LoincMapping("2469-5", "IgG subclass 4 [Mass/volume] in Serum", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("237", "mU/l"),
        new LoincMapping("20448-7", "Insulin [Units/volume] in Serum or Plasma", ONE, "u[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("238", "pmol/l"),
        new LoincMapping("27882-0", "Proinsulin [Moles/volume] in Serum or Plasma", ONE, "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("239", "µg/l"),
        new LoincMapping("2963-7", "Somatotropin [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("284", "%"),
        new LoincMapping(
            "13980-8", "Albumin/Protein.total in Serum or Plasma by Electrophoresis", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("286", "%"),
        new LoincMapping(
            "13978-2",
            "Alpha 1 globulin/Protein.total in Serum or Plasma by Electrophoresis",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("288", "%"),
        new LoincMapping(
            "13981-6",
            "Alpha 2 globulin/Protein.total in Serum or Plasma by Electrophoresis",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("290", "%"),
        new LoincMapping(
            "13982-4",
            "Beta globulin/Protein.total in Serum or Plasma by Electrophoresis",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("292", "%"),
        new LoincMapping(
            "13983-2",
            "Gamma globulin/Protein.total in Serum or Plasma by Electrophoresis",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("298", "%"),
        new LoincMapping("4679-7", "Reticulocytes/100 erythrocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("299", "Giga/l"),
        new LoincMapping(
            "60474-4", "Reticulocytes [#/volume] in Blood by Automated count", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("301", ""), new LoincMapping("11558-4", "pH of Blood", ONE, "[pH]"));
    mappings.put(
        new LocalCodeWithUnit("302", "mm Hg"),
        new LoincMapping("11557-6", "Carbon dioxide [Partial pressure] in Blood", ONE, "mm[Hg]"));
    mappings.put(
        new LocalCodeWithUnit("303", "mm Hg"),
        new LoincMapping("11556-8", "Oxygen [Partial pressure] in Blood", ONE, "mm[Hg]"));
    mappings.put(
        new LocalCodeWithUnit("304", "mmol/l"),
        new LoincMapping("1959-6", "Bicarbonate [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("305", "mmol/l"),
        new LoincMapping(
            "72720-6",
            "Base excess.100% oxygenated [Moles/volume] standard in Capillary blood by calculation",
            ONE,
            "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("306", "%"),
        new LoincMapping("2614-6", "Methemoglobin/Hemoglobin.total in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("307", "%"),
        new LoincMapping("20563-3", "Carboxyhemoglobin/Hemoglobin.total in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("308", "g/dl"),
        new LoincMapping("718-7", "Hemoglobin [Mass/volume] in Blood", ONE, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("311", "sec."),
        new LoincMapping("14979-9", "aPTT in Platelet poor plasma by Coagulation assay", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("315", "%"),
        new LoincMapping("5894-1", "Prothrombin time (PT) actual/Normal", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("316", "sec."),
        new LoincMapping("14979-9", "aPTT in Platelet poor plasma by Coagulation assay", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("318", "g/l"),
        new LoincMapping(
            "3255-7",
            "Fibrinogen [Mass/volume] in Platelet poor plasma by Coagulation assay",
            HUNDRED,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("331", "Tera/l"),
        new LoincMapping(
            "789-8", "Erythrocytes [#/volume] in Blood by Automated count", ONE, "10*6/uL"));
    mappings.put(
        new LocalCodeWithUnit("332", "Giga/l"),
        new LoincMapping(
            "6690-2", "Leukocytes [#/volume] in Blood by Automated count", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("333", "g/dl"),
        new LoincMapping("55782-7", "Hemoglobin [Mass/volume] in Blood by Oximetry", ONE, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("334", "l/l"),
        new LoincMapping("20570-8", "Hematocrit [Volume Fraction] of Blood", HUNDRED, "%"));
    mappings.put(
        new LocalCodeWithUnit("335", "pg"),
        new LoincMapping(
            "28539-5", "Erythrocyte mean corpuscular hemoglobin [Entitic mass]", ONE, "pg"));
    mappings.put(
        new LocalCodeWithUnit("336", "g/dl"),
        new LoincMapping(
            "28540-3",
            "Erythrocyte mean corpuscular hemoglobin concentration [Mass/volume]",
            ONE,
            "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("337", "fl"),
        new LoincMapping(
            "30428-7", "Erythrocyte mean corpuscular volume [Entitic volume]", ONE, "fL"));
    mappings.put(
        new LocalCodeWithUnit("338", "Giga/l"),
        new LoincMapping("26515-7", "Platelets [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("339", "Giga/l"),
        new LoincMapping("26474-7", "Lymphocytes [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("340", "%"),
        new LoincMapping(
            "736-9", "Lymphocytes/100 leukocytes in Blood by Automated count", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("341", "Giga/l"),
        new LoincMapping("26499-4", "Neutrophils [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("342", "%"),
        new LoincMapping("26511-6", "Neutrophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("344", "Giga/l"),
        new LoincMapping("26444-0", "Basophils [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("345", "%"),
        new LoincMapping("30180-4", "Basophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("346", "Giga/l"),
        new LoincMapping("26449-9", "Eosinophils [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("347", "%"),
        new LoincMapping("26450-7", "Eosinophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("348", "Giga/l"),
        new LoincMapping("26484-6", "Monocytes [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("349", "%"),
        new LoincMapping(
            "5905-5", "Monocytes/100 leukocytes in Blood by Automated count", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("351", "l/l"),
        new LoincMapping(
            "4545-0", "Hematocrit [Volume Fraction] of Blood by Centrifugation", HUNDRED, "%"));
    mappings.put(
        new LocalCodeWithUnit("356", "Giga/l"),
        new LoincMapping("26515-7", "Platelets [#/volume] in Blood", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("361", "mmol/l"),
        new LoincMapping(
            "32307-1", "Chloride [Moles/volume] in Unspecified specimen", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("370", "mg/dl"),
        new LoincMapping(
            "35662-6",
            "Glucose [Mass/volume] in Urine collected for unspecified duration",
            ONE,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("374", "%"),
        new LoincMapping(
            "27820-0",
            "Protein C Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("385", "µg/l"),
        new LoincMapping(
            "14182-0",
            "Thrombin antithrombin complex Ag [Mass/volume] in Platelet poor plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("388", "%"),
        new LoincMapping(
            "27823-4",
            "Protein S Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("403", "mg/dl"),
        new LoincMapping(
            "48986-4", "Glucose [Mass/volume] in Serum or Plasma --8 AM specimen", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("424", ""),
        new LoincMapping(
            "30106-9", "Acetylcholinesterase [Presence] in Amniotic fluid", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("438", "g/l"),
        new LoincMapping(
            "3255-7",
            "Fibrinogen [Mass/volume] in Platelet poor plasma by Coagulation assay",
            HUNDRED,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("454", "%"),
        new LoincMapping("26450-7", "Eosinophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("455", "%"),
        new LoincMapping("30180-4", "Basophils/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("456", "%"),
        new LoincMapping("26478-8", "Lymphocytes/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("457", "%"),
        new LoincMapping("26485-3", "Monocytes/100 leukocytes in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("47", "%"),
        new LoincMapping("2502-3", "Iron saturation [Mass Fraction] in Serum or Plasma", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("499", "fl"),
        new LoincMapping("28542-9", "Platelet mean volume [Entitic volume] in Blood", ONE, "fL"));
    mappings.put(
        new LocalCodeWithUnit("501", ""),
        new LoincMapping("5803-2", "pH of Urine by Test strip", ONE, "[pH]"));
    mappings.put(
        new LocalCodeWithUnit("505", "mg/dl"),
        new LoincMapping("5792-7", "Glucose [Mass/volume] in Urine by Test strip", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("506", ""),
        new LoincMapping("2514-8", "Ketones [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("507", ""),
        new LoincMapping("5818-0", "Urobilinogen [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("508", ""),
        new LoincMapping("5802-4", "Nitrite [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("509", ""),
        new LoincMapping("5802-4", "Nitrite [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("513", ""),
        new LoincMapping(
            "25145-4", "Bacteria [Presence] in Urine sediment by Light microscopy", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("519", "mg/dl"),
        new LoincMapping("5792-7", "Glucose [Mass/volume] in Urine by Test strip", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("53", "pg/ml"),
        new LoincMapping(
            "83107-3",
            "Natriuretic peptide.B prohormone N-Terminal [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("5301", "mg/dl"),
        new LoincMapping("2344-0", "Glucose [Mass/volume] in Body fluid", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("5307", "µg/l"),
        new LoincMapping("2963-7", "Somatotropin [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5311", "pmol/l"),
        new LoincMapping("27882-0", "Proinsulin [Moles/volume] in Serum or Plasma", ONE, "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("5315", "pg/ml"),
        new LoincMapping("1992-7", "Calcitonin [Mass/volume] in Serum or Plasma", ONE, "ng/L"));
    mappings.put(
        new LocalCodeWithUnit("5321", "mIU/l"),
        new LoincMapping(
            "3016-3", "Thyrotropin [Units/volume] in Serum or Plasma", ONE, "m[IU]/L"));
    mappings.put(
        new LocalCodeWithUnit("5322", "IU/l"),
        new LoincMapping(
            "83098-4",
            "Follitropin [Units/volume] in Serum or Plasma by Immunoassay",
            THOUSANDTH,
            "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5323", "IU/l"),
        new LoincMapping(
            "83103-2",
            "Lutropin [Units/volume] in Serum or Plasma by Immunoassay",
            THOUSANDTH,
            "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5324", "mU/l"),
        new LoincMapping("20448-7", "Insulin [Units/volume] in Serum or Plasma", ONE, "u[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5325", "µg/l"),
        new LoincMapping("1986-9", "C peptide [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5326", "µg/dl"),
        new LoincMapping(
            "83088-5", "Cortisol [Mass/volume] in Serum or Plasma by Immunoassay", TEN, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5329", "µg/dl"),
        new LoincMapping(
            "2191-5",
            "Dehydroepiandrosterone sulfate (DHEA-S) [Mass/volume] in Serum or Plasma",
            ONE,
            "ug/dL"));
    mappings.put(
        new LocalCodeWithUnit("533", "mg/l"),
        new LoincMapping("1754-1", "Albumin [Mass/volume] in Urine", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("5332", "µg/l"),
        new LoincMapping(
            "20568-2", "Prolactin [Mass/volume] in Serum or Plasma by Immunoassay", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5334", "µg/l"),
        new LoincMapping(
            "1854-9", "Androstenedione [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5335", "µg/l"),
        new LoincMapping(
            "2193-1",
            "Dehydroepiandrosterone (DHEA) [Mass/volume] in Serum or Plasma",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5336", "µg/l"),
        new LoincMapping(
            "1668-3", "17-Hydroxyprogesterone [Mass/volume] in Serum or Plasma", HUNDRED, "ng/dL"));
    mappings.put(
        new LocalCodeWithUnit("5339", "%"),
        new LoincMapping(
            "71693-6",
            "Platelets reticulated/100 platelets in Blood by Automated count",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("534", "g/mol K."),
        new LoincMapping("32294-1", "Albumin/Creatinine [Ratio] in Urine", ONE, "mg/mmol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("5340", "%"),
        new LoincMapping(
            "33516-6", "Immature reticulocytes/Reticulocytes.total in Blood", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("5341", "pg"),
        new LoincMapping(
            "71694-4", "Hemoglobin [Entitic mass] in Reticulocytes by Automated count", ONE, "pg"));
    mappings.put(
        new LocalCodeWithUnit("5344", "µg/l"),
        new LoincMapping(
            "53731-6", "Posaconazole [Mass/volume] in Serum or Plasma", THOUSANDTH, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("5346", "ng/ml"),
        new LoincMapping(
            "15355-1", "Estrone sulfate [Mass/volume] in Serum or Plasma", THOUSAND, "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("5348", "%"),
        new LoincMapping(
            "27821-8",
            "Protein S Free Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("535", "mg/l"),
        new LoincMapping("33415-1", "IgG [Mass/volume] in Urine", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("536", "g/mol K."),
        new LoincMapping(
            "33948-1", "IgG/Creatinine [Mass Ratio] in Urine", CREATIN, "mg/g{creat}"));
    mappings.put(
        new LocalCodeWithUnit("537", "mg/l"),
        new LoincMapping("46723-3", "Alpha-1-Microglobulin [Mass/volume] in Urine", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("5370", "sec."),
        new LoincMapping(
            "52768-9", "Clot formation [Time] in Blood by Thromboelastography", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("5374", "sec."),
        new LoincMapping("52789-5", "Clotting time of Blood by Thromboelastography", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("5375", "sec."),
        new LoincMapping(
            "52768-9", "Clot formation [Time] in Blood by Thromboelastography", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("5377", "mm"),
        new LoincMapping(
            "52780-4",
            "Maximum clot firmness.extrinsic coagulation system activated.platelets inhibited [Length] in Blood by Thromboelastography",
            ONE,
            "mm"));
    mappings.put(
        new LocalCodeWithUnit("538", "g/mol K."),
        new LoincMapping(
            "48415-4",
            "Alpha-1-Microglobulin/Creatinine [Mass Ratio] in Urine",
            CREATIN,
            "mg/g{creat}"));
    mappings.put(
        new LocalCodeWithUnit("5384", "ng/ml"),
        new LoincMapping(
            "83116-4",
            "Testosterone [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5385", "pg/ml"),
        new LoincMapping(
            "26881-3", "Interleukin 6 [Mass/volume] in Serum or Plasma", ONE, "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("5389", ""),
        new LoincMapping(
            "6799-1", "Tricyclic antidepressants [Presence] in Urine by Immunoassay", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("539", "mg/l"),
        new LoincMapping(
            "6888-2", "Alpha 2 globulin [Mass/volume] in Urine", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("5395", "%"),
        new LoincMapping(
            "13995-6", "Gamma globulin/Protein.total in Urine by Electrophoresis", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("540", "mg/l"),
        new LoincMapping(
            "27365-6", "Immunoglobulin light chains.kappa [Mass/volume] in Urine", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("5400", "IU/ml"),
        new LoincMapping(
            "46267-1",
            "Proteinase 3 Ab [Units/volume] in Serum by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5401", "U/ml"),
        new LoincMapping(
            "46266-3",
            "Myeloperoxidase Ab [Units/volume] in Serum by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5402", "U/ml"),
        new LoincMapping(
            "3182-3", "Cardiolipin IgM Ab [Units/volume] in Serum by Immunoassay", ONE, "[MPL'U]"));
    mappings.put(
        new LocalCodeWithUnit("5403", "U/ml"),
        new LoincMapping(
            "3181-5", "Cardiolipin IgG Ab [Units/volume] in Serum by Immunoassay", ONE, "[GPL'U]"));
    mappings.put(
        new LocalCodeWithUnit("5404", "U/ml"),
        new LoincMapping(
            "44449-7",
            "Beta 2 glycoprotein 1 IgM Ab [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5405", "U/ml"),
        new LoincMapping(
            "44448-9",
            "Beta 2 glycoprotein 1 IgG Ab [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5406", "U/ml"),
        new LoincMapping(
            "32218-0",
            "Cyclic citrullinated peptide Ab [Units/volume] in Serum by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5409", "U/ml"),
        new LoincMapping(
            "82938-2",
            "Smith extractable nuclear D IgG Ab [Units/volume] in Serum",
            ONE,
            "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("541", "mg/l"),
        new LoincMapping(
            "27394-6",
            "Immunoglobulin light chains.lambda [Mass/volume] in Urine",
            TENTH,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("5414", "U/ml"),
        new LoincMapping(
            "82391-4",
            "Centromere protein B Ab [Units/volume] in Serum by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5415", "Titer"),
        new LoincMapping(
            "5048-4", "Nuclear Ab [Titer] in Serum by Immunofluorescence", ONE, "{titer}"));
    mappings.put(
        new LocalCodeWithUnit("5418", "nmol/l"),
        new LoincMapping("9740-2", "Neopterin [Moles/volume] in Serum or Plasma", ONE, "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("5420", "µg/l"),
        new LoincMapping(
            "25638-8", "Eosinophil cationic protein (ECP) [Mass/volume] in Serum", ONE, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("5421", "kU/l"),
        new LoincMapping(
            "6103-6", "Echinococcus sp IgE Ab [Units/volume] in Serum", ONE, "k[IU]/L"));
    mappings.put(
        new LocalCodeWithUnit("5422", "U/ml"),
        new LoincMapping(
            "59394-7",
            "Glomerular basement membrane Ab [Units/volume] in Serum by Immunoassay",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("5425", "pg/ml"),
        new LoincMapping(
            "48425-3", "Troponin T.cardiac [Mass/volume] in Blood", THOUSANDTH, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("5427", "µg/l"),
        new LoincMapping(
            "2991-8", "Testosterone Free [Mass/volume] in Serum or Plasma", THOUSAND, "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("5431", ""),
        new LoincMapping("50956-2", "HLA-B*57:01 [Presence]", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("5433", "mmol/mol"),
        new LoincMapping(
            "59261-8", "Hemoglobin A1c/Hemoglobin.total in Blood by IFCC protocol", TENTH, "%"));
    mappings.put(
        new LocalCodeWithUnit("5434", "ng/ml"),
        new LoincMapping(
            "9679-2", "Squamous cell carcinoma Ag [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5435", "µg/l"),
        new LoincMapping(
            "83116-4",
            "Testosterone [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5441", "%"),
        new LoincMapping(
            "12841-3",
            "Prostate Specific Ag Free/Prostate specific Ag.total in Serum or Plasma",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5444", "ng/ml"),
        new LoincMapping(
            "2484-4",
            "Insulin-like growth factor-I [Mass/volume] in Serum or Plasma",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("5445", "µg/l"),
        new LoincMapping(
            "38370-3", "Voriconazole [Mass/volume] in Serum or Plasma", THOUSANDTH, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("545", ""),
        new LoincMapping("3390-2", "Benzodiazepines [Presence] in Urine", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("5461", "%"),
        new LoincMapping(
            "3209-4",
            "Coagulation factor VIII activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5462", "%"),
        new LoincMapping(
            "3187-2",
            "Coagulation factor IX activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5463", "%"),
        new LoincMapping(
            "3289-6",
            "Prothrombin activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5464", "%"),
        new LoincMapping(
            "3193-0",
            "Coagulation factor V activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5465", "%"),
        new LoincMapping(
            "3198-9",
            "Coagulation factor VII activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5466", "%"),
        new LoincMapping(
            "3218-5",
            "Coagulation factor X activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5467", "%"),
        new LoincMapping(
            "3187-2",
            "Coagulation factor IX activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5468", "%"),
        new LoincMapping(
            "3232-6",
            "Coagulation factor XII activity actual/normal in Platelet poor plasma by Coagulation assay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5469", "%"),
        new LoincMapping(
            "3239-1",
            "Coagulation factor XIII Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5470", "%"),
        new LoincMapping(
            "27816-8",
            "von Willebrand factor (vWf) Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5471", "%"),
        new LoincMapping(
            "6014-5",
            "von Willebrand factor (vWf) ristocetin cofactor actual/normal in Platelet poor plasma by Aggregation",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5472", "B.E"),
        new LoincMapping(
            "3204-5",
            "Coagulation factor VIII inhibitor [Units/volume] in Platelet poor plasma by Coagulation assay",
            ONE,
            ""));
    mappings.put(
        new LocalCodeWithUnit("5477", "sec."),
        new LoincMapping(
            "24471-5",
            "Platelet function (closure time) collagen+Epinephrine induced [Time] in Blood",
            ONE,
            "s"));
    mappings.put(
        new LocalCodeWithUnit("5478", "sec."),
        new LoincMapping(
            "24472-3",
            "Platelet function (closure time) collagen+ADP induced [Time] in Blood",
            ONE,
            "s"));
    mappings.put(
        new LocalCodeWithUnit("549", "mosm/kg"),
        new LoincMapping("2695-5", "Osmolality of Urine", ONE, "mosm/kg"));
    mappings.put(
        new LocalCodeWithUnit("5503", "%"),
        new LoincMapping(
            "28660-9",
            "Plasminogen actual/normal in Platelet poor plasma by Chromogenic method",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5504", "ng/ml"),
        new LoincMapping(
            "5971-7",
            "Plasminogen activator tissue type Ag [Mass/volume] in Platelet poor plasma by Immunoassay",
            ONE,
            "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("5505", "%"),
        new LoincMapping(
            "27810-1",
            "Plasmin inhibitor actual/normal in Platelet poor plasma by Chromogenic method",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5506", "%"),
        new LoincMapping(
            "50377-1",
            "von Willebrand factor (vWf).collagen binding activity actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("5513", "nmol/l"),
        new LoincMapping(
            "25742-8",
            "Prothrombin Fragment 1.2 [Moles/volume] in Serum or Plasma",
            ONE,
            "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("578", "µmol/d"),
        new LoincMapping(
            "14666-2", "Copper [Moles/time] in 24 hour Urine", THOUSAND, "nmol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("581", "mosm/kg"),
        new LoincMapping("2695-5", "Osmolality of Urine", ONE, "mosm/kg"));
    mappings.put(
        new LocalCodeWithUnit("583", ""),
        new LoincMapping("19343-3", "Amphetamine [Presence] in Urine by Screen method", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("586", ""),
        new LoincMapping("19295-5", "Opiates [Presence] in Urine by Screen method", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("594", "mg/l"),
        new LoincMapping("1754-1", "Albumin [Mass/volume] in Urine", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("595", "mg/d"),
        new LoincMapping("1755-8", "Albumin [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("596", "g/mol K."),
        new LoincMapping(
            "76401-9", "Albumin/Creatinine [Ratio] in 24 hour Urine", ONE, "mg/mmol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("597", "mg/l"),
        new LoincMapping("2465-3", "IgG [Mass/volume] in Serum or Plasma", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("598", "mg/d"),
        new LoincMapping("55923-7", "IgG [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("599", "g/mol K."),
        new LoincMapping(
            "33948-1", "IgG/Creatinine [Mass Ratio] in Urine", CREATIN, "mg/g{creat}"));
    mappings.put(
        new LocalCodeWithUnit("6", "mosm/kg"),
        new LoincMapping("2692-2", "Osmolality of Serum or Plasma", ONE, "mosm/kg"));
    mappings.put(
        new LocalCodeWithUnit("600", "mg/l"),
        new LoincMapping("46723-3", "Alpha-1-Microglobulin [Mass/volume] in Urine", ONE, "mg/L"));
    mappings.put(
        new LocalCodeWithUnit("601", "mg/d"),
        new LoincMapping(
            "48414-7", "Alpha-1-Microglobulin [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("602", "g/mol K."),
        new LoincMapping(
            "48415-4",
            "Alpha-1-Microglobulin/Creatinine [Mass Ratio] in Urine",
            CREATIN,
            "mg/g{creat}"));
    mappings.put(
        new LocalCodeWithUnit("603", "mg/l"),
        new LoincMapping(
            "40604-1",
            "Alpha-2-Macroglobulin [Presence] in Urine by Immunoelectrophoresis",
            EMPTY,
            ""));
    mappings.put(
        new LocalCodeWithUnit("604", "mg/l"),
        new LoincMapping(
            "27365-6", "Immunoglobulin light chains.kappa [Mass/volume] in Urine", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("605", "mg/l"),
        new LoincMapping(
            "27394-6",
            "Immunoglobulin light chains.lambda [Mass/volume] in Urine",
            TENTH,
            "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("606", ""),
        new LoincMapping(
            "33559-6",
            "Immunoglobulin light chains.kappa/Immunoglobulin light chains.lambda [Mass Ratio] in Urine",
            ONE,
            "{ratio}"));
    mappings.put(
        new LocalCodeWithUnit("610", "µmol/d"),
        new LoincMapping(
            "14689-4", "Delta aminolevulinate [Moles/time] in 24 hour Urine", ONE, "umol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("612", "µg/d"),
        new LoincMapping("10885-2", "Porphyrins [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("614", "µmol/d"),
        new LoincMapping(
            "14882-5", "Porphobilinogen [Moles/time] in 24 hour Urine", ONE, "umol/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("625", "µg/d"),
        new LoincMapping("2147-7", "Cortisol Free [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("631", "mg/l"),
        new LoincMapping("1747-5", "Albumin [Mass/volume] in Body fluid", TEN_THOUSANDTH, "g/dL"));
    mappings.put(
        new LocalCodeWithUnit("632", "mg/l"),
        new LoincMapping("15183-7", "IgG [Mass/volume] in Body fluid", TENTH, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("640", "µg/d"),
        new LoincMapping("2232-7", "Epinephrine [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("642", "µg/d"),
        new LoincMapping(
            "2668-2", "Norepinephrine [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("644", "µg/d"),
        new LoincMapping("2218-6", "Dopamine [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("646", "mg/d"),
        new LoincMapping(
            "3122-9", "Vanillylmandelate [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("648", "mg/d"),
        new LoincMapping("2436-4", "Homovanillate [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("650", "µg/d"),
        new LoincMapping("19049-6", "Metanephrine [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("652", "µg/d"),
        new LoincMapping(
            "2671-6", "Normetanephrine [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("654", "mg/d"),
        new LoincMapping(
            "1695-6", "5-Hydroxyindoleacetate [Mass/time] in 24 hour Urine", ONE, "mg/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("658", "µmol/mol"),
        new LoincMapping(
            "24522-5", "Epinephrine/Creatinine [Molar ratio] in Urine", ONE, "umol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("660", "µmol/mol"),
        new LoincMapping(
            "24523-3", "Norepinephrine/Creatinine [Molar ratio] in Urine", ONE, "umol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("662", "µmol/mol"),
        new LoincMapping(
            "24524-1", "Dopamine/Creatinine [Molar ratio] in Urine", ONE, "umol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("664", "mmol/mol"),
        new LoincMapping(
            "14948-4",
            "Vanillylmandelate/Creatinine [Molar ratio] in Urine",
            ONE,
            "mmol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("666", "mmol/mol"),
        new LoincMapping(
            "22708-2", "Homovanillate/Creatinine [Molar ratio] in Urine", ONE, "mmol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("686", ""),
        new LoincMapping("44038-8", "Bacteria [Presence] in Unspecified specimen", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("695", ""),
        new LoincMapping(
            "41599-2", "Bacteria [Presence] in Body fluid by Light microscopy", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("696", "Zell./µl"),
        new LoincMapping(
            "26455-6", "Erythrocytes [#/volume] in Body fluid", THOUSANDTH, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("762", "Zell./µl"),
        new LoincMapping("30405-5", "Leukocytes [#/volume] in Urine", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("763", "Zell./µl"),
        new LoincMapping("30391-7", "Erythrocytes [#/volume] in Urine", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("764", ""),
        new LoincMapping(
            "25145-4", "Bacteria [Presence] in Urine sediment by Light microscopy", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("778", "µg/d"),
        new LoincMapping("2147-7", "Cortisol Free [Mass/time] in 24 hour Urine", ONE, "ug/(24.h)"));
    mappings.put(
        new LocalCodeWithUnit("779", ""),
        new LoincMapping("20454-5", "Protein [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("781", ""),
        new LoincMapping("5770-3", "Bilirubin.total [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("782", ""),
        new LoincMapping("20454-5", "Protein [Presence] in Urine by Test strip", EMPTY, ""));
    mappings.put(
        new LocalCodeWithUnit("786", "µg/l"),
        new LoincMapping(
            "10989-2", "Itraconazole [Mass/volume] in Serum or Plasma", THOUSANDTH, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("788", "µg/l"),
        new LoincMapping(
            "27081-9",
            "Itraconazole+Hydroxyitraconazole [Mass/volume] in Serum or Plasma",
            ONE_POW_MINUS_SIX,
            "mg/mL"));
    mappings.put(
        new LocalCodeWithUnit("790", "mmol/mol"),
        new LoincMapping(
            "14831-2", "Metanephrine/Creatinine [Molar ratio] in Urine", ONE, "mmol/mol{creat}"));
    mappings.put(
        new LocalCodeWithUnit("793", "pg/ml"),
        new LoincMapping(
            "2731-8", "Parathyrin.intact [Mass/volume] in Serum or Plasma", ONE, "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("806", "%"),
        new LoincMapping("38505-4", "Thyroglobulin recovery in Serum or Plasma", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("807", "µg/l"),
        new LoincMapping(
            "3013-0", "Thyroglobulin [Mass/volume] in Serum or Plasma", HUNDRED, "ng/dL"));
    mappings.put(
        new LocalCodeWithUnit("809", "IU/l"),
        new LoincMapping(
            "5385-0", "Thyrotropin receptor Ab [Units/volume] in Serum", ONE, "[IU]/L"));
    mappings.put(
        new LocalCodeWithUnit("841", "nmol/l"),
        new LoincMapping(
            "83125-5",
            "Triiodothyronine (T3) [Moles/volume] in Serum or Plasma by Immunoassay",
            THOUSANDTH,
            "nmol/mL"));
    mappings.put(
        new LocalCodeWithUnit("842", "pmol/l"),
        new LoincMapping(
            "83126-3",
            "Triiodothyronine (T3) Free [Moles/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("843", "nmol/l"),
        new LoincMapping(
            "83120-6",
            "Thyroxine (T4) [Moles/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("844", "pmol/l"),
        new LoincMapping(
            "83121-4",
            "Thyroxine (T4) free [Moles/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("845", "mIU/l"),
        new LoincMapping(
            "3016-3", "Thyrotropin [Units/volume] in Serum or Plasma", ONE, "m[IU]/L"));
    mappings.put(
        new LocalCodeWithUnit("846", "IU/l"),
        new LoincMapping(
            "83086-9",
            "Choriogonadotropin [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "m[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("847", "ng/l"),
        new LoincMapping(
            "83096-8",
            "Estradiol (E2) [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("848", "IU/l"),
        new LoincMapping(
            "83098-4",
            "Follitropin [Units/volume] in Serum or Plasma by Immunoassay",
            THOUSANDTH,
            "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("849", "IU/l"),
        new LoincMapping(
            "83103-2",
            "Lutropin [Units/volume] in Serum or Plasma by Immunoassay",
            THOUSANDTH,
            "[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("850", "µg/l"),
        new LoincMapping(
            "83109-9",
            "Progesterone [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("855", "µg/dl"),
        new LoincMapping(
            "83088-5", "Cortisol [Mass/volume] in Serum or Plasma by Immunoassay", TEN, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("856", "µg/dl"),
        new LoincMapping(
            "2191-5",
            "Dehydroepiandrosterone sulfate (DHEA-S) [Mass/volume] in Serum or Plasma",
            ONE,
            "ug/dL"));
    mappings.put(
        new LocalCodeWithUnit("857", "nmol/l"),
        new LoincMapping(
            "13967-5",
            "Sex hormone binding globulin [Moles/volume] in Serum or Plasma",
            ONE,
            "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("858", "µg/l"),
        new LoincMapping(
            "1834-1", "Alpha-1-Fetoprotein [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("859", "IU/ml"),
        new LoincMapping(
            "83083-6",
            "Cancer Ag 15-3 [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "U/mL"));
    mappings.put(
        new LocalCodeWithUnit("860", "IU/ml"),
        new LoincMapping(
            "83084-4",
            "Cancer Ag 19-9 [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "U/mL"));
    mappings.put(
        new LocalCodeWithUnit("861", "IU/ml"),
        new LoincMapping(
            "83082-8",
            "Cancer Ag 125 [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "U/mL"));
    mappings.put(
        new LocalCodeWithUnit("864", "nmol/l"),
        new LoincMapping("14732-2", "Folate [Moles/volume] in Serum or Plasma", ONE, "nmol/L"));
    mappings.put(
        new LocalCodeWithUnit("865", "IU/ml"),
        new LoincMapping(
            "83102-4", "IgE [Units/volume] in Serum or Plasma by Immunoassay", ONE, "k[IU]/L"));
    mappings.put(
        new LocalCodeWithUnit("867", "IU/l"),
        new LoincMapping(
            "83086-9",
            "Choriogonadotropin [Units/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "m[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("868", "µg/l"),
        new LoincMapping(
            "47275-3", "S100 calcium binding protein B [Mass/volume] in Serum", ONE, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("870", "µg/l"),
        new LoincMapping("2697-1", "Osteocalcin [Mass/volume] in Serum or Plasma", ONE, "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("871", "µg/l"),
        new LoincMapping(
            "77370-5",
            "Procollagen type I.N-terminal propeptide [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("872", "µg/l"),
        new LoincMapping(
            "41171-0",
            "Collagen crosslinked C-telopeptide [Mass/volume] in Serum or Plasma",
            THOUSAND,
            "pg/mL"));
    mappings.put(
        new LocalCodeWithUnit("873", "µg/l"),
        new LoincMapping(
            "20568-2", "Prolactin [Mass/volume] in Serum or Plasma by Immunoassay", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("874", "ng/ml"),
        new LoincMapping(
            "2484-4",
            "Insulin-like growth factor-I [Mass/volume] in Serum or Plasma",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("875", "ng/ml"),
        new LoincMapping(
            "2483-6",
            "Insulin-like growth factor binding protein 3 [Mass/volume] in Serum or Plasma",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("876", "Ratio"),
        new LoincMapping(
            "13590-5",
            "Activated protein C resistance [Time Ratio] in Platelet poor plasma by Coagulation assay",
            ONE,
            "{ratio}"));
    mappings.put(
        new LocalCodeWithUnit("882", "µg/l"),
        new LoincMapping(
            "35741-8",
            "Prostate specific Ag [Mass/volume] in Serum or Plasma by Detection limit <= 0.01 ng/mL",
            ONE,
            "ug/L"));
    mappings.put(
        new LocalCodeWithUnit("885", "sec."),
        new LoincMapping(
            "48344-6",
            "Activated clotting time (ACT) of Platelet poor plasma by Kaolin induced method",
            ONE,
            "s"));
    mappings.put(
        new LocalCodeWithUnit("888", "Ratio"),
        new LoincMapping(
            "69423-2", "Rosner index in Platelet poor plasma by Coagulation assay", HUNDRED, "%"));
    mappings.put(
        new LocalCodeWithUnit("894", "%"),
        new LoincMapping(
            "27812-7",
            "Antithrombin Ag actual/normal in Platelet poor plasma by Immunoassay",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("895", "pg/ml"),
        new LoincMapping("1992-7", "Calcitonin [Mass/volume] in Serum or Plasma", ONE, "ng/L"));
    mappings.put(
        new LocalCodeWithUnit("896", "µmol/l"),
        new LoincMapping(
            "13965-9", "Homocysteine [Moles/volume] in Serum or Plasma", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("897", "pmol/l"),
        new LoincMapping(
            "14685-2", "Cobalamin (Vitamin B12) [Moles/volume] in Serum or Plasma", ONE, "pmol/L"));
    mappings.put(
        new LocalCodeWithUnit("898", "%"),
        new LoincMapping(
            "48495-6",
            "Transferrin.carbohydrate deficient/Transferrin.total in Serum or Plasma",
            ONE,
            "%"));
    mappings.put(
        new LocalCodeWithUnit("900", "g/l"),
        new LoincMapping(
            "3034-6", "Transferrin [Mass/volume] in Serum or Plasma", HUNDRED, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("909", "µg/l"),
        new LoincMapping(
            "1854-9", "Androstenedione [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("910", "µg/l"),
        new LoincMapping(
            "2193-1",
            "Dehydroepiandrosterone (DHEA) [Mass/volume] in Serum or Plasma",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("911", "mmol/l"),
        new LoincMapping("6298-4", "Potassium [Moles/volume] in Blood", ONE, "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("912", "mg/dl"),
        new LoincMapping("2339-0", "Glucose [Mass/volume] in Blood", ONE, "mg/dL"));
    mappings.put(
        new LocalCodeWithUnit("913", "mmol/l"),
        new LoincMapping(
            "47596-2",
            "Calcium.ionized [Moles/volume] in Blood by Ion-selective membrane electrode (ISE)",
            ONE,
            "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("914", "mmol/l"),
        new LoincMapping(
            "47598-8",
            "Calcium.ionized [Moles/volume] adjusted to pH 7.4 in Blood",
            ONE,
            "mmol/L"));
    mappings.put(
        new LocalCodeWithUnit("917", "%"),
        new LoincMapping(
            "59408-5", "Oxygen saturation in Arterial blood by Pulse oximetry", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("924", "sec."),
        new LoincMapping("3243-3", "Thrombin time", ONE, "s"));
    mappings.put(
        new LocalCodeWithUnit("925", "Giga/l"),
        new LoincMapping(
            "40574-6", "Platelets [#/volume] in Body fluid by Automated count", ONE, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("93", "mU/l"),
        new LoincMapping("20448-7", "Insulin [Units/volume] in Serum or Plasma", ONE, "u[IU]/mL"));
    mappings.put(
        new LocalCodeWithUnit("936", "µmol/l"),
        new LoincMapping("8245-3", "Zinc [Mass/volume] in Blood", ZINC, "ug/mL"));
    mappings.put(
        new LocalCodeWithUnit("937", "µmol/l"),
        new LoincMapping("47100-3", "Copper [Moles/volume] in Blood", ONE, "umol/L"));
    mappings.put(
        new LocalCodeWithUnit("938", "µg/l"),
        new LoincMapping(
            "83085-1",
            "Carcinoembryonic Ag [Mass/volume] in Serum or Plasma by Immunoassay",
            ONE,
            "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("94", "µg/l"),
        new LoincMapping("986-9", "C peptide [Mass/volume] in Serum or Plasma", ONE, "ng/mL"));
    mappings.put(
        new LocalCodeWithUnit("944", "mm/h"),
        new LoincMapping(
            "82477-1", "Erythrocyte sedimentation rate by Photometric method", ONE, "mm/h"));
    mappings.put(
        new LocalCodeWithUnit("951", "µg/ml"),
        new LoincMapping(
            "1832-5", "Alpha-1-Fetoprotein [Mass/volume] in Amniotic fluid", THOUSAND, "ug/L"));

    mappings.put(
        new LocalCodeWithUnit("955", "Zell./µl"),
        new LoincMapping("26466-3", "Leukocytes [#/volume] in Body fluid", THOUSANDTH, "10*3/uL"));
    mappings.put(
        new LocalCodeWithUnit("956", "µg/l"),
        new LoincMapping(
            "1668-3", "17-Hydroxyprogesterone [Mass/volume] in Serum or Plasma", HUNDRED, "ng/dL"));
    mappings.put(
        new LocalCodeWithUnit("957", "µg/l"),
        new LoincMapping(
            "1668-3", "17-Hydroxyprogesterone [Mass/volume] in Serum or Plasma", HUNDRED, "ng/dL"));
    mappings.put(
        new LocalCodeWithUnit("960", "µg/l"),
        new LoincMapping(
            "3013-0", "Thyroglobulin [Mass/volume] in Serum or Plasma", HUNDRED, "ng/dL"));
    mappings.put(
        new LocalCodeWithUnit("961", "%"),
        new LoincMapping("38505-4", "Thyroglobulin recovery in Serum or Plasma", ONE, "%"));
    mappings.put(
        new LocalCodeWithUnit("971", "Zell./µl"),
        new LoincMapping("30391-7", "Erythrocytes [#/volume] in Urine", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("972", "Zell./µl"),
        new LoincMapping("30405-5", "Leukocytes [#/volume] in Urine", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("973", "Zell./µl"),
        new LoincMapping("51480-2", "Bacteria [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("974", "Anz./µl"),
        new LoincMapping("51483-6", "Casts [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("976", "Anz./µl"),
        new LoincMapping("51482-8", "Crystals [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("977", "Zell./µl"),
        new LoincMapping(
            "51485-1",
            "Epithelial cells.non-squamous [#/volume] in Urine by Automated count",
            ONE,
            "/uL"));
    mappings.put(
        new LocalCodeWithUnit("978", "Anz./µl"),
        new LoincMapping(
            "78741-6", "Pathologic casts [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("979", "Anz./µl"),
        new LoincMapping("51481-0", "Yeast [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("980", "Anz./µl"),
        new LoincMapping(
            "51479-4", "Spermatozoa [#/volume] in Urine by Automated count", ONE, "/uL"));
    mappings.put(
        new LocalCodeWithUnit("983", "IU/ml"),
        new LoincMapping(
            "28652-6",
            "Heparinoid [Units/volume] in Platelet poor plasma by Chromogenic method",
            ONE,
            "[arb'U]/mL"));
    mappings.put(
        new LocalCodeWithUnit("984", "mg/lFEU"),
        new LoincMapping(
            "48065-7",
            "Fibrin D-dimer FEU [Mass/volume] in Platelet poor plasma",
            THOUSAND,
            "ng/mL{FEU}"));
    mappings.put(
        new LocalCodeWithUnit("993", "mIU/ml"),
        new LoincMapping(
            "15061-5", "Erythropoietin (EPO) [Units/volume] in Serum or Plasma", ONE, "[IU]/L"));
    return mappings;
  }

  public static Set<String> getLocalCodesWithUnknownConversion() {
    return Set.of(
        "10560", "10620", "504", "5408", "5410", "5411", "5412", "5413", "5419", "542", "5507",
        "5508", "5509", "5512", "587", "626", "775", "811", "921", "952", "988");
  }
}
