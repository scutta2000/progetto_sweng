package bikeMi;

import java.sql.Timestamp;

import org.bson.Document;


public class User {
    private int id;

    private String password;

    private int strikes = 0;

    private SubscriptionType subscription;

    private Timestamp subscriptionStart;

    private Timestamp lastUsed;

    protected User(int id, String password, int strikes, SubscriptionType subscription, Timestamp subscriptionStart, Timestamp lastUsed){
        this.id = id;
        this.password = password;
        this.strikes = strikes;
        this.subscription = subscription;
        this.subscriptionStart = subscriptionStart;
        this.lastUsed = lastUsed;
    }

    public int getId(){ return id;}
    
    //TODO: hash
    public boolean checkPassword(String pass){
        return  pass.equals(password);
    }

    public int returnStrikes(){ return strikes;}
    public void addStrike(){
        strikes++;
    }

    //forse ha senso passare la bici e salvare nell'user la bici che sta usando
    // public void useBike(){
    //     Date date = new Date();
    //     lastUsed = new Timestamp(date.getTime());

    // }

    protected Document asDocument(){
        Document d = new Document("_id", id)
            .append("password", password)
            .append("strikes", strikes)
            .append("subscription", subscription)
            .append("subscriptionStart", subscriptionStart)
            .append("lastUsed", lastUsed);

        return d;
    }

    public void save(){
        Database.getInstance().saveNewUser(this);
    }

    @Override
    public String toString() {
        return String.format("{\n"
            +"\tid = %d,\n"
            +"\tpassword  = %s,\n"
            +"\tstrikes = %d,\n"
            +"\tsubscription = %s,\n"
            +"\tsubscriptionStart = %s,\n"
            +"\tlastUsed = %s,\n"
        +"}\n",
        id, password, strikes, subscription, subscriptionStart, lastUsed);
    }

    
}
