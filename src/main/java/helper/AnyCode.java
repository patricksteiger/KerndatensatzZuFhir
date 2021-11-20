package helper;

import interfaces.Code;
import unit.mapping.LocalDisplayMapper;
import unit.mapping.LoincMapping;
import unit.mapping.Mapper;

import static constants.CodingSystem.LOINC;

public class AnyCode implements Code {
  private final String code;
  private final String system;
  private final String display;

  private AnyCode(String code, String system, String display) {
    this.code = code;
    this.system = system;
    this.display = display;
  }

  public static Code from(String code, String system, String display) {
    return new AnyCode(code, system, display);
  }

  public static Code from(Code code) {
    return new AnyCode(code.getCode(), code.getSystem(), code.getDisplay());
  }

  public static AnyCode fromLoincMapping(LoincMapping loincMapping) {
    return new AnyCode(loincMapping.getCode(), LOINC, loincMapping.getDisplay());
  }

  // TODO: What is the system of local codes?
  public static AnyCode fromLocalSystem(ParsedCode localCode) {
    return LocalDisplayMapper.fromLocalCode(localCode.getCode())
        .map(display -> new AnyCode(localCode.getCode(), "", display))
        .orElseGet(() -> new AnyCode(localCode.getCode(), LOINC, localCode.getDisplay()));
  }

  public static AnyCode fromLocalCodeToLoinc(ParsedCode parsedCode, String localUnit) {
    return Mapper.getLoincMappingFromLocalCodeAndUnit(parsedCode.getCode(), localUnit)
        .map(AnyCode::fromLoincMapping)
        .orElseGet(() -> AnyCode.fromLocalSystem(parsedCode));
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getSystem() {
    return system;
  }

  @Override
  public String getDisplay() {
    return display;
  }
}
