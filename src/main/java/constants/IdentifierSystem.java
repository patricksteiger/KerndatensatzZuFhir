package constants;

public class IdentifierSystem {

    private static final String NAMING_SYSTEM = "https://www.medizininformatik-initiative.de/fhir/core/NamingSystem/";

    public static final String LOCAL_PID = "http://uniklinik-ulm.de/fhir/NamingSystem/pid";
    public static final String NS_DIZ = NAMING_SYSTEM + "DIZ";
    public static final String VERSICHERTEN_ID_GKV = "http://fhir.de/NamingSystem/gkv/kvid-10";
    public static final String PID = NAMING_SYSTEM + "patient-identifier";
    public static final String ORGANIZATION_REFERENCE_ID = "http://fhir.de/NamingSystem/arge-ik/iknr";
    public static final String TYPE_CODE = "http://terminology.hl7.org/CodeSystem/v2-0203";
}
