package com.monisha.samples.sloshed.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.monisha.samples.sloshed.dbmodels.DrinkDB;

import java.util.List;

/**
 * Created by Monisha on 3/25/2018.
 */
@Dao
public interface DrinkDAO {
    @Query("SELECT * FROM drinkdb")
    List<DrinkDB> getAll();

    @Query("SELECT * FROM drinkdb WHERE timestamp>=:startTime AND timestamp<=:endTime")
    List<DrinkDB> getForStartEnd(long startTime, long endTime);

    @Query("SELECT * FROM drinkdb WHERE day=:day")
    List<DrinkDB> getForThisDay(String day);

    @Insert
    void insertAll(DrinkDB... drinks);

    @Update
    void update(DrinkDB... drinks);

    @Delete
    void delete(DrinkDB drink);

}
