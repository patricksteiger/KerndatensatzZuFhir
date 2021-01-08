package enums;

public enum VitalStatus {
  LEBENDIG("L", "Patient lebt"),
  VERSTORBEN("T", "Patient verstorben"),
  NICHT_AUFFINDBAR("A", "unbekannt, Patient nicht mehr auffindbar (lost to follow-up)"),
  NACHSORGE_NICHT_NOETIG("N", "unbekannt, Betreuung/Nachsorge nicht mehr nötig"),
  ANDERORTS_BETREUUNG("B", "unbekannt, Patient ist anderenorts in Betreuung"),
  VERWEIGERT("V", "unbekannt, Patient verweigert weitere Betreuung"),
  UNBEKANNT("X", "unbekannt");

  private final String code;
  private final String display;

  VitalStatus(String code, String display) {
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
