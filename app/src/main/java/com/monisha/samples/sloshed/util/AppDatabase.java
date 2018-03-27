package com.monisha.samples.sloshed.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.monisha.samples.sloshed.daos.BlockedContactDAO;
import com.monisha.samples.sloshed.daos.DrinkDAO;
import com.monisha.samples.sloshed.daos.EmergencyContactDAO;
import com.monisha.samples.sloshed.daos.UserDAO;
import com.monisha.samples.sloshed.dbmodels.BlockedContactDB;
import com.monisha.samples.sloshed.dbmodels.DrinkDB;
import com.monisha.samples.sloshed.dbmodels.EmergencyContactDB;
import com.monisha.samples.sloshed.dbmodels.UserDB;

/**
 * Created by Monisha on 3/25/2018.
 */


@Database(entities = {BlockedContactDB.class, EmergencyContactDB.class, UserDB.class, DrinkDB.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "notsloshed-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            //.allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract BlockedContactDAO blockedContactDAO();

    public abstract EmergencyContactDAO emergencyContactDAO();

    public abstract UserDAO userDAO();

    public abstract DrinkDAO drinkDAO();
}
