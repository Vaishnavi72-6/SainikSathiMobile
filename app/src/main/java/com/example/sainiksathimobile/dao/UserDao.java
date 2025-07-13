package com.example.sainiksathimobile.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sainiksathimobile.model.User;

@Dao
public interface UserDao {

    // Insert a new user
    @Insert
    void insertUser(User user);

    // Get user by email (for login)
    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    // Get user by ID (if needed for Dashboard or PDF)
    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    User getUserById(int id);
}
