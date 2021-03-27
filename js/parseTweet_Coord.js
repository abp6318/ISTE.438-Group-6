conn = new Mongo("mongodb+srv://abp6318:group6password@cluster0.bgnbf.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
db = conn.getDB("MongoProject");
collection = db.getCollection("Tweets");

collection.find().forEach(function (e){
    if("tweet_coord" in e && e.tweet_coord !== ""){
        var stringCoord = e.tweet_coord;
        var substringCoord = stringCoord.substring(1, stringCoord.length-1);
        var splitCoord = substringCoord.split(", ");
        var lat = parseFloat(splitCoord[0]);
        var long = parseFloat(splitCoord[1]);

        var ll = {longitude : long, latitude: lat};
        // printjson(ll);
        var lla =[]; //an array
        //fills the array with longitude and latitude
        Object.keys(ll).forEach(function(key) {
            var val = ll[key];
            lla.push(val);
        })
        // print(lla);
        var p = "Point";
        // // Create location variable in document - see how this compares with the slides
        e.loc = {type: p, coordinates: lla};       
        printjson(e);  
        
        // // save the updated document
        collection.save(e);
    }
})