package com.monisha.samples.sloshed.models;

import com.monisha.samples.sloshed.util.DrinkEnum;

/**
 * Created by Monisha on 3/10/2018.
 * Reference for standard drink size:
 * https://www.niaaa.nih.gov/alcohol-health/overview-alcohol-consumption/what-standard-drink
 * https://www.rethinkingdrinking.niaaa.nih.gov/How-much-is-too-much/What-counts-as-a-drink/How-Many-Drinks-Are-In-Common-Containers.aspx
 **/

public class Drink {
    private DrinkEnum drinkType = null;
    private float alcoholPercentage = 0;
    private float quantity = 0;
    private float drinkCount = 0;

    public DrinkEnum getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(DrinkEnum drinkType) {
        this.drinkType = drinkType;
    }

    public float getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(float alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setDrinkCount(float drinkCount) {
        this.drinkCount = drinkCount;
    }

    public float getDrinkCount() {
        if (drinkCount == 0) {
            computeDrinkCount();
        }
        return drinkCount;
    }

    public void computeDrinkCount() {
        //Regular Beer
        if (alcoholPercentage <= 5) {
            if (quantity <= 12) {
                drinkCount = 1;
            } else if (quantity <= 16) {
                drinkCount = (float) 1.33;
            } else if (quantity <= 22) {
                drinkCount = 2;
            } else if (quantity <= 40) {
                drinkCount = (float) 3.33;
            }
        }
        //Malt Beer
        else if (alcoholPercentage <= 7) {
            if (quantity <= 12) {
                drinkCount = (float) 1.5;
            } else if (quantity <= 16) {
                drinkCount = 2;
            } else if (quantity <= 22) {
                drinkCount = (float) 2.5;
            } else if (quantity <= 40) {
                drinkCount = (float) 4.5;
            }
        }
        //Table Wine
        else if (alcoholPercentage <= 12) {
            if (quantity <= 5) {
                drinkCount = 1;
            } else if (quantity <= 25.36) { //one whole bottle, 750 ml
                drinkCount = 5;
            }
        }
        //Distilled spirits
        else if (alcoholPercentage <= 40) {
            if (quantity <= 1.5) {
                drinkCount = 1;
            } else if (quantity <= 6.76) {//Half pint, 200ml
                drinkCount = (float) 4.5;
            } else if (quantity <= 12.68) {//a pint or half bottle, 375ml
                drinkCount = (float) 8.5;
            } else if (quantity <= 25.36) {//a fifth, 750ml
                drinkCount = 17;
            }
        }
    }
}
