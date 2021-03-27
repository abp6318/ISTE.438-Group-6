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
import com.mongodb.client.model.Updates.*;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;

// GridFS
import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.*;

// IO things
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;

//Log message control imports
import java.util.logging.Logger;
import java.util.logging.Level;

//Java imports

import java.util.Arrays;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;


public class UserDetails extends JFrame {

   MongoDatabase sampleDB = null;
   MongoClient client = null;
   MongoCollection<Document> collection = null;
   MongoCursor<Document> cursor = null;
   MongoCursor<String> dbList = null;
   MongoCursor<String> collList =null;   
   
   // Public variables
   public String image;
   public String text;
   public String userName;
   public String location;
   public String date;
   public String image_reference;
   
   // JPanels for layout
   JPanel top;
   JPanel middle;
   JPanel bottom;
 
   // Frame for the user details
   JFrame userFrame;
   JTextArea getComment;
   public JTextField commentText;

   public UserDetails(String image, String text, String userName, String location, String date, String image_reference, MongoDatabase sampleDB) {
      
      this.image = image;
      this.text = text;
      this.userName = userName;
      this.location = location;
      this.date = date;
      this.image_reference = image_reference;
      this.sampleDB = sampleDB;
      
      // Creating jframe
      userFrame = new JFrame(userName);
      setSize(1000,700);
      setLocation(500, 200);
      setLayout(null);
      setVisible(true);     
      setTitle(userName);
      
      Container gui = getContentPane();
      gui.setLayout(new BorderLayout());
      
      top = new JPanel();
      middle = new JPanel();
      bottom = new JPanel();
      
      // New Text Field
      getComment = new JTextArea(10,20);
      commentText = new JTextField(50); // Comment text field for "make a comment"
      
      // Add panels to gui
      gui.add(top, BorderLayout.NORTH);
      gui.add(middle, BorderLayout.CENTER);		
      gui.add(bottom, BorderLayout.SOUTH);
      
      // JLabel variables
      JLabel title = new JLabel("Detail View");
      JLabel dateLabel = new JLabel("Date: ");
      JLabel imageLabel = new JLabel("Image: ");
      JLabel tweetLabel = new JLabel("Tweet: ");
      JLabel locLabel = new JLabel("Location: ");
      JButton comment = new JButton("Make a Comment");
      
      // Details to go on panel
      JTextField dateTime = new JTextField(date);
      dateTime.setEditable(false);
      
      // Location Named placeholders if blank
      if (location.equals("")) {
         location = "No Location Available";
      }
      
      JTextField loc = new JTextField(location);
      loc.setEditable(false); // Lock location editable textbox
      
      JTextArea tweet = new JTextArea(text);
      tweet.setEditable(false); // Lock tweet desc textbox
   
      Image img = null; // null images
   
      if(!image_reference.equals("")){
         // then get the reference from GridFS
         GridFSBucket gridFs = GridFSBuckets.create(sampleDB, "files"); // sampleDB is a reference to the database
      
         try{
            FileOutputStream streamToDownloadTo = new FileOutputStream("temp.jpeg"); // where it's going
            GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0); // boiler
            gridFs.downloadToStream(image_reference, streamToDownloadTo, downloadOptions); // where it is coming from
            streamToDownloadTo.close();
         
            File file = new File("temp.jpeg"); // grabbing it from local storage
            img = ImageIO.read(file);
            JLabel lblimage = new JLabel(new ImageIcon(img)); // how we display it
            middle.add(imageLabel); // where we display it
            middle.add(lblimage);
         }catch(IOException e){
            e.printStackTrace();
         }// end of try catch block
      } else {
         // try catch with URL goes here
         // also include a default image
         
      // Try catch, error handling
         try {
            URL url = new URL(image);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            int responseCode = httpsURLConnection.getResponseCode();
            if(responseCode == 404){
               // default
               JLabel lblimage = new JLabel("No Image Available");
               middle.add(lblimage);
            }else{
               // load url through ImageIO
               img = ImageIO.read(url);
               JLabel lblimage = new JLabel(new ImageIcon(img));
               middle.add(imageLabel);
               middle.add(lblimage);
            }
         } catch (IOException e) {} // end of try catch statement
      
      } // end of if else statement
      
      // Add title, date, location, tweet, and comment
      top.add(title);
      middle.add(dateLabel);
      middle.add(dateTime);
      
      middle.add(locLabel);
      middle.add(loc);
      middle.add(tweetLabel);
      middle.add(tweet);
      bottom.add(getComment);
   
      bottom.add(commentText);
      bottom.add(comment);
      //bottom.add(getComment);
      
      // Comment Functionality
      comment.addActionListener(new makeComment(userName, sampleDB, commentText));
      
      
   } // End of Constructor
   
   @Override
   public String toString() {
   
      return image + text + userName + location + date;
   
   } // End of toString method
   

} // End of UserDetails Class


class makeComment implements ActionListener {
   MongoDatabase db;
   String name;
   JTextField commentText;

   public makeComment(String name, MongoDatabase db, JTextField commentText){
      this.name = name;
      this.db = db;
      this.commentText = commentText;
   }
   public void actionPerformed (ActionEvent event) {
      String comText = commentText.getText();
      System.out.println(comText);
      System.out.println(db);
      
   
      //db.getCollection("Tweets").update("{name:" + name + "}","{$push:{comments:" + comText + "}}");
      
      db.getCollection("Tweets").findOneAndUpdate(Filters.eq("name", name), new Document().append( "$push", new Document("comments",comText)));
      
   
   
                  	
   }//actionPerformed
}//class makeComment
