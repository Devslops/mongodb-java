import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Mongo {
    public static void main( String args[] ){
        try{
            //MONGO CONNECTION STRING
            ConnectionString connString = new ConnectionString(
                    "mongodb://dbadmin:root@localhost:27017/db1"
            );

            //MONGO SETTINGS FIELDS
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .retryWrites(true)
                    .build();

            //CREATE CONNECTION
            MongoClient mongoClient = MongoClients.create(settings);

            //GET DATABASE
            MongoDatabase database = mongoClient.getDatabase("db1");

            //GET COLLECTION
            MongoCollection collection = database.getCollection("resto");

            //FETCH ALL
            FindIterable<Document> documents = collection.find();
            for(Document doc : documents) {
                System.out.println(doc);
            }

            //CREATE ONE
            Document restaurant = new Document("_id", new ObjectId());
            restaurant.append("borough", "Lyon");

            collection.insertOne(restaurant);

            //FETCH ONE 1
            Document bestRestaurant1 = (Document) collection.find(new Document("borough", "Lyon")).first();
            System.out.println("Best restaurant 1: " + bestRestaurant1);

            //UPDATE ONE
            BasicDBObject query = new BasicDBObject();
            query.put("borough", "Lyon");

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("borough", "Paris");

            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);

            collection.updateOne(query, updateObject);

            //FETCH ONE 2
            Document bestRestaurant2 = (Document) collection.find(new Document("borough", "Paris")).first();
            System.out.println("Best restaurant 2: " + bestRestaurant2);

        } catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
