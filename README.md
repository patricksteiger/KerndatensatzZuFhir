# KerndatensatzZuFhir
This library allows [Basismodule](https://www.medizininformatik-initiative.de/de/basismodule-des-kerndatensatzes-der-mii) to be parsed from a csv-File and converts them to their corresponding FHIR-Resources.
A csv-File contains the values of one type of Basismodul and has to be structured according to the parsing-strategy defined by the parser.
After the values have been parsed, the _toFhirResources_-method can be called, which checks for correctness of given values (e.g. required values can't be missing),
turns those values to the corresponding values described by the [FHIR-Implementation-guides](https://simplifier.net/organization/koordinationsstellemii/~guides)
and returns a list of FHIR-Resources.

