package com.example.houbikes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_station.*

class StationActivity : AppCompatActivity() {

    var station_id: String = ""
    var name: String = ""
    var address: String = ""
    var lat: Double = 0.0
    var lon: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        station_id = intent!!.getStringExtra("station_id")
        name = intent!!.getStringExtra("name")
        address = intent!!.getStringExtra("address")
        lat = intent!!.getDoubleExtra("lat",0.0)
        lon = intent!!.getDoubleExtra("lon",0.0)


        stationTitle.text = name
        stationAddress.text = address


        //https://gbfs.bcycle.com/bcycle_houston/station_status.json

    }
}
