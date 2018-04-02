package com.monisha.samples.sloshed.dbmodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.monisha.samples.sloshed.util.TimeConverter;

import java.util.Date;


/**
 * Created by Monisha on 3/25/2018.
 */

@Entity
public class DrinkDB
{
    public DrinkDB(@NonNull int session, @NonNull Date timestamp, float drinkCount, Date start_time, Date end_time, float bac)
    {
        this.session = session;
        this.timestamp = timestamp;
        this.drinkCount = drinkCount;
        this.start_time = start_time;
        this.end_time = end_time;
        this.bac = bac;
    }

    @PrimaryKey
    @NonNull
    public int session;

    @NonNull
    @TypeConverters({TimeConverter.class})
    public Date timestamp;

    @ColumnInfo(name = "drink_count")
    public float drinkCount;

    @ColumnInfo(name = "start_time")
    @TypeConverters({TimeConverter.class})
    public Date start_time;

    @ColumnInfo(name = "end_time")
    @TypeConverters({TimeConverter.class})
    public Date end_time;

    @ColumnInfo(name = "BAC")
    public float bac;

    @NonNull
    public int getSession() {
        return session;
    }

    public void setSession(@NonNull int session) {
        this.session = session;
    }

    @NonNull
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getDrinkCount() {
        return drinkCount;
    }

    public void setDrinkCount(float drinkCount) {
        this.drinkCount = drinkCount;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public float getBac() {
        return bac;
    }

    public void setBac(float bac) {
        this.bac = bac;
    }
}
