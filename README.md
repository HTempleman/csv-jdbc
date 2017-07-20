# csv-jdbc
## Purpose:
This Java program is intended to be used for uploading a simply formatted csv file into a mySQL database using the JDBC API. 
Though it is more of proof of concept than a program meant for deployment, the code may be modified to suit the needs of the user. 
Specifically, the mySQL databaseURL variable should be replaced with a url for a mySQL database of the user's choosing, as opposed 
to the one that I have provided. Besides that, a couple snippets of code may need to be commented out for the purposes of usability 
(namely the one that immediately deletes the newly created mySQL table). 

## Usage:
Here I'll assume limited knowledge of java and break down the usage of this simple program into a series of steps:

# Step One:
Ensure that you have the Java Development Kit (JDK) installed. 
You can access a download [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

# Step Two:
These instructions are written with Unix in mind. Using a command line interface, e.g. *Terminal* on Mac or *xterm* on Linux, 
navigate to the directory containing the CSVIntoSQL program files. Issue the following command:
```
javac -cp .:opencsv-3.9.jar CSVIntoSQL.java

```
If using command prompt on Windows, you will want to replace the colons *:* with semicolons *;*.

# Step Three:
Run the java program:
```
java -cp .:opencsv-3.9.jar:mysql-connector-java-5.1.42-bin.jar CSVIntoSQL

# Step Four:
You're done, the program should be able to run fine or at least explain itself to you if things go awry.





