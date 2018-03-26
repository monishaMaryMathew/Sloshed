package com.monisha.samples.sloshed.dbmodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Monisha on 3/25/2018.
 */
@Entity
public class UserDB {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "weight")
    public float weight;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "is_bac_allowed")
    public int isBacAllowed;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsBacAllowed() {
        return isBacAllowed;
    }

    public void setIsBacAllowed(int isBacAllowed) {
        this.isBacAllowed = isBacAllowed;
    }
}
