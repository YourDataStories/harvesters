###V 2.0 Fetch the Public Works, their related subprojects and add them to the RDF graph.

##How to execute the application
The application is a Java executable (jar) file. It needs the Java Runtime Environment (JRE) installed, Version 8 or newer1. 

##Description of Packages

- JPA package is responsible for object/relational mapping facility for managing relational data.
- DAO Package is responsible for handling the database operation required to manipulate the entities.
- DTO Package is responsible for transfering the data between classes and modules of application.
- Service Package is responsible for providing logic to operate on the data sent to and from the DAO and the client.
- Ontology Package includes the Ontology Specification and the data model.
- RDF Package is responsible for transformation of data to RDF. Those relational data are delivered by Service layer.