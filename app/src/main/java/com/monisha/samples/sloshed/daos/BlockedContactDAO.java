package com.monisha.samples.sloshed.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;

import java.util.List;

/**
 * Created by Monisha on 3/25/2018.
 */
@Dao
public interface BlockedContactDAO {
    @Query("SELECT * FROM blockedcontactdb")
    List<BlockedContactDB> getAll();

    @Query("SELECT * FROM blockedcontactdb WHERE phoneNumber IN (:phoneNumbers)")
    List<BlockedContactDB> loadAllByPhoneNumbers(int[] phoneNumbers);

    @Query("SELECT * FROM blockedcontactdb WHERE contact_name LIKE :contactname AND "
            + "phoneNumber LIKE :phonenumber ")
    List<BlockedContactDB> findByNameAndNumber(String contactname, String phonenumber);

    @Query("SELECT * FROM blockedcontactdb WHERE contact_name LIKE :contactname ")
    List<BlockedContactDB> findByName(String contactname);

    @Query("SELECT * FROM blockedcontactdb WHERE "
            + "phoneNumber LIKE :phonenumber ")
    List<BlockedContactDB> findByNumber(String phonenumber);

    @Insert
    void insertAll(BlockedContactDB... blockedcontacts);

    @Update
    void update(BlockedContactDB... blockedcontacts);

    @Delete
    void delete(BlockedContactDB blockedcontact);
}
