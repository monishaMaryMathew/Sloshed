package com.monisha.samples.sloshed.models;

import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;
import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;
import com.monisha.samples.sloshed.util.GenderEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monisha on 3/10/2018.
 * Reference: Alcohol chart - http://www.brad21.org/bac_charts.html
 */

public class User {
    //Profiling details as per the settings screen
    private int weight = 150;
    private int age = 21;
    private GenderEnum genderEnum = GenderEnum.MALE;
    //Emergency Contact Info
    //check if at least one emergency contact information is available
    private List<EmergencyContactDB> emergencyContacts = new ArrayList<EmergencyContactDB>();
    private List<BlockedContactDB> blockedContacts = new ArrayList<BlockedContactDB>();

    private int blockedForHours;
    private float bacThreshold;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String zipcode;


    //TODO Compute the following
    private int lowLevel;
    private int mediumLevel;

    private int lowLevelCount;
    private int mediumLevelCount;

    //TODO keep updating the following information
    private List<Drink> drinksConsumed = new ArrayList<Drink>();
    private float currentDrinkCount;

    public User() {

    }

    public User(int weight, int age, GenderEnum genderEnum, List<EmergencyContactDB> emergencyContacts, List<BlockedContactDB> blockedContacts) {
        this.weight = weight;
        this.age = age;
        this.genderEnum = genderEnum;
        this.emergencyContacts = emergencyContacts;
        this.blockedContacts = blockedContacts;
    }

    public int getLowLevel() {
        computeLevelCounts();
        computeLevelPercentages();
        return lowLevel;
    }

    public void setLowLevel(int lowLevel) {
        this.lowLevel = lowLevel;
    }
    //compute drink levels
    //driving impaired -
    //danger

    public int getMediumLevel() {
        computeLevelCounts();
        computeLevelPercentages();
        return mediumLevel;
    }

    public void setMediumLevel(int mediumLevel) {
        this.mediumLevel = mediumLevel;
    }

    private void computeLevelCounts() {
        if (genderEnum == GenderEnum.MALE) {
            //Male
            if (weight <= 100) {
                lowLevelCount = 1;
                mediumLevelCount = 7;
            } else if (weight <= 140) {
                lowLevelCount = 2;
                mediumLevelCount = 9;
            } else if (weight <= 200) {
                lowLevelCount = 3;
                mediumLevelCount = 10;
            } else if (weight <= 240) {
                lowLevelCount = 4;
                mediumLevelCount = 10;
            }

        } else {
            //Female
            if (weight <= 90) {
                lowLevelCount = 1;
                mediumLevelCount = 5;
            } else if (weight <= 100) {
                lowLevelCount = 1;
                mediumLevelCount = 6;
            } else if (weight <= 120) {
                lowLevelCount = 1;
                mediumLevelCount = 7;
            } else if (weight <= 140) {
                lowLevelCount = 2;
                mediumLevelCount = 9;
            } else if (weight <= 180) {
                lowLevelCount = 2;
                mediumLevelCount = 10;
            } else if (weight <= 240) {
                lowLevelCount = 3;
                mediumLevelCount = 10;
            }
        }
    }

    private void computeLevelPercentages() {
        lowLevel = lowLevelCount * 10;
        mediumLevel = mediumLevelCount * 10;
    }

    public int getDrinkPercentage() {
        return (int) (currentDrinkCount * 10);
    }

    public float getDrinkCount() {
        return currentDrinkCount;
    }

    public void addDrink(Drink drink) {
        drinksConsumed.add(drink);
        currentDrinkCount += drink.getDrinkCount();
    }

    public int getBlockedForHours() {
        return blockedForHours;
    }

    public void setBlockedForHours(int blockedForHours) {
        this.blockedForHours = blockedForHours;
    }

    public float getBacThreshold() {
        return bacThreshold;
    }

    public void setBacThreshold(float bacThreshold) {
        this.bacThreshold = bacThreshold;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public GenderEnum getGenderEnum() {
        return genderEnum;
    }

    public void setGenderEnum(GenderEnum genderEnum) {
        this.genderEnum = genderEnum;
    }

    public List<EmergencyContactDB> getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(List<EmergencyContactDB> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public List<BlockedContactDB> getBlockedContacts() {
        return blockedContacts;
    }

    public void setBlockedContacts(List<BlockedContactDB> blockedContacts) {
        this.blockedContacts = blockedContacts;
    }

    public int getLowLevelCount() {
        return lowLevelCount;
    }

    public void setLowLevelCount(int lowLevelCount) {
        this.lowLevelCount = lowLevelCount;
    }

    public int getMediumLevelCount() {
        return mediumLevelCount;
    }

    public void setMediumLevelCount(int mediumLevelCount) {
        this.mediumLevelCount = mediumLevelCount;
    }

    public List<Drink> getDrinksConsumed() {
        return drinksConsumed;
    }

    public void setDrinksConsumed(List<Drink> drinksConsumed) {
        this.drinksConsumed = drinksConsumed;
    }

    public float getCurrentDrinkCount() {
        return currentDrinkCount;
    }

    public void setCurrentDrinkCount(float currentDrinkCount) {
        this.currentDrinkCount = currentDrinkCount;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
