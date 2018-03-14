package com.monisha.samples.sloshed.models;

/**
 * Created by Monisha on 3/10/2018.
 */

public class Drink {
    public String drinkName;
    public float alcoholPercentage;
    public float quantity;

    public Drink(String drinkName, float alcoholPercentage, float quantity) {
        this.drinkName = drinkName;
        this.alcoholPercentage = alcoholPercentage;
        this.quantity = quantity;
    }
}
