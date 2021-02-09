package helper;

public class Logger {
  private final String datablock;

  public Logger(String datablock) {
    this.datablock = datablock;
  }
  public <T extends Object> T log(
      String fhirResourceClass, String valueName, String value, String method) {
    System.err.println(
        "In datablock \""
            + datablock
            + "\" the FHIR-Resource \""
            + fhirResourceClass
            + "\" misses a value due to an invalid datablock-value!");
    System.err.println("Valuename: \"" + valueName + "\", invalid value: \"" + value + "\".");
    System.err.println("Method: \"" + method + "\".\n");
    return null;
  }
}
