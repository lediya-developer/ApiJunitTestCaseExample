package com.lediya.apitest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lediya.apitest.data.dao.PostDao
import com.lediya.apitest.model.Posts

@Database(entities = [Posts::class, Character::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val postDao: PostDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
/**
 * create instance to get the database*/
        fun getDatabase(context: Context): AppDatabase? {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = buildDatabase(context)
                }
                return INSTANCE
            }
        }
        /**
         * create builder using room database */
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Post_database"
            ).build()
        }
    }

}