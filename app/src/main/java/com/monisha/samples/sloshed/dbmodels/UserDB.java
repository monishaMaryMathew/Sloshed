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

    @ColumnInfo(name = "bac_threshold")
    public float bacThreshold;

    @ColumnInfo(name = "blocked_for_hour")
    public int blockedForHour;

    @ColumnInfo(name = "address_line_1")
    public String addressLine1;

    @ColumnInfo(name = "address_line_2")
    public String addressLine2;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "zipcode")
    public String zipCode;

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

    public float getBacThreshold() {
        return bacThreshold;
    }

    public void setBacThreshold(float bacThreshold) {
        this.bacThreshold = bacThreshold;
    }

    public int getBlockedForHour() {
        return blockedForHour;
    }

    public void setBlockedForHour(int blockedForHour) {
        this.blockedForHour = blockedForHour;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void updateUserObj(UserDB newUser) {
        this.weight = newUser.getWeight();
        this.gender = newUser.getGender();
        this.age = newUser.getAge();
        this.message = newUser.getMessage();
        this.isBacAllowed = newUser.getIsBacAllowed();
        this.bacThreshold = newUser.getBacThreshold();
        this.blockedForHour = newUser.getBlockedForHour();
        this.addressLine1 = newUser.getAddressLine1();
        this.addressLine2 = newUser.getAddressLine2();
        this.city = newUser.getCity();
        this.zipCode = newUser.getZipCode();
    }
}
