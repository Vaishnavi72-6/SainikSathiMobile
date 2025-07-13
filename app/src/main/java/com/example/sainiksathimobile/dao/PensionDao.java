package com.example.sainiksathimobile.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sainiksathimobile.data.model.PensionRecord;

import java.util.List;

@Dao
public interface PensionDao {

    @Insert
    void insertPension(PensionRecord record); // ✅ Make sure NOT nullable

    @Query("SELECT * FROM PensionRecord WHERE userId = :userId")
    List<PensionRecord> getPensionsForUserNow(int userId);
}
