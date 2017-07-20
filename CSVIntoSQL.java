//The following script is for uploading data from a csv provided by the user
//into a remote MySQL database. It is a simple program, so try not to confuse it
//too much ya hear!
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.StringBuilder;

public class CSVIntoSQL {
    public static void main(String[] args) {

        //Declare basic objects that will be needed.
        String databaseURL = "jdbc:mysql://159.203.74.78:3306/javaexample?useSSL=false";
        String user = "testguest";
        String password = "Chamfer0764!Gabloo";
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(databaseURL, user, password);
            if (conn != null) {
                System.out.println("\nYay! You're connected to a mySQL database! :D\n");


                //Ask user to provide csv file for input.
                System.out.println("Please provide the pathway \nFor the csv file you'd like to upload:");
                Scanner csvPathway = new Scanner(System.in);
                String csvFile = csvPathway.next();
                System.out.println();

                //Invoke CSVReader for building a header..
                CSVReader headerReader = new CSVReader(new FileReader(csvFile), ',');
                //Store the first line of data (The header).
                String[] headContainer = headerReader.readNext();
                //Repeat process for everything but the header.
                CSVReader dataReader = new CSVReader(new FileReader("example.csv"), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
                //Set container to be used later for reading in data.
                String[] dataContainer;


                //Create a statement for making a SQL table.
                Statement s = conn.createStatement();
                System.out.println("Why, Thank you! What would you like to name the table we're creating?\n\n(Warning: Using a name that incorporates a SQL command or symbol will crash the program)\n");
                System.out.println("Please enter a desired table name: ");
                //Get the user table name and build a statement for creating said table.
                Scanner nameRequest = new Scanner(System.in);
                String name = nameRequest.next();
                StringBuilder insertHeaderQuery = new StringBuilder("CREATE TABLE " + name + " (");
                for(int i = 0; i < headContainer.length; i++){
                  insertHeaderQuery.append(" " + headContainer[i] + " VARCHAR(20)");
                  if(i != (headContainer.length -1)){
                    insertHeaderQuery.append(",");
                  }else{
                    insertHeaderQuery.append(" );");
                  }
                }

                //Create the table.
                s.executeUpdate(insertHeaderQuery.toString());

                //Build a prepared statement
                StringBuilder insertDataQuery = new StringBuilder("INSERT INTO " + name + " (");
                for(int i = 0; i<headContainer.length; i++){
                  insertDataQuery.append(headContainer[i]);
                  if(i != (headContainer.length -1)){
                    insertDataQuery.append(", ");
                  }else{
                    insertDataQuery.append(") ");
                  }
                }
                //Now, furnish the 'INSERT' query with value placeholders.
                insertDataQuery.append("VALUES (");
                for(int i= 0; i < headContainer.length; i++){
                  insertDataQuery.append("?");
                  if(i != (headContainer.length -1)){
                    insertDataQuery.append(",");
                  }else{
                    insertDataQuery.append(");");
                  }
                }
                //Finally, update the SQL table with the values in the csv file.
                PreparedStatement pstmt = conn.prepareStatement(insertDataQuery.toString());
                while((dataContainer = dataReader.readNext()) != null){
                  for(int i = 0; i<dataContainer.length; i++){
                      //System.out.println(datcontainer[i]);
                      pstmt.setString((i % dataContainer.length) + 1, dataContainer[i]);
                  }
                  //Update the database.
                  pstmt.addBatch();
                  pstmt.executeBatch();



                }
                //Obtain a result set showing the user that a table has been created:
                ResultSet rs = pstmt.executeQuery("SELECT Name, Salary, Position FROM " + name);
                ResultSetMetaData rsmd = rs.getMetaData();
                String columnType = rsmd.getColumnTypeName(1);
                System.out.println("\nWhat a wonderful name!\n\nThe first column uploaded from your csv file is \nStored as a SQL value of the following type: \n");
                System.out.println(columnType + "\n");

                //Immediately delete the table (kinda makes the program useless in the long run don't it?)
                Statement drop = conn.createStatement();
                drop.executeUpdate("DROP TABLE " + name + ";");

                //Bid the user farewell.
                System.out.println("I bid thee farewell...\n\n");



            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Could not find database driver class");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("\n\nAn error occurred. Maybe user/password is invalid\n\nWorse yet, you may have named your table with a SQL command. How dare you!");
            System.out.println("\nPerhaps you could try again with a simpler name?");
            System.out.println("\nTry to avoid confusing symbols like ';' or ':' and the like");
            System.out.println();

        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("\n\nThere's something fishy going on...\n\nMaybe the file pathway you provided was incorrect?");
            System.out.println();
        }  finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
