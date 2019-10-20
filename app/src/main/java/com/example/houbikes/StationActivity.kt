package com.example.houbikes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.houbikes.Models.CStationStatus
import kotlinx.android.synthetic.main.activity_station.*
import org.json.JSONArray
import org.json.JSONObject

class StationActivity : AppCompatActivity() {

    var station_id: String = ""
    var name: String = ""
    var address: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    var num_docks_available = 0
    var num_bikes_available = 0
    var total_docks = 0
    var last_reported = ""

    private var qInfo: RequestQueue? = null
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        // Get the passing values from the other activity (MainActivity)
        station_id = intent!!.getStringExtra("station_id")
        name = intent!!.getStringExtra("name")
        address = intent!!.getStringExtra("address")
        lat = intent!!.getDoubleExtra("lat",0.0)
        lon = intent!!.getDoubleExtra("lon",0.0)

        setTitle(name)

        stationAddress.text = "Address: ${address}"

        qInfo = Volley.newRequestQueue(this@StationActivity)

        getStationStatus()
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun getStationStatus(){
        val aList = arrayListOf<CStationStatus>()
        val request = object: JsonObjectRequest(Method.GET, "https://gbfs.bcycle.com/bcycle_houston/station_status.json", null, Response.Listener<JSONObject> { response ->
            val dObject = JSONObject(response.toString())
            val data = JSONObject(dObject.get("data").toString())
            val dStations = JSONArray(data.get("stations").toString())

            for (i in 0 until dStations.length()) {
                var dataInner: JSONObject = dStations.getJSONObject(i)

                // station id must match, all other data ignored
                if (station_id == dataInner.getString("station_id")){
                    num_docks_available = dataInner.getInt("num_docks_available")
                    num_bikes_available = dataInner.getInt("num_bikes_available")
                    last_reported = dataInner.getString("last_reported")

                    // Get the total of docks
                    total_docks = num_docks_available + num_bikes_available;

                    stationBikeStatus.text = "$num_bikes_available out of $total_docks bike${addPlural(total_docks)}\n\nThere are $num_docks_available available dock${addPlural(num_docks_available)} to park your bike."
                }
            }

            saProgressBar.visibility = View.GONE
        }, Response.ErrorListener { e ->
            Log.d(">>>>>",e.message)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Accept"] = "application/json"
                return headers
            }
        }
        qInfo!!.add(request)
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun addPlural(total: Int):String{
        if (total > 1) {
            return "s"
        }
        return ""
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
}
