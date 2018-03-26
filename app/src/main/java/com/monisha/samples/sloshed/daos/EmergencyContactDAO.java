package com.monisha.samples.sloshed.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;

import java.util.List;

/**
 * Created by Monisha on 3/25/2018.
 */
@Dao
public interface EmergencyContactDAO {
    @Query("SELECT * FROM emergencycontactdb")
    List<EmergencyContactDB> getAll();

    @Query("SELECT * FROM emergencycontactdb WHERE phoneNumber IN (:phoneNumbers)")
    List<EmergencyContactDB> loadAllByPhoneNumbers(int[] phoneNumbers);

    @Query("SELECT * FROM emergencycontactdb WHERE contact_name LIKE :contactname AND "
            + "phoneNumber LIKE :phonenumber ")
    List<EmergencyContactDB> findByNameAndNumber(String contactname, String phonenumber);

    @Query("SELECT * FROM emergencycontactdb WHERE contact_name LIKE :contactname ")
    List<EmergencyContactDB> findByName(String contactname);

    @Query("SELECT * FROM emergencycontactdb WHERE "
            + "phoneNumber LIKE :phonenumber ")
    List<EmergencyContactDB> findByNumber(String phonenumber);

    @Insert
    void insertAll(EmergencyContactDB... emergencycontacts);

    @Update
    void update(EmergencyContactDB... emergencycontacts);

    @Delete
    void delete(EmergencyContactDB emergencycontact);
}
