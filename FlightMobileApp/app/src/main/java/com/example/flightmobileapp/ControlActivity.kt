package com.example.flightmobileapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.control_layout.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.math.abs

class ControlActivity : AppCompatActivity() {
    private var network = Network()

    private var getTheShot = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if we reached here, this means we have received a screenshot
        val urlItem = intent.getStringExtra("urlItem")
        if (urlItem != null) {
            val checkConnection = network.establishConnection(urlItem, applicationContext)
            if (checkConnection) {
                //initiate connection first still not done though.
                setContentView(R.layout.control_layout)
                val rudderText = findViewById<TextView>(R.id.rudderText)
                val throttleText = findViewById<TextView>(R.id.throttleText)
                val aileronText = findViewById<TextView>(R.id.aileronText)
                val elevatorText = findViewById<TextView>(R.id.elevatorText)
                rudderText.text = "0.0"
                throttleText.text = "0.0"
                aileronText.text = "0.0"
                elevatorText.text = "0.0"
                setJoystickListen()
                setSeekBarListen()
                onStart()
            }
        }
    }

    //set the listeners for joystick
    private fun setJoystickListen() {
        val aileronText = findViewById<TextView>(R.id.aileronText)
        val elevatorText = findViewById<TextView>(R.id.elevatorText)
        var aileronFlag : Boolean
        var elevatorFlag : Boolean
        controllerView.setOnMoveListener { angle, strength ->
            val aileronSet = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevatorSet = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            //update the text
            aileronText.text = aileronSet.toString()
            elevatorText.text = elevatorSet.toString()
            //check 1% change in the values
            aileronFlag = checkChange(aileronSet,  network.getAileron())
            elevatorFlag = checkChange(elevatorSet, network.getElevator())

            if (aileronFlag) {
                network.setAileron(aileronSet)
            }
            if (elevatorFlag) {
                network.setElevator(elevatorSet)
            }
            if (aileronFlag || elevatorFlag) {
                CoroutineScope(IO).launch {  network.sendControlValues() }
            }
        }
    }

    /**
     * A function where we set the listeners for the throttle Seekbar and the rudder variant seekbar
     * here we call the function resposible for updating the value when a change has happened
     */
    private fun setSeekBarListen() {
        val rudderText = findViewById<TextView>(R.id.rudderText)
        val throttleText = findViewById<TextView>(R.id.throttleText)
        val rudderSeekBar = findViewById<SeekBar>(R.id.rudderSeek)
        rudderSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progressChanged = p1.toDouble() / 100
                rudderText.text = progressChanged.toString()
                network.setRudder(progressChanged)

                CoroutineScope(IO).launch {  network.sendControlValues() }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        // add throtleseekbar listeners
        val throttleSeekBar = findViewById<SeekBar>(R.id.throttleSeek)
        throttleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progressChanged = p1.toDouble() / 100
                throttleText.text = progressChanged.toString()
                network.setThrottle(progressChanged)

                CoroutineScope(IO).launch {  network.sendControlValues() }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    /**
     * Function to check whether a change of 1% has happened in the values since last
     * time the value was updated
     */
    private fun checkChange(givenValue: Double, previousValue: Double): Boolean {
        if ((abs(givenValue - previousValue) > 0.01 )) {
            return true
        }
        return false
    }

    /**
     * As long as we are in control page, keep requesting a screenshot of.
     */
    private fun loopScreenshot() {
        val imageView = findViewById<ImageView>(R.id.imageView)
        CoroutineScope(IO).launch {
            while (getTheShot) {
                network.getScreenshot(imageView)
                delay(700)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getTheShot = true
        loopScreenshot()
    }

    override fun onResume() {
        super.onResume()
        getTheShot = true
    }

    override fun onPause() {
        super.onPause()
        getTheShot = false
    }

    override fun onStop() {
        super.onStop()
        getTheShot = false
    }

    override fun onRestart() {
        super.onRestart()
        getTheShot = true
    }

    override fun onDestroy() {
        super.onDestroy()
        getTheShot = false
    }
}