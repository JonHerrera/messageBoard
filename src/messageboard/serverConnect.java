/* ////////   //////    //    ///////    //       // //////
 * //     //  //      //  //  //    //   ////   //// // 
 * //     //  //     //    // //     //  // // // // //
 * ////////   /////  //////// //      // //  //   // ///// 
 * //   //    /////  //    // //      // //       // /////
 * //    //   //     //    // //     //  //       // // 
 * //     //  //     //    // //    //   //       // //
 * //     //  ////// //    // ///////    //       // ///////
 * 
 * This class is used to handle server connections
 * I realized at 22:25 07/01/2016 that all my work is on a virtual database that I cannot send, therefore,
 * To use the app, you must create a database following this information
 * Database name must me messages
 * Database username must be user1
 * DB password must be pass1
 * Table name must be messages
 * A NAME column must be made as a VARCHAR, set to null, (limit 30)
 * A MESSAGE column must be made as a VARCHAR, set to null (limit 180)
 * 
 */
package messageboard;
//the following are imported to connect with the Database
    import java.awt.event.ActionEvent;
    import javax.swing.JOptionPane;
    import javax.swing.*;
    import java.sql.*;
    import java.sql.Connection;
    //this is used to establish the connection 
    import java.sql.DriverManager;
    //this is used to manage the database connection
    import java.sql.SQLException;
    //this particular import is to handle any exception errors
    import java.sql.Statement;
    //this is used manipulate the database from NetBeans
    import java.sql.ResultSet;
    //this is to display the SQL being called

public class serverConnect {
//extending the other class so I can call its variables
//    static MessageBoard msgBrd = new MessageBoard(); this line creates a second window. Don't use.
    
    //making the login info variables for easy reference
            private final static String host = "jdbc:derby://localhost:1527/messages";
            private final static String uName = "user1";
            private final static String uPass = "password1";

            private static Connection con = null;
            //this will establish the connection
            private static Statement stmt;
            //not exactly sure what this does
            private static PreparedStatement prepsInsertProduct = null;  
            //gets the message ready for upload
            
//            String msg = submitPressed();
//            stmt.executeUpdate("INSERT INTO messages " + "VALUES (6, 'Mike', 'WTF')");
            private static ResultSet rs = null;
            //this holds all records from the DB table
                //it is used to move through the data
            
    

    
    static Connection establishConnection() {
    //the following codes will connect to the Database at the beginning
    //the connection is placed into the try and catch method in case it fails to connect
    
    try {
        con = DriverManager.getConnection(host, uName, uPass);
    }
    catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Failed to establish connection.");
        System.out.println(e.getMessage());
    }
//        try {
//            con = DriverManager.getConnection(host, uName, uPass);
//            // Create and execute an INSERT SQL prepared statement.  
//                    String insertSql = "INSERT INTO messages (MESSAGE) VALUES " + message;
//      
//                    uploadMessage = con.prepareStatement(  
//                        insertSql,  
//                        Statement.RETURN_GENERATED_KEYS);  
//                    uploadMessage.execute();  
//                      
//                    // Retrieve the generated key from the insert.  
//                    rs = uploadMessage.getGeneratedKeys();  
//                      
//                    // Print the ID of the inserted row.  
//                    while (rs.next()) {  
//                        System.out.println("Generated: " + rs.getString(1));  
//                    }  
//
//            con.close();
//            //this will close the connection to ensure security
//        }
//        catch(SQLException e) {
//            System.out.println("Exception occured");
//            System.out.println(e.getMessage());
//        }
        return con;
    }
    
    static void postMessage(String time, String userName, String userMessage) {
    //to post the user's message to the Database
    
        try {
            establishConnection();
            if (userName.equals("") || userMessage.equals("")) {
            //prevents empty messages by looking for a name and message
                JOptionPane.showMessageDialog(null, "Please enter a name and message!");
            }
            else {
                String insertSQL = "INSERT INTO messages (TIME, NAME, MESSAGE) VALUES ('" + time + "', '" + userName + "', '" + userMessage + "')";
                    //OMMMMGGGG. I spent forever trying t fix this code, turns out, I don't need a ; after 'potato')
                stmt = con.createStatement();
                prepsInsertProduct = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);  
                prepsInsertProduct.execute(); 
                rs = prepsInsertProduct.getGeneratedKeys();   //Assigns the executed query to resultSet Object

                while (rs.next())   
                {  
                //to see if connected to DB: will go through DB and display each name and message
                    System.out.println("Posted!");
                //Displays the success Message after insertion
                }  
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Apostrophes can't be used. Sorry grammar enthusiasts!"); 
            System.out.println(e); 
                //Displays the error message when the connection or query fails  
                //Before, I had it say a custom text. After hours of srtuggling I decided to make it say the actual error message
                    //two seconds after it does, I solve the problem. *facepalm*
        }
        finally {
            // Close the connections after the data has been handled.  
            if (prepsInsertProduct != null) try { prepsInsertProduct.close(); } catch(Exception e) {}  
            if (rs != null) try { rs.close(); } catch(Exception e) {}   
            if (con != null) try { con.close(); } catch(Exception e) {}  
        }
        
    }
    
    static void initialDisplay() {
    //opens the connection to the server then grabs all the information to then post to the screen

         try {  
            establishConnection();       
            //establishes connection every time you hit submit
        // Create and execute a SELECT SQL statement.  
            String getSQL = "SELECT * FROM messages";
            //this will insert the user's message into the SQL
            stmt = con.createStatement();
            rs = stmt.executeQuery(getSQL);
//            prepsInsertProduct = con.prepareStatement(insertSql,Statement.RETURN_GENERATED_KEYS);  
//            prepsInsertProduct.execute(); 
//            rs = prepsInsertProduct.getGeneratedKeys();   //Assigns the executed query to resultSet Object

            while (rs.next())   
            {  
            //to see if connected to DB: will go through DB and display each name and message
                String time = rs.getString("Time");
                String name = rs.getString("Name");
                String message = rs.getString("Message");
                System.out.print(time + ":");
                System.out.println(name + ":");
                System.out.println("   " + message);
                MessageBoard.displayMessage(time, name, message);
                //this will take the retrieved strings, send it to the other class, then display it onto the GUI
//                JOptionPane.showMessageDialog(null,"Inserted Successfully!"); //Displays the success Message after insertion
            }  
        }  
        catch (Exception e) {  
            System.out.println("Failed to Create Connection & Perform Query Action"); //Displays the error message when the connection or query fails  
        }  
        finally {  
            // Close the connections after the data has been handled.  
            if (prepsInsertProduct != null) try { prepsInsertProduct.close(); } catch(Exception e) {}  
            if (rs != null) try { rs.close(); } catch(Exception e) {}   
            if (con != null) try { con.close(); } catch(Exception e) {}  
        }
         
    }
    
    public void submitEvent(ActionEvent e) {
        
        serverConnect svr = new serverConnect();

    }
}

