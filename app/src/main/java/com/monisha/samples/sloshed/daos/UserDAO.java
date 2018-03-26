package com.monisha.samples.sloshed.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.monisha.samples.sloshed.dbmodels.UserDB;

import java.util.List;

/**
 * Created by Monisha on 3/25/2018.
 */
@Dao
public interface UserDAO {
    @Query("SELECT * FROM userdb")
    List<UserDB> getAll();

    @Insert
    void insertAll(UserDB... users);

    @Update
    void update(UserDB... users);

    @Delete
    void delete(UserDB user);
}
