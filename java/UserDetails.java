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


public class UserDetails extends JFrame {
   
   // Public variables
   public String image;
   public String text;
   public String userName;
   public String location;
   public String date;
   
   // Frame for the user details
   JFrame userFrame;

   public UserDetails(String image, String text, String userName, String location, String date) {
   
      this.image = image;
      this.text = text;
      this.userName = userName;
      this.location = location;
      this.date = date;
      
      // Creating jframe
      userFrame = new JFrame(userName);
      setSize(1000,700);
      setLocation(500, 200);
      setLayout(null);
      setVisible(true);
      
      setTitle(userName);

   
   } // End of Constructor
   
   @Override
   public String toString() {
   
      return image + text + userName + location + date;
   
   } // End of toString method

} // End of UserDetails Class