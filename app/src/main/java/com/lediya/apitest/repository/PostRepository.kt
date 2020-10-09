package com.lediya.apitest.repository

import com.lediya.apitest.data.Character
import com.lediya.apitest.data.dao.PostDao
import com.lediya.apitest.model.Posts

class PostRepository(private var postDao: PostDao) {
    /**
     * insert the post list data in the database */
    suspend fun insertPostData(posts: List<Posts>) {
        return postDao.insertAllPost(posts)
    }
    /**
     * get the post list data in the database */
    suspend fun getPostData(): List<Posts>{
        return postDao.getAllPost()
    }
    /**
     * insert the user character length in the database */
    suspend fun insertContentLength(character: Character){
        return postDao.insertContentLength(character)
    }
    /**
     * get the user character length in the database */
    suspend fun getContentLengthData(): Character{
        return postDao.getContentLength()
    }
}