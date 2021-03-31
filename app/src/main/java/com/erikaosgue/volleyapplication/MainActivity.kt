package com.erikaosgue.volleyapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import com.erikaosgue.volleyapplication.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

	private var requestQueue: RequestQueue? =null
	private var url1 =  "https://www.google.com/"
	private var url2 = "https://pastebin.com/raw/Em972E5s"
	private var url3 = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02"

	lateinit var  activityMainBinding: ActivityMainBinding
 	override fun onCreate(savedInstanceState: Bundle?) {
 		super.onCreate(savedInstanceState)
 		activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
 		setContentView(activityMainBinding.root)

		// Instantiate the RequestQueue.
		requestQueue = Volley.newRequestQueue(this)

		getStringRequest(url1)
		getJSONArray(url2)
		getJSONObject(url3)

  	}

	private fun getJSONObject(url: String) {

		val textView = findViewById<TextView>(R.id.jsonObjectRequest)


		val jsonObjectRequest = JsonObjectRequest(
				Request.Method.GET,
				url,
				null,

				//Response.Listener
				{ response: JSONObject ->


					// Getting a String from the Main Object with key ="type"
					val typeString: String = response["type"] as String
					Log.d("1. type:",  typeString)

					// Getting an object from the Main Object with key="metadata"
					val metaDataObject: JSONObject = response["metadata"] as JSONObject
					Log.d("2. MetadataObject: ", metaDataObject.toString())

					// Getting an Int from the metaDataObject with key="count"
					val countInt: Int = metaDataObject["count"] as Int
					Log.d("3 Count from Metadata", countInt.toString())

					// Getting an Array from the Main object with key="features"
					val featuresArray: JSONArray = response["features"]  as JSONArray
					Log.d("4. Features Array:", featuresArray.toString())

					// Putting the textView information
					("Main Object:\n" +
							"-> type: $typeString\n" +
							"-> metadata $metaDataObject\n" +
							"->count $countInt\n" +
							"->features $featuresArray").also { textView.text = it }

					//Getting multiple objects from the featuresArray list
					var featuresObj: JSONObject
					var featuresType: String
					var featuresPropertiesObj: JSONObject
					var propertiesMag: Any
					var featuresGeometryObj: JSONObject
					var featuresId: String

					for (i in 0 until featuresArray.length()) {

						/* featuresObject contains:
						* "type": String
						* "properties": JSONObject
						* "geometry": JSONObject -> {
						* 	"type": String
						* 	"coordinates": [-71.621, -29.888, 40] //List of int and Float
						* }
						* "id": String
						*/
						featuresObj = featuresArray[i] as JSONObject

						// Getting from the features Objects

						// key="type"
						featuresType = featuresObj["type"] as String

						// key="properties"
						featuresPropertiesObj = featuresObj["properties"] as JSONObject

						//key="mag" from featuresPropertiesObj
						propertiesMag = featuresPropertiesObj["mag"] as Any
						if (propertiesMag !is Number){
							propertiesMag = 0
						}

						// Key="geometry"
						featuresGeometryObj = featuresObj["geometry"] as JSONObject

						// Key="id"
						featuresId = featuresObj["id"] as String


						Log.d("Object # ===>", "${i+1}")
						Log.d("features OBJ:", "$featuresObj")
						Log.d("features type:", featuresType)
						Log.d("feat Properties OBJ:", "$featuresPropertiesObj")
						// Log.d("Properties Mag:", "${featuresPropertiesObj["mag"]}") In case of not using the type Any this works
						Log.d("Properties Mag:", "$propertiesMag")
						Log.d("Features geometry:", "$featuresGeometryObj")
						Log.d("features id: ", featuresId)
					}

				},

				//Response.Error
				{ error: VolleyError ->
						textView.text = "ERROR: %s".format(error.toString())
						Log.d("object error:", "$error")
				}
		)
		requestQueue?.add(jsonObjectRequest)

	}

	private fun getJSONArray(url: String) {

		val textView = findViewById<TextView>(R.id.jsonArrayRequest)

		val jsonArray = JsonArrayRequest(url,
				{ response ->
					try {
						textView.text = "Response is: ${response}"
						for (i in 0 until response.length()) {
							val myObject = response.getJSONObject(i)

							val firstName = myObject.getString("firstname")

								Log.d("object ${i+1} is:", "$myObject")
							Log.d("Firstname is:", firstName)

						}

					}catch (e: JSONException) {e.printStackTrace()}
				},
				{ error ->
					try {
						// Handle error
						textView.text = "ERROR: %s".format(error.toString())

					}catch (e: JSONException) {e.printStackTrace()}

				})

		requestQueue?.add(jsonArray)
	}

	private fun getStringRequest(url: String) {

		val textView = findViewById<TextView>(R.id.stringRequest)

		val stringRequest = StringRequest(Request.Method.GET, url,
			{ response ->
				// Display the first 500 characters of the response string.
				textView.text = "Response is: ${response.substring(0, 500)}"
			},
			{ textView.text = "That didn't work!" })

		requestQueue?.add(stringRequest)
	}

}

