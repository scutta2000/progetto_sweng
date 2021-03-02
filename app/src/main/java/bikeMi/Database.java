package bikeMi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.conversions.Bson;

class Database {
    private static Database instance;

    private MongoClient mongoClient;
    private MongoDatabase database;

    private Database() {
        mongoClient = MongoClients.create();
        database = mongoClient.getDatabase("bikeMi");
    };

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void close() {
        mongoClient.close();
        mongoClient = null;
        return;
    }

    private MongoCollection<Document> getUserCollection(){
        return database.getCollection("users");

    }
    /**
     * 
     * @return a new unique id for the users collection
     */
    public int getNewUserId(){
        Document query = new Document("_id", "UNIQUE COUNT USER IDENTIFIER");
        //NB: Anche se gli id generati sono sequenziali non necesariamente verranno usati tutti
        Bson update = Updates.inc("COUNT", 1);
        
        MongoCollection<Document> users = getUserCollection();

        //the use of find and update ensure that the operarion is atomical
        Document d =  users.findOneAndUpdate(query, update); 

        if (d == null){
            Document counter = new Document("_id", "UNIQUE COUNT USER IDENTIFIER")
                .append("COUNT", 1);
            users.insertOne(counter);
            return 0;
        }
        else{
            return d.get("COUNT", Integer.class);
        }
        

    }

    public void saveNewUser(User u) {
        MongoCollection<Document> collection = getUserCollection();
        collection.insertOne(u.asDocument());
    }

}
