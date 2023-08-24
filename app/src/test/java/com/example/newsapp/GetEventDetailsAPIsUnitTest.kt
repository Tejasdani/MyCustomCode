package com.example.newsapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.api.ApiService
import com.example.newsapp.db.repository.EventRepository
import com.example.newsapp.ui.dashboard.HomeRepo
import com.example.newsapp.ui.dashboard.HomeViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.*
import org.mockito.ArgumentMatchers.anyString

import org.mockito.kotlin.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/*Created By Tejas Dani on 14/March/2023
* to test API using mock server
* */

class GetEventDetailsAPIsUnitTest {

    /*@get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()*/

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: HomeRepo
    private lateinit var mockedResponse: String
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    @Before
    fun setUp() {

        //Local WebServer were we have already added dependency for this
        mockWebServer = MockWebServer()
        mockWebServer.start()
        var BASE_URL = mockWebServer.url("/").toString()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build().create(ApiService::class.java)

        repository = HomeRepo(service)

    }

    // To test weather response is not null and giving HTTP_OK result
    @Test
    fun testApiOK() = runTest {
        mockedResponse = MockResponseFileReader("dashDetailsApi/success.json").content

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockedResponse)
        )

        val response = repository.apiService.getPersonalDetails()
        assertNotNull(response)
        assertEquals(response.code(), HttpURLConnection.HTTP_OK)
    }

    //To test for testing error with 404
    @Test
    fun testApi_returnError() = runTest {
        // mockedResponse = MockResponseFileReader("personalDetailsApi/success.json").content

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody("Somethings went Wrong")
        )

        val response = repository.apiService.dashboardExtends(anyString())
        assertEquals(false, response.isSuccessful)
        assertEquals(response.code(), HttpURLConnection.HTTP_NOT_FOUND)
    }

    // Test for actual response is return
    @Test
    fun testAPISuccess() = runTest {
        mockedResponse = MockResponseFileReader("dashDetailsApi/success.json").content
        assertNotNull(mockedResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )
        val response = repository.apiService.dashboardExtends(anyString())

        //Todo Callers should use this to verify the request was sent as intended
        // to ensure that response coming from server we are taking that response.
        mockWebServer.takeRequest()// Very important

        val json = gson.toJson(response.body())
        val resultResponse = JsonParser.parseString(json)
        val expectedResponse = JsonParser.parseString(mockedResponse)
        assertNotNull(response)
        assertTrue(resultResponse.equals(expectedResponse))
    }


    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}