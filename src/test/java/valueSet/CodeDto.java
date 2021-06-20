package valueSet;

public class CodeDto {
  private final String code;
  private final String display;

  public CodeDto(String code, String display) {
    this.code = code;
    this.display = display;
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }
}
