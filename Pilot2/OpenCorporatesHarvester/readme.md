#Open Corporates Harvester

The harvester takes a collection of OpenCorporates URLs (plain text file, one URL per line) and retrieves the RDF representations of those resources (one .RDF file per URL).

```java
File file = new File("companies.txt");
```

You will also need to specify your own output directory:

```java
String outputDir = "";
```

To get around the download limits, an API token needs to be appended to the <code>apiToken</code> string:
```java
String apiToken = "?api_token=";
```
