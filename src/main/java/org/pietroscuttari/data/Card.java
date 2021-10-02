package org.pietroscuttari.data;

import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "card")
public class Card {
    public Card() {
    }

    public Card(String code, String cvv, Date expirDate) {
        this.code = code;
        this.cvv = cvv;
        this.expireDate = expirDate;
    }

    // @ invariant code.length = 16
    @DatabaseField(id = true)
    private String code;

    // @ ensures code.length == 16
    public String getCode() {
        return code;
    }

    @DatabaseField
    private String cvv;

    public String getCvv() {
        return cvv;
    }

    @DatabaseField
    private Date expireDate;

    public Date getExpireDate() {
        return expireDate;
    }

    @ForeignCollectionField
    private ForeignCollection<Account> accounts;

    public ForeignCollection<Account> getAccounts() {
        return accounts;
    }
}
