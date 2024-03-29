package com.example.houbikes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import android.widget.ListView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.houbikes.Adapters.CStationAdapter
import com.example.houbikes.Models.CStation
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var qStation: RequestQueue? = null
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        qStation = Volley.newRequestQueue(this@MainActivity)

        setTitle("HOU Bike Share")

        getStation()
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun getStation(){
        val aList = arrayListOf<CStation>()
        val request = object: JsonObjectRequest(Method.GET, "https://gbfs.bcycle.com/bcycle_houston/station_information.json", null, Response.Listener<JSONObject> { response ->
            val dObject = JSONObject(response.toString())
            val data = JSONObject(dObject.get("data").toString())
            val dStations = JSONArray(data.get("stations").toString())

            for (i in 0 until dStations.length()) {
                var dataInner: JSONObject = dStations.getJSONObject(i)
                //Log.d(">>>>>>",dataInner.getString("name"))
                aList.add(
                    CStation(
                        dataInner.getString("station_id"),
                        dataInner.getString("name"),
                        dataInner.getString("address"),
                        dataInner.getDouble("lat"),
                        dataInner.getDouble("lon")
                ))
            }

            val adapter = CStationAdapter(this, aList)
            amListStation.adapter = adapter

            amListStation.setOnItemClickListener { _, _, position, _ ->
                var intent: Intent? = Intent(this, StationActivity::class.java)

                // Pass the values to next activity (StationActivity)
                intent!!.putExtra("station_id",aList[position].STATION_ID)
                intent!!.putExtra("name",aList[position].STATION_NAME)
                intent!!.putExtra("address",aList[position].STATION_ADDRESS)
                intent!!.putExtra("lat",aList[position].STATION_LAT)
                intent!!.putExtra("lon",aList[position].STATION_LON)

                startActivity(intent)
            }

            amProgressBar.visibility = View.GONE
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
        qStation!!.add(request)
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
}
