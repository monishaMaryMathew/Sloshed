package com.monisha.samples.sloshed.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.monisha.samples.sloshed.dbmodels.DrinkDB;
import com.monisha.samples.sloshed.util.TimeConverter;

import java.util.Date;
import java.util.List;

/**
 * Created by Monisha on 3/25/2018.
 */
@Dao
public interface DrinkDAO {
    @Query("SELECT * FROM drinkdb")
    List<DrinkDB> getAll();

    @Query("SELECT * FROM drinkdb WHERE start_time>=:startTime AND end_time<=:endTime")
    @TypeConverters({TimeConverter.class})
    List<DrinkDB> getForStartEnd(Date startTime, Date endTime);

    @Query("SELECT * FROM drinkdb WHERE timestamp=:day")
    @TypeConverters({TimeConverter.class})
    List<DrinkDB> getForThisSession(Date day);

    @Insert
    void insertAll(DrinkDB... drinks);

    @Update
    void update(DrinkDB... drinks);

    @Delete
    void delete(DrinkDB drink);

}
