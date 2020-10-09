package com.lediya.apitest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * Post table to store post details in the database*/
@Entity
 data class Posts (
    var userId :String,
    @PrimaryKey var id :String,
    var title:String,
    var body:String
)
