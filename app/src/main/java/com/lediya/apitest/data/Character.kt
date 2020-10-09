package com.lediya.apitest.data

import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * Character table to store contentLength in the database*/
@Entity
data class Character(
    @PrimaryKey(autoGenerate = true)
    var localId: Long = 0,
    val contentLength:String?
)