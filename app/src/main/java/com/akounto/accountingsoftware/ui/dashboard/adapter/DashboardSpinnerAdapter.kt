package com.akounto.accountingsoftware.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.akounto.accountingsoftware.Data.DashboardSearchData.Datum
import com.akounto.accountingsoftware.R
import java.util.*

class DashboardSpinnerAdapter(var context: Context, var dataList: ArrayList<Datum>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(i: Int): Any {
        return dataList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_dashboard_spinner_layout, null)
        val tvDashboardSpinner = view.findViewById<TextView>(R.id.tvDasboardSpinner)
        tvDashboardSpinner.text = dataList[position].label
        return view
    }

    @SuppressLint("InflateParams")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView = inflater.inflate(R.layout.item_dashboard_spinner_dropdown_layout, null)
        val tvDashboardSpinner = convertView.findViewById<TextView>(R.id.tvDasboardSpinner)
        tvDashboardSpinner.text = dataList[position].label
        return convertView
    }
}