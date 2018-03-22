package com.monisha.samples.sloshed.models;

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
    private List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();

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

    public User(int weight, int age, GenderEnum genderEnum, List<EmergencyContact> emergencyContacts) {
        this.weight = weight;
        this.age = age;
        this.genderEnum = genderEnum;
        this.emergencyContacts = emergencyContacts;
    }

    public int getLowLevel() {
        computeLevelCounts();
        computeLevelPercentages();
        return lowLevel;
    }

    public int getMediumLevel() {
        computeLevelCounts();
        computeLevelPercentages();
        return mediumLevel;
    }
    //compute drink levels
    //driving impaired -
    //danger

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


}
