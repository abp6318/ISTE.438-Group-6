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

 import com.mongodb.client.model.geojson.Point;
 import com.mongodb.client.model.geojson.*;
 import com.mongodb.client.model.Indexes;
 import com.mongodb.client.model.Filters;
 import com.mongodb.client.FindIterable;

//Log message control imports
import java.util.logging.Logger;
import java.util.logging.Level;

//Java imports

import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class MongoProject extends JFrame {

   // Text Fields/Areas 
   JTextField inputText;
   JTextField inputLoc;
   JTextArea message;
   
   // JLabels
   JLabel label1;
   JLabel label2;
   
   // Menu items
   JMenuBar mb;
   JMenu menu;
   JMenuItem conn, disconn;
   
   //JFrame cenFrame = new JFrame();
   
   // Center Panel
   JPanel centerPanel = new JPanel();
	
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
      // Title of GUI
      setTitle("Find a Tweet!"); // Title
      
      // Menu Bar
      JFrame frame = new JFrame("Mongo Project: Search Tweets");
      JMenuBar mb = new JMenuBar();
      menu = new JMenu("Menu");
      conn = new JMenuItem("Connect");
      disconn = new JMenuItem("Disconnect");
      
      // Add connection, disconnection
      menu.add(conn);
      menu.add(disconn);
      mb.add(menu);
   
      setJMenuBar(mb);
      setSize(1300,700);
      setLocation(500, 200);
      setLayout(null);
      setVisible(true);
   	
      Container cont = getContentPane();
      cont.setLayout(new BorderLayout() );
   	
      // Buttons
      JButton search1 = new JButton("Search");
      JButton clear1 = new JButton("Clear");
      JButton search2 = new JButton("Search");
      JButton clear2 = new JButton("Clear");
   	// New Text Field
      inputText = new JTextField(20);
      inputLoc = new JTextField(20);
      // Label
      label1 = new JLabel("Search by Text: ");
      label2 = new JLabel("Search by Location (longitude: -86.15, latitude: 39.76): " );
   	//Scroll Panell
      JScrollPane cenOutput = new JScrollPane();
      
      // The scroll bar for the button area
      JScrollPane buttonScrollPane = new JScrollPane(centerPanel);
      //Scroll for message area
      message = new JTextArea(10, 20);
      JScrollPane spOutput = new JScrollPane(message);
      
      JPanel northPanel = new JPanel();
      northPanel.setLayout(new FlowLayout());
      centerPanel.setLayout(new GridLayout(5,7));
      // Search and clear by text
      northPanel.add(label1);
      northPanel.add(inputText);
      northPanel.add(search1);
      
      // Search and clear by location
      northPanel.add(label2);
      northPanel.add(inputLoc);
      northPanel.add(search2);
      northPanel.add(clear2);
      
      //centerPanel.add(cenFrame);
   	
      cont.add(northPanel, BorderLayout.NORTH);
      cont.add(buttonScrollPane, BorderLayout.CENTER);		
      cont.add(spOutput, BorderLayout.SOUTH);
   	
      // Need functionality for location search*****
      conn.addActionListener(new ConnectMongo());
      disconn.addActionListener(new ExitMongo());
      search1.addActionListener(new GetMongo());
      search2.addActionListener(new GetLocMongo());
      clear2.addActionListener(new ClearMongo());
   	
      //Exit
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
      
      MongoProject runIt = new MongoProject();
   
   }//main
	
   class ConnectMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      
         client = MongoClients.create();
         // Url source to mongodb
         client = MongoClients.create("mongodb://abp6318:group6password@cluster0-shard-00-00.bgnbf.mongodb.net:27017,cluster0-shard-00-01.bgnbf.mongodb.net:27017,cluster0-shard-00-02.bgnbf.mongodb.net:27017/MongoProject?ssl=true&replicaSet=atlas-zvs1gy-shard-0&authSource=admin&retryWrites=true&w=majority");
         message.append("Connection to server completed\n");   
      
      //Get a List of databases on the server connection
         dbList = client.listDatabaseNames().iterator();
         message.append("LIST OF DATABASES\n");
      
         while (dbList.hasNext()) {
            message.append(dbList.next());
         //output.append(cursor.next().toJson());
            message.append("\n");
         }
          
      
      //access the database
         sampleDB = client.getDatabase("MongoProject");        
         message.append("Connection to database completed\n");
      //Get a List of collection in the database
         collList = sampleDB.listCollectionNames().iterator();
         message.append("LIST OF COLLECTIONS\n");
      
         while (collList.hasNext()) {
            message.append(collList.next());
            message.append("\n");
         }
      		
      //get the collection
      
         collection = sampleDB.getCollection("Tweets");
         
         message.append("Collection obtained\n");
      		
      }//actionPerformed
   } // class connectmango
   
   class ExitMongo implements ActionListener {
      public void actionPerformed( ActionEvent event )
      {
         message.append("\n");
         message.append("...Not Connected...\n");
         client.close();
            //System.exit(0);
      
      } // end method actionPerformed 
   			
   } // class exit mongo
      
   class GetMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      
      //Normal Find regex                 
         String searchText = inputText.getText();
         String regexPattern = "\\b" + searchText;
      //System.out.println("Regex: " + regexPattern);
         cursor = collection.find(regex("text", regexPattern, "i")).iterator();
      
      //Set counter and print results (cursor)
         int cnt = 0; 
      
         while(cursor.hasNext()) {
            Document d = cursor.next();
            cnt = cnt+1;
            JButton button = new JButton(d.getString("name"));
            button.addActionListener(
               new ActionListener() { 
                  public void actionPerformed(ActionEvent e) { 
                     System.out.println(d.getString("name"));
                   // Get String: Profileimage, text, name, tweet loc, created date and image ref
                     UserDetails user = new UserDetails(d.getString("profileimage"), 
                                                      d.getString("text"), 
                                                      d.getString("name"), 
                                                      d.getString("tweet_location"),
                                                      d.getString("tweet_created"),
                                                      d.getString("image_reference"),
                                                      sampleDB);
                  } 
               });
            
            //button.add(message);
            // int count = 0;
           
            centerPanel.add(button, BorderLayout.CENTER); // Center Panel
              
         }  
         centerPanel.revalidate();
         message.append("The count is " + cnt + "\n");      
                  	
      }//actionPerformed
   }//class GetMongo


   class GetLocMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      
      //Get long and lat coordinates               
         String searchText = inputLoc.getText();
         String[] coords = searchText.split(", ", 2);
         
      //Geospatial Indexing
         //Point currentLoc = new Point(new Position(-73.9667, 40.78)); //test data
         //cursor = collection.find(Filters.near("location", currentLoc, 100000.0, 10.0));
         Point currentLoc = new Point(new Position(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
         FindIterable<Document> findIterable = collection.find( Filters.near("loc", currentLoc, 100000.0, 0.0)); //loc, current location, max distance, min distance
                                                                                             //100 km, 100000m
      //Set counter and print results (cursor)
         int cnt = 0; 
      
         for(Document d : findIterable) {
            //Document d = cursor.next();
            //Document d = findIterable.next();
            cnt = cnt+1;
            JButton button = new JButton(d.getString("name"));
            button.addActionListener(
               new ActionListener() { 
                  public void actionPerformed(ActionEvent e) { 
                     System.out.println(d.getString("name"));
                   // Get String: Profileimage, text, name, tweet loc, created date and image ref
                     UserDetails user = new UserDetails(d.getString("profileimage"), 
                                                      d.getString("text"), 
                                                      d.getString("name"), 
                                                      d.getString("tweet_location"),
                                                      d.getString("tweet_created"),
                                                      d.getString("image_reference"),
                                                      sampleDB);
                  } 
               });
            
            //button.add(message);
            // int count = 0;
           
            centerPanel.add(button, BorderLayout.CENTER); // Center Panel
              
         }  
         centerPanel.revalidate();
         message.append("The count is " + cnt + "\n");      
                  	
      }//actionPerformed
   }//class GetLocMongo
     
	// Clear Options
   class ClearMongo implements ActionListener {
      public void actionPerformed (ActionEvent event) {
      //in this section open the connection. Should be able to see if it is not null
      // to see if ti is already open
         centerPanel.removeAll();
         centerPanel.validate();
         centerPanel.repaint();
      
      
      }//actionPerformed
   
   
   }//class ClearMongo


} //class
