# KerndatensatzZuFhir
This library allows "Basismodule" to be parsed from a csv-File and converts "Basismodule" to their corresponding FHIR-Resources.
A csv-File contains the values of one type of "Basismodul" and has to be structured according to the parsing-strategy defined by the parser.
After the values have been parsed, the "toFhirResources"-method can be called, which checks for correctness of given values (e.g. required values can't be empty),
turns those values to the corresponding values described by the FHIR-Implementation-guides and returns a list of FHIR-Resources.

