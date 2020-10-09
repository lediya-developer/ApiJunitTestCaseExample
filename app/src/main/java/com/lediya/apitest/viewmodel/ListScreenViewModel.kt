package com.lediya.apitest.viewmodel

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lediya.apitest.communication.ApiEndPointService
import com.lediya.apitest.communication.RestClient
import com.lediya.apitest.data.AppDatabase
import com.lediya.apitest.data.Character
import com.lediya.apitest.model.Posts
import com.lediya.apitest.repository.PostRepository
import com.lediya.apitest.utility.Event
import com.lediya.apitest.utility.ResultImp
import com.lediya.apitest.utility.ResultType
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ListScreenViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repository: PostRepository
    val postList: LiveData<Event<List<Posts>>>
        get() = _postsList
    private var _postsList = MutableLiveData<Event<List<Posts>>>()
    private val _fetchResult = MutableLiveData<Event<ResultImp>>()
    val fetchResult: LiveData<Event<ResultImp>>
        get() = _fetchResult
    private val _totalTimeResult = MutableLiveData<Event<Long>>()
    val totalTimeResult: LiveData<Event<Long>>
        get() = _totalTimeResult
    private val _totalCharacterLength = MutableLiveData<Event<Character>>()
    val totalCharacterLength: LiveData<Event<Character>>
        get() = _totalCharacterLength
    private var  start:Long = 0
    init {
        val db = AppDatabase.getDatabase(application)
        if (db != null) {
            repository = PostRepository(db.postDao)
        }
    }
    /**
     * Fetch the post data from database
     */
    fun fetchPostDataFromDatabase()= viewModelScope.launch {
        _postsList.postValue((Event(repository.getPostData())))
        _totalCharacterLength.postValue(Event(repository.getContentLengthData()))

        if(start!=0L) {
            val endTime = SystemClock.elapsedRealtime()
            val elapsedMilliSeconds: Long = endTime - start
            val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMilliSeconds)
            _totalTimeResult.postValue(Event(seconds))
        }
    }
    /**
     * Get the post data from server
     */
    fun getPostDataFromServer() = viewModelScope.launch {
        start = SystemClock.elapsedRealtime()
        _fetchResult.postValue(Event(ResultImp(ResultType.PENDING)))
        try {
            val response = RestClient.getInstance(ApiEndPointService::class.java).getAllPost()
            response.body()?.let { insertUpdatedPostData(it) }
        } catch (e:Exception) {
            _fetchResult.postValue(Event(ResultImp(ResultType.FAILURE)))
        }
    }
    /**
     * Get the user data from server
     */
    fun getUserDataFromServer() = viewModelScope.launch {
         val totalCharacterLength = RestClient.getInstance(ApiEndPointService::class.java).getAllUser().body()?.bytes()?.size
         insertUpdateCharacterLength(Character(0,totalCharacterLength.toString()))

    }
    /**
     * Insert the post data to the database
     */
    private fun insertUpdatedPostData(post:List<Posts>){
        viewModelScope.launch {
            repository.insertPostData(post)
            _fetchResult.postValue(Event(ResultImp(ResultType.SUCCESS)))
        }
    }
    /**
     * Insert the user character length to the database
     */
    private fun insertUpdateCharacterLength(charsLength:Character){
        viewModelScope.launch {
            repository.insertContentLength(charsLength)
            _fetchResult.postValue(Event(ResultImp(ResultType.SUCCESS)))
        }
    }
}

