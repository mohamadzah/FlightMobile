package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var urlViewModel: UrlViewModel
    private var networkRequests = Network()

    /**
     * OnCreate function where we initialize our main page listeners and database
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.urlLayout)
        val adapter = UrlListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
        urlViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setUrls(it) }
        })
        val fab = findViewById<Button>(R.id.connectButton)
        fab.setOnClickListener {
            // here check if / is the last letter, or if space is the last letter , or if empty text
            val urlTextEdit = findViewById<EditText>(R.id.urlInput).text.toString()
            if (urlTextEdit.trim().isNotEmpty()) {
                val url = UrlItem(urlTextEdit)
                urlViewModel.insert(url)
                //we try to see if its a valid URL, we confirm the success of it by getting a screenshot from server
                val checkConnection = networkRequests.establishConnection(urlTextEdit, applicationContext)
                if (checkConnection) {
                    //check if our connection is successful by getting a screenshot first from our server!
                    networkRequests.checkFirstShot { code ->
                        if (code == "200") {
                            nextPage(urlTextEdit)
                        }
                    }
                } else {
                    val message = Toast.makeText(
                        applicationContext,
                        "Could not establish a successful connection on this URL! try again",
                        Toast.LENGTH_SHORT
                    )
                    message.show()
                }
            }
        }
    }

    /**
     * Called when a url is selected from the room database to be
     * displayed on the text on the corner of the screen.
     */
    fun selectedUrlHighlight(view : View) {
        val str = (view as TextView).text.toString()
        val urlTextEdit = findViewById<EditText>(R.id.urlInput)
        urlTextEdit.text = Editable.Factory.getInstance().newEditable(str)
    }

    /**
     * Go to the next page (Control page)
     */
    private fun nextPage(urlItem: String) {
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("urlItem", urlItem)
        startActivity(intent)
    }
}