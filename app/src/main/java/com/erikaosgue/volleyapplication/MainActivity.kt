package com.erikaosgue.volleyapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import com.erikaosgue.volleyapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private var requestQueue: RequestQueue? =null
	private var url =  "https://www.google.com/"

	lateinit var  activityMainBinding: ActivityMainBinding
 	override fun onCreate(savedInstanceState: Bundle?) {
 		super.onCreate(savedInstanceState)
 		activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
 		setContentView(activityMainBinding.root)


		// Instantiate the RequestQueue.
		val requestQueue = Volley.newRequestQueue(this)
		getString(url)


  	}

	private fun getString(url: String) {
		val textView = findViewById<TextView>(R.id.textViewId)

		val stringRequest = StringRequest(Request.Method.GET, url,
			{ response ->
				// Display the first 500 characters of the response string.
				textView.text = "Response is: ${response.substring(0, 500)}"
			},
			{ textView.text = "That didn't work!" })

		requestQueue?.add(stringRequest)
	}
}