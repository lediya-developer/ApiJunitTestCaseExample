package com.lediya.apitest

import android.content.Context
import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.lediya.apitest.communication.ApiEndPointService
import com.lediya.apitest.communication.RestClient
import com.lediya.apitest.data.AppDatabase
import com.lediya.apitest.data.dao.PostDao
import com.lediya.apitest.model.Posts
import com.lediya.apitest.utility.Utils
import com.lediya.apitest.view.ListScreen
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response
import java.io.IOException
import java.lang.AssertionError

/**
 * Instrumented test, which will execute on an Android device.
 *
 *
 */
@RunWith(AndroidJUnit4::class)
class PostDataInstrumentedTest {
    private lateinit var postDao: PostDao
    private lateinit var db: AppDatabase
    /**
     * Create the db for database operation */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        postDao = db.postDao
    }
    /**
     * Close the db after the database operation test case*/
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
    /**
     * check internet is on and downlaod api data test case*/
    @Test
    fun internetOnTest() {
        try {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val activity = ActivityTestRule(ListScreen::class.java)
            activity.launchActivity(Intent())
            assertEquals("com.lediya.inmobi", appContext.packageName)
            onView(withId(R.id.button)).perform((click()))
            assertTrue(Utils.isConnectedToNetwork(appContext))
            downloadApiData()
        }
        catch (e:AssertionError){

        }
    }
    /**
     * check internet is off and shows the warning alert test case */
    @Test
    fun internetOffTest() {
        try{
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            val activity = ActivityTestRule(ListScreen::class.java)
        activity.launchActivity(Intent())
        assertEquals("com.lediya.inmobi", appContext.packageName)
        onView(withId(R.id.button)).perform((click()))
        assertFalse(Utils.isConnectedToNetwork(appContext))
        onView(withId(R.id.noInternetAvailable)).check(ViewAssertions.matches(withText("Internet not available")))
        }
        catch (e:AssertionError){

        }
    }
    /**
     * check write and retrieve the data in the database test case*/
    @Test
    @Throws(Exception::class)
    fun writeAndRetrieveData() {
        val modelData = listOf(Posts("1", "12", "Test", "Test Description"))
        runBlocking { postDao.insertAllPost(modelData) }
        val afterRetrievePostData = runBlocking { postDao.getAllPost() }
        assertEquals(modelData, afterRetrievePostData)
    }
    /**
     * Download api data test case*/
    @Test
    fun downloadApiData() {
        try {
            runBlocking {
                val response: Response<List<Posts>> =
                    RestClient.getInstance(ApiEndPointService::class.java).getAllPost()
                val postResponse: List<Posts>? = response.body()
               assertEquals(
                   postResponse?.get(0)?.userId!=null&& postResponse[0].userId.isNotBlank(),
                true)
                assertEquals(
                    postResponse?.get(0)?.id!=null&& postResponse[0].id.isNotBlank(),
                    true)
                assertEquals(
                    postResponse?.get(0)?.title!=null&& postResponse[0].title.isNotBlank(),
                    true)
                assertEquals(
                    postResponse?.get(0)?.body!=null&& postResponse[0].body.isNotBlank(),
                    true)
            }
        }
        catch (exception: IOException) {
       }
    }
}
