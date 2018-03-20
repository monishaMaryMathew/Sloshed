package com.monisha.samples.sloshed.models;

import com.monisha.samples.sloshed.util.GenderEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monisha on 3/10/2018.
 */

public class User {
    //Profiling details as per the settings screen
    public int weight;
    public int age;
    public GenderEnum genderEnum;
    //Emergency Contact Info
    //check if at least one emergency contact information is available
    public List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();

    //TODO Compute the following
    public int lowLevel = 10;
    public int mediumLevel = 50;

    //TODO keep updating the following information
    public List<Drink> drinksConsumed = new ArrayList<Drink>();
    public float currentPercentage;

    public User() {

    }

    public User(int weight, int age, GenderEnum genderEnum, List<EmergencyContact> emergencyContacts) {
        this.weight = weight;
        this.age = age;
        this.genderEnum = genderEnum;
        this.emergencyContacts = emergencyContacts;
    }

    public void setLevels(int low, int medium) {
        this.lowLevel = low;
        this.mediumLevel = medium;
    }


}
