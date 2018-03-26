package com.monisha.samples.sloshed.dbmodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Monisha on 3/25/2018.
 */

@Entity
public class DrinkDB {
    @PrimaryKey
    @NonNull
    public long timestamp;

    @ColumnInfo(name = "drink_type")
    public String drinkType;

    @ColumnInfo(name = "alcohol_percentage")
    public float alcoholPercentage;

    @ColumnInfo(name = "quantity")
    public float quantity;

    @ColumnInfo(name = "drink_count")
    public float drinkCount;

    @ColumnInfo(name = "day")
    public String day;

    @ColumnInfo(name = "day_time")
    public String dayTime;

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(String drinkType) {
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

    public float getDrinkCount() {
        return drinkCount;
    }

    public void setDrinkCount(float drinkCount) {
        this.drinkCount = drinkCount;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }
}
