package bikeMi;

import java.sql.Timestamp;
import java.util.Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

public class UserBuilder {
    private int id = 0;
    private String password = null;
    private int strikes = 0;
    private SubscriptionType subscription = null;
    private Timestamp subscriptionStart = null;
    private Timestamp lastUsed = null;
    

    protected static User fromDocument(Document d){
        int id = d.get("_id", int.class);
        String password = d.get("password", String.class);
        int strikes = d.get("strikes", int.class);
        SubscriptionType subscription = d.get("subscription", SubscriptionType.class);
        Timestamp subscriptionStart = d.get("subscriptionStart", Timestamp.class);
        Timestamp lastUsed = d.get("lastUsed", Timestamp.class);
        

        return new User(id, password, strikes, subscription, subscriptionStart, lastUsed);
    }

    public int setPassword(String s){
        password = s; //TODO:hash
        id = Database.getInstance().getNewUserId();
        return id;
    }

    public void setSubscription(SubscriptionType s){
        subscription = s;
        if (s == SubscriptionType.YEARLY){
            subscriptionStart = new Timestamp(System.currentTimeMillis());
            
        }
    }

    public User build(){
        Objects.requireNonNull(id, "Id and password need to be set, use .setPassword()");
        Objects.requireNonNull(password, "Id and password need to be set, use .setPassword()");
        Objects.requireNonNull(subscription, "Subscription needs to be set, use .setSubscription()");

        User u = new User(id, password, strikes, subscription, subscriptionStart, lastUsed);

        return u;
    }

    




}
