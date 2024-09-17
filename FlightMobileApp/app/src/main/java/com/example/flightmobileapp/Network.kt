package com.example.flightmobileapp

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Network : AppCompatActivity(){
    private lateinit var context : Context
    private var aileron: Double = 0.0
    private var rudder: Double = 0.0
    private var elevator: Double = 0.0
    private var throttle: Double = 0.0

    private lateinit var urlItem : URL

    //setters and getters
    fun setAileron(newVal : Double) {
        this.aileron = newVal
    }

    fun setRudder(newVal : Double) {
        this.rudder = newVal
    }

    fun setElevator(newVal : Double) {
        this.elevator = newVal
    }

    fun setThrottle(newVal : Double) {
        this.throttle = newVal
    }

    fun getAileron() : Double{
        return this.aileron
    }

    fun getElevator() : Double{
        return this.elevator
    }

    /**
     * Function where we test the URLs
     */
    fun establishConnection(urlItem: String, context: Context): Boolean {
        this.context = context
        val url : URL
        return try {
            url = URL("$urlItem/")
            url.openConnection() as HttpURLConnection
            this.urlItem = url
            true
        } catch (e : IOException) {
            false
        }
    }

    /**
     * Send the updated values to the server, in the case of failure, we display the appropriate message back to the user.
     */
    fun sendControlValues() {
        val jsonData: String = "{\"aileron\": $aileron, \n \"rudder\": $rudder, \n \"elevator\" : $elevator, \n" +
                " \"throttle\" : $throttle}"
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonData)
        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit
            .Builder()
            .baseUrl(urlItem)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder)).build()
        val apiService = retrofit
            .create(ApiService::class.java)
        apiService.post(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = Toast.makeText(context, "Did not receive a response from server, we suggest you move back to Home Screen", Toast.LENGTH_SHORT) //here is my problem, if we dont send values the server will send error
                message.show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.code() == 422) {
                        val message = Toast.makeText(context, "Values could not be processed.", Toast.LENGTH_SHORT)
                        message.show()
                    } else if (response.code() == 500) {
                        val message = Toast.makeText(context, "Internal server issue, please move back to home screen!", Toast.LENGTH_LONG)
                        message.show()
                    }
                }
                catch (e: IOException) {
                    val message = Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                    message.show()
                }
            }
        })
    }

    /**
     * Request a screenshot from the server
     */
    fun getScreenshot(theImageView: ImageView) {
        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit
            .Builder()
            .baseUrl(urlItem)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.get().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = Toast.makeText(context, "Screenshot could not be loaded!, Connection with server was lost, please move back to home screen!", Toast.LENGTH_SHORT)
                message.show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val inputStream = response.body()?.byteStream()
                        val bmp = BitmapFactory.decodeStream(inputStream)
                        runOnUiThread {
                            theImageView.setImageBitmap(bmp)
                        }
                    }
                }
                catch (e : IOException) {
                    val message = Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                    message.show()
                }
            }

        })
    }

    //function will call checkFirstShot.
    //then call a function from there with a string , if string is 200, then Ok

    fun checkFirstShot(resultHandler: (String) -> Unit){
        val gsonBuilder = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(urlItem)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.get().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = Toast.makeText(
                    context,
                    "Could not establish a successful connection! try again",
                    Toast.LENGTH_SHORT
                )
                message.show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val ch = response.code().toString()
                resultHandler(gsonBuilder.fromJson(ch, String::class.java))
            }
        })
    }
}