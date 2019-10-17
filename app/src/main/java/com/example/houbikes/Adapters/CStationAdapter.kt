package com.example.houbikes.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.houbikes.Models.CStation
import com.example.houbikes.R.layout
import com.example.houbikes.R.id

class CStationAdapter(context: Context, private val dataSource: ArrayList<CStation>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cItem = getItem(position) as CStation
        val rowView = inflater.inflate(layout.listview_station, parent, false)

        // Get title
        val name = rowView.findViewById(id.lvStationName) as TextView
        name.text = cItem.STATION_NAME

        // Get description
        val description = rowView.findViewById(id.lvStationAddress) as TextView
        description.text = cItem.STATION_ADDRESS

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}