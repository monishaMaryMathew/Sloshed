package com.monisha.samples.sloshed.dbmodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Monisha on 3/25/2018.
 */

@Entity
public class EmergencyContactDB {
    @ColumnInfo(name = "contact_name")
    public String contactName;

    @PrimaryKey
    @NonNull
    public String phoneNumber;

    public EmergencyContactDB(String contactName, String phoneNumber) {
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

