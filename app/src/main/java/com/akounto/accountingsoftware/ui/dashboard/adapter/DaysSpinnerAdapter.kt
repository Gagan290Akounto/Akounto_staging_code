package com.akounto.accountingsoftware.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.ui.base.model.DateTimeModel
import java.util.*

class DaysSpinnerAdapter(var activity: AppCompatActivity, var dataList: ArrayList<DateTimeModel>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(i: Int): Any? {
        return dataList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_spinner_layout, null)
        val tvDashboardSpinner = view.findViewById<TextView>(R.id.tvDasboardSpinner)
        tvDashboardSpinner.text = dataList[position].dayInInteger.toString()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = inflater.inflate(R.layout.item_spinner_layout, null)
        val tvDashboardSpinner = convertView.findViewById<TextView>(R.id.tvDasboardSpinner)
        tvDashboardSpinner.text = dataList[position].dayInInteger.toString()
        return convertView
    }
}