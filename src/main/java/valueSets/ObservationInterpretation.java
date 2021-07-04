package valueSets;

import constants.CodingSystem;
import helper.Helper;
import interfaces.Code;

import java.util.Optional;

// TODO: What should be done with deprecated values?
/** @see "http://hl7.org/fhir/ValueSet/observation-interpretation" */
public enum ObservationInterpretation implements Code {
  GENETIC_OBSERVATION_INTERPRETATION(
      "_GeneticObservationInterpretation", "GeneticObservationInterpretation"),
  CAR("CAR", "Carrier"),
  ONSERVATION_INTERPRETATION_CHANGE(
      "_ObservationInterpretationChange", "ObservationInterpretationChange"),
  B("B", "Better"),
  D("D", "Significant change down"),
  U("U", "Significant change up"),
  W("W", "Worse"),
  OBSERVATION_INTERPRETATION_EXCEPTIONS(
      "_ObservationInterpretationExceptions", "ObservationInterpretationExceptions"),
  LT("<", "Off scale low"),
  GT(">", "Off scale high"),
  IE("IE", "Insufficient evidence"),
  OBSERVATION_INTERPRETATION_NORMALITY(
      "_ObservationInterpretationNormality", "ObservationInterpretationNormality"),
  A("A", "Abnormal"),
  AA("AA", "Critical abnormal"),
  HH("HH", "Critical high"),
  LL("LL", "Critical low"),
  H("H", "High"),
  HU("HU", "Significantly high"),
  L("L", "Low"),
  LU("LU", "Significantly low"),
  N("N", "Normal"),
  OBSERVATION_INTERPRETATION_SUSCEPTIBILITY(
      "_ObservationInterpretationSusceptibility", "ObservationInterpretationSusceptibility"),
  I("I", "Intermediate"),
  NCL("NCL", "No CLSI defined breakpoint"),
  NS("NS", "Non-susceptible"),
  R("R", "Resistant"),
  SYN_R("SYN-R", "Synergy - resistant"),
  S("S", "Susceptible"),
  SDD("SDD", "Susceptible-dose dependent"),
  SYN_S("SYN-S", "Synergy - susceptible"),
  EX("EX", "outside threshold"),
  HX("HX", "above high threshold"),
  LX("LX", "below low threshold"),
  OBSERVATION_INTERPRETATION_DETECTION(
      "ObservationInterpretationDetection", "ObservationInterpretationDetection"),
  IND("IND", "Indeterminate"),
  E("E", "Equivocal"),
  NEG("NEG", "Negative"),
  ND("ND", "Not detected"),
  POS("POS", "Positive"),
  DET("DET", "Detected"),
  OBSERVATION_INTERPRETATION_EXPECTATION(
      "ObservationInterpretationExpectation", "ObservationInterpretationExpectation"),
  EXP("EXP", "Expected"),
  UNE("UNE", "Unexpected"),
  REACTIVITY_OBSERVATION_INTERPRETATION(
      "ReactivityObservationInterpretation", "ReactivityObservationInterpretation"),
  NR("NR", "Non-reactive"),
  RR("RR", "Reactive"),
  WR("WR", "Weakly reactive");

  private final String code;
  private final String display;

  ObservationInterpretation(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public static Optional<ObservationInterpretation> fromCode(String code) {
    return Helper.codeFromString(ObservationInterpretation.values(), code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return CodingSystem.OBSERVATION_INTERPRETATION;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
