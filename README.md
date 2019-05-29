# WolfDBHandler
A Mysql8 JDBC Handler for java

## How to use.
Simply load the jar file as a library. (Found in `releases` section).

To use, call the `DatabaseHandler` class.

1. Create a new instance of it, as it will hold the data to create the connection to the database.
2. Provide a `Map<String, String>` or a properties file name to one of the following functions.
~~~ JAVA
  DatabaseHandler dbh = new DatabaseHandler();
  // Calling the properties file.
  dbh.readProperties("properties.prop");
  // Calling the map for data.
  dbh.setDetails();
~~~
3. Open a connection before updating or reading from the database.
~~~ JAVA
  dbh.doConnect();
~~~
4. Close the database connection after you are done.
~~~ JAVA
  dbh.doClose();
~~~


## Avaliable methods
~~~
  readProperties( );
  doStatement( );
  setDetails( );
  doConnect( );
  doUpdate( );
  doQuery( );
  doClose( );
~~~

### Function descriptions

#### `readProperties( );`
> Returns a map of all the variables from the propertie file.
Usage : `dbh.readProperties( propertyFileNameString );`
Used to load all the config data from a properties file. Read below for what data is required in both the map and the properties file.

#### `doStatement( );`
> Returns either an int or ResultSet depending on chosen method.
Usage : `doStatement( sqlQueryString );` - Returns ResultSet.
Usage : `doStatement( sqlQueryString, "update" );` - Returns int.
Used to query or update the database.

#### `setDetails( );`
> Returns hashmap of what the internal variables have been set to.
Usage : `dbh.setDetails( Map<String, String> );`
Used to set the connection information.

#### `doConnect( );`
> Returns true or false based on if the connection was successful or not.
Usage : `dbh.doConnect();`
Used to open a connection to the database on which you can run your update or query commands.

#### `doUpdate( );`
> There are multiple overloaded methods regarding this, all return an int of 1 or 0. 1 means it ran sucessfully.
Usage : `dbh.doUpdate( sqlQueryString );`
Usage : `dbh.doUpdate( sqlQueryString, List<Object> );`
The second overloaded method replaces all the `?` in your `sqlQueryString` with the data in the relative position of `List<Object>`

#### `doQuery( );`
> There are multiple overloaded methods regarding this, all return a ResultSet.
Usage : `dbh.doQuery( sqlQueryString );`
Usage : `dbh.doQuery( sqlQueryString, List<Object> );`
The second overloaded method replaces all the `?` in your `sqlQueryString` with the data in the relative position of `List<Object>`

#### `doClose( );`
> Returns true or false based on if the operation was successful
Usage : `dbh.doClose();`
Used to close the connection to the database.


### Connection information.
 - `url` - Url is simply the http:// url of the database, along with the `/` database. (There is no need for the `jdbc` information at the start.
EG. `http://localhost:3306/mydb`
 - `username` - String of the username to connect to the database.
 - `password` - String of the password to connect to the database.
