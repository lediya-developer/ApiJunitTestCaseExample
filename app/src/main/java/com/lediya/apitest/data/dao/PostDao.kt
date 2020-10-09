package com.lediya.apitest.data.dao

import androidx.room.*
import com.lediya.apitest.data.Character
import com.lediya.apitest.model.Posts

@Dao
interface PostDao {
    /**
     * Get the post list in the database*/
    @Query("SELECT * FROM Posts")
    suspend fun getAllPost(): List<Posts>
    /**
     * Store the post list in the database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPost(posts: List<Posts>)
    /**
     * delete the post list in the database*/
    @Delete
    suspend fun deleteAllPost(posts: List<Posts>)
    /**
     * Get the user character length  in the database*/
    @Query("SELECT * FROM Character")
    suspend fun getContentLength(): Character
    /**
     * Insert the user character length in the database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentLength(character:Character)

}