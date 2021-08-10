package com.akounto.accountingsoftware.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akounto.accountingsoftware.Data.CountryData
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.util.CommonMethod
import java.util.*

class CountryCodeSpinnerAdapter(
    var activity: AppCompatActivity, var dataList: ArrayList<CountryData>
) :
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

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_selected_country_code_spinner_layout, null)

        val tvCountryCodeSelectedSpinner =
            view.findViewById<TextView>(R.id.tvCountryCodeSelectedSpinner)

        val tvCountryFlag =
            view.findViewById<TextView>(R.id.tvCountryFlag)

        tvCountryCodeSelectedSpinner.text = " +" + dataList[position].phoneCode
        tvCountryFlag.text = CommonMethod.countryCodeToEmoji(dataList[position].countryCode)


        Log.e(
            "CountryCode",
            "Selected value " + CommonMethod.countryCodeToEmoji(dataList[position].countryCode)
        )
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_dropdown_country_code_spinner_layout, null)

        val tvCountryCodeSpinner = view.findViewById<TextView>(R.id.tvCountryCodeSpinner)
        val tvCountryFlag = view.findViewById<TextView>(R.id.tvCountryFlag)

        tvCountryCodeSpinner.text = dataList[position].name + " +" + dataList[position].phoneCode
        tvCountryFlag.text = CommonMethod.countryCodeToEmoji(dataList[position].countryCode)

        Log.e(
            "CountryCode",
            "Dropdown value " + CommonMethod.countryCodeToEmoji(dataList[position].countryCode)
        )
        return view
    }


}