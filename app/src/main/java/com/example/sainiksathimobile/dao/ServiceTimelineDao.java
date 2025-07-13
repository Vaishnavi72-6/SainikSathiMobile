package com.example.sainiksathimobile.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sainiksathimobile.model.ServiceTimelineRecord;

import java.util.List;

@Dao
public interface ServiceTimelineDao {

    @Insert
    void insertAll(List<ServiceTimelineRecord> records);

    @Query("SELECT * FROM ServiceTimelineRecord WHERE userId = :userId")
    List<ServiceTimelineRecord> getTimelineForUser(int userId);
}
