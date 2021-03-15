import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Mongo Imports	
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import org.bson.*;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;

//Log message control imports
import java.util.logging.Logger;
import java.util.logging.Level;

//Java imports

import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class MongoProject extends JFrame {

   // Tex Fields/Areas 
   JTextField input;
   JTextArea output;
   
   // Menu items
   JMenuBar mb;
   JMenu menu;
   JMenuItem conn, disconn;
	
   MongoDatabase sampleDB = null;
   MongoClient client = null;
   MongoCollection<Document> collection = null;
   MongoCursor<Document> cursor = null;
   MongoCursor<String> dbList = null;
   MongoCursor<String> collList =null;
   WindowListener exitListener = null;
	
   public MongoProject() {
      setSize(600, 200);
      setLocation(400, 500);
      setTitle("Access MongoDB");
      
      JFrame frame = new JFrame("Mongo Project: Search Tweets");
      
      JMenuBar mb = new JMenuBar();
      menu = new JMenu("Menu");
      conn = new JMenuItem("Connect");
      disconn = new JMenuItem("Disconnect");
      
      menu.add(conn);
      menu.add(disconn);
      mb.add(menu);
      
      setJMenuBar(mb);
      setSize(600,500);
      setLocation(500, 200);
      setLayout(null);
      setVisible(true);
   	
      Container cont = getContentPane();
      cont.setLayout(new BorderLayout() );
   	
      // Buttons
      JButton search = new JButton("Search");
      JButton clear = new JButton("Clear");
   	
      input = new JTextField(20);
   	
      output = new JTextArea(10, 30);
      JScrollPane spOutput = new JScrollPane(output);
   	
      JPanel northPanel = new JPanel();
      northPanel.setLayout(new FlowLayout());
      northPanel.add(input);
      northPanel.add(search);
      northPanel.add(clear);
   	
      cont.add(northPanel, BorderLayout.NORTH);
   			
      cont.add(spOutput, BorderLayout.CENTER);
   	
      conn.addActionListener(new ConnectMongo());
      disconn.addActionListener(new ExitMongo());
      search.addActionListener(new GetMongo());
      clear.addActionListener(new ClearMongo());
   	
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
      exitListener = 
         new WindowAdapter() {
         
            @Override
            public void windowClosing(WindowEvent e) {
               int confirm = JOptionPane.showOptionDialog(
                  null, "Are You Sure to Close Application?", 
                  "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
                  JOptionPane.QUESTION_MESSAGE, null, null, null);
            
               if (confirm == 0) {
               // Close the Mongo Client
                  client.close();
               
                  System.exit(0);
               }
            }
         };
      
      addWindowListener(exitListener);
   
   
   
      setVisible(true);
   	
   
   } //AccessMongo
	
   public static void main (String [] args) {
   	
   // The following statements are used to eliminate MongoDB Logging
   //   information suche as INFO messages that the user should not see.
   // It requires the import of Logger and Level classes.
      //Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
      //mongoLogger.setLevel(Level.INFO); 
      
      
      MongoProject runIt = new MongoProject();
   
   }//main
	
   class ConnectMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      //in this section open the connection to MongoDB. 
      //You should enter the code to connect to the database here
      //Remember to connect to MongoDB, connect to the database and connect to the 
      //    desired collection
      
         client = MongoClients.create();
         // client = MongoClients.create("mongodb://localhost:27017");
         client = MongoClients.create("mongodb://abp6318:group6password@cluster0-shard-00-00.bgnbf.mongodb.net:27017,cluster0-shard-00-01.bgnbf.mongodb.net:27017,cluster0-shard-00-02.bgnbf.mongodb.net:27017/MongoProject?ssl=true&replicaSet=atlas-zvs1gy-shard-0&authSource=admin&retryWrites=true&w=majority");
         
         output.append("Connection to server completed\n");
      
      //Get a List of databases on the server connection
         dbList = client.listDatabaseNames().iterator();
         output.append("LIST OF DATABASES\n");
      
         while (dbList.hasNext()) {
            output.append(dbList.next());
         //output.append(cursor.next().toJson());
            output.append("\n");
         }
          
      
      //access the database
         sampleDB = client.getDatabase("MongoProject");        
         output.append("Connection to database completed\n");
      //Get a List of collection in the database
         collList = sampleDB.listCollectionNames().iterator();
         output.append("LIST OF COLLECTIONS\n");
      
         while (collList.hasNext()) {
            output.append(collList.next());
            output.append("\n");
         }
      		
      //get the collection
      
         collection = sampleDB.getCollection("Tweets");
         
         output.append("Collection obtained\n");
      		
      }//actionPerformed
   } // class connectmango
   
   class ExitMongo implements ActionListener {
      public void actionPerformed( ActionEvent event )
      {
            output.append("\n");
            output.append("...Not Connected...\n");
            client.close();
            //System.exit(0);

      } // end method actionPerformed 
				
		} // class exit mongo

      
		
   class GetMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      // In this section you should retrieve the data from the collection
      // and use a cursor to list the data in the output JTextArea
       
      //Normal Find text
      //   String searchText = input.getText();
      //   cursor = collection.find(eq("fromUser", searchText)).iterator();
      
      //Normal Find id numeric value
      // int searchText = Integer.parseInt(input.getText());
      // cursor = collection.find(eq("id", searchText)).iterator();
      
      //Normal Find regex                 
         String searchText = input.getText();
         String regexPattern = "^" + searchText + "\\b.*";
      //System.out.println("Regex: " + regexPattern);
         cursor = collection.find(regex("text", regexPattern, "i")).iterator();
      
      //Set counter and print results (cursor)
         int cnt = 0; 
      
         while(cursor.hasNext()) {
            Document d = cursor.next();
            //output.append(d.toJson() + "\n");
            output.append(d.getInteger("id") + " " +d.getString("fromUser") + " " + d.getString("text") + "\n");
            cnt = cnt+1;
         }  
      
         output.append("The count is " + cnt + "\n");      
         
      
         	
      }//actionPerformed
   }//class GetMongo
	
   class ClearMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      //in this section open the connection. Should be able to see if it is not null
      // to see if ti is already open
         output.setText("");
      
      }//actionPerformed
   
   
   }//class ClearMongo


} //class