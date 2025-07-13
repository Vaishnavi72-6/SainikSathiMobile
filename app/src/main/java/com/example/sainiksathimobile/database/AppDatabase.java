package com.example.sainiksathimobile.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.sainiksathimobile.model.ServiceTimelineRecord;
import com.example.sainiksathimobile.dao.ServiceTimelineDao;

import com.example.sainiksathimobile.dao.PensionDao;
import com.example.sainiksathimobile.dao.ServiceTimelineDao;
import com.example.sainiksathimobile.dao.UserDao;
import com.example.sainiksathimobile.data.model.PensionRecord;
import com.example.sainiksathimobile.data.model.ServiceTimeline;
import com.example.sainiksathimobile.model.User;

@Database(entities = {
        User.class,
        PensionRecord.class,
        ServiceTimelineRecord.class
}, version = 5) // ✅ make sure version is incremented after adding new entities
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract ServiceTimelineDao serviceTimelineDao();

    public abstract UserDao userDao();
    public abstract PensionDao pensionDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "sainik_sathi_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
