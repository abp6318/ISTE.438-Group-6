# ISTE.438-Group-6

## Setting Up

If you are on an RIT lab computer, or have taken ISTE.438, you should be able to run these without additional setup. Otherwise...

1. Must have Java 7 or higher.
2. Must have bson-4.1.0.jar, mongodb-driver-core-4.1.0.jar, mongodb-driver-sync-4.1.0.jar in your classpath.
3. To run the program, simply compile both UserDetails.java and MongoProject.java, and then run MongoProject.
4. When you run the GUI, got to the top left and click "Connect", this establishes a connection to the MongoDB set up on Atlas (cloud storage).
5. Enter text or enter coordinates in the respective search bars. When searching by location, the format of input is quite particular. It must be ```longitude, latitude```. Do not forget the comma or space between the two numbers.
6. After searching, you can click on any of the results (which are buttons in grid form) and view Tweets.
7. While looking at a specific Tweet, you can leave comments which are then stored in the MongoDB on Atlas and displayed beneath the Tweet.
8. When finished looking at a specific Tweet, you can simply close out the pop up window, and either view more Tweets with the same search results, or clear the search results with the clear button.
9. Once you are done searching, you simply go back to the menu in the top left corner and "Disconnect."
10. Close the program.