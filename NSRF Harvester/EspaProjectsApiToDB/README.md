In order to use the application, a file containing the information of the database connection is needed. The username, password and full address of the MySQL database is required. The file should be in the following format:

Username= [The username]
Password= [The password]
DataBase= [Full connection address]

Since there are many projects, and the API doesn’t provide any other way to access a particular fragment of the list, we use offsets to access the list. Thus, we need to provide the following information:

Start page: from which page/offset of the list we want to search from
End page (Optional): The last page/offset of the list we want to search from
Page size: The number of items each page/offset will return (maximum 1000)

Also there is the option to select which projects to get, public works or subsidies. In particular the projects are separated by their status, so if we want the approved public works we pass the parameter ergaIn, if we want the disapproved ones we use the parameter ergaOut, and following the same pattern we have enisxIn and enisxOut, for the subsidies.

In this context, the full command to call the application is:
java -jar Espa4.jar [path to database connection credentials file] [starting page] [finish page(Optional)] [page size] [type (one of[enisxIn],[enisxOut],[ergaIn],[ergaOut],[all])]
