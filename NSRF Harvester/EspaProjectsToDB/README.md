#The NSRF project harvester#

###Description-What this application is about.###
This Java (using Maven) application is downloading all the available information for a selected set of projects that are financed by the NSRF (National Strategic Reference Framework) 2007–2013 (ESPA in Greek). The information is taken from the official NSRF monitoring portal for Greece, (anaptyxi.gov.gr) which is maintained by the Greek Ministry for Development and Competitiveness.

###What this application does###
The application is using a direct link to the page of each NSRF financed projects using their unique id codes (OPS in Greek). From there all the information is copied to a local CSV file for each project and a database is updated with the harvested data.

###The Database###
The data stored to the database is distributed to a number of tables, created in such a form to handle queries of distinct records. Each project has a different record for every harvest taken, keeping the timeline of the project's process intact. This table, acting as the main table of the database, is then connecting the rest of the tables, either using its record id, or the project's id code. 

###How to execute the application###
The application is a Java executable (jar) file. It needs the Java Runtime Environment (JRE) installed, Version 8 or newer1. 
The application was designed in such a matter that it needs a list of project codes (MIS codes) to work. The list should be saved on a text file, having each code on a separate line.

Apart from the list of codes, the application needs a few more things in order to work.
The downloading directory, the path where the user wants to save the files
The email notification settings, a file containing the username, password and a recipient’s addresses, which will be used to automatically sent emails on errors and on successfully completing the process.
The Database settings file, a file containing the connection settings for the database
