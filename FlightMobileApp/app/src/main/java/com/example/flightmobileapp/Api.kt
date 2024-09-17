package com.example.flightmobileapp

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.Call
import okhttp3.ResponseBody

// api interface
interface ApiService {
    // post function for sending commands
    @POST("/api/command")
    fun post(@Body requestBody: RequestBody): Call<ResponseBody>

    //get function to request screenshots
    @GET ("/screenshot")
    fun get() : Call<ResponseBody>
}
