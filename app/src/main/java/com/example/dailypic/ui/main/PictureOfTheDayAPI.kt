package com.example.dailypic.ui.main

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>
    @GET("planetary/apod")
    fun getEarthPicture(@Query("api_key") apiKey:String):Call<PODServerResponseData>

}