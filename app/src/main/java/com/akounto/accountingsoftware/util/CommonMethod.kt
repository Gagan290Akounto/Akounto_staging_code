package com.akounto.accountingsoftware.util

import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akounto.accountingsoftware.Data.CountryData
import com.akounto.accountingsoftware.Data.Currency
import com.akounto.accountingsoftware.ui.base.model.TimeZoneModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.InputStream
import java.lang.reflect.Type

object CommonMethod {
    /*public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    fun getAmountWithSymbol(amount: Double): String {
        val BILLION = 1000000000.0
        val MILLION = 1000000.0
        val THOUSAND = 1000.0

        return when {
            amount >= BILLION -> {
                (amount / BILLION).toString() + "B"
            }
            amount >= MILLION -> {
                (amount / MILLION).toString() + "M"
            }
            amount >= THOUSAND -> {
                (amount / THOUSAND).toString() + "K"
            }
            else -> {
                amount.toString()
            }
        }
    }

    fun addFragmentNotToStack(
        activity: AppCompatActivity, frameLayout: FrameLayout,
        fragment: Fragment, addToBackStack: Boolean
    ) {
        if (addToBackStack) {
            activity.supportFragmentManager
                .beginTransaction()
                .add(frameLayout.id, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name)
                .commit()
        } else {
            activity.supportFragmentManager
                .beginTransaction()
                .add(frameLayout.id, fragment, fragment.javaClass.name)
                .commit()
        }
    }

    fun replaceFragmentNotToStack(
        activity: AppCompatActivity, frameLayout: FrameLayout,
        fragment: Fragment, addToBackStack: Boolean
    ) {
        if (addToBackStack) {
            activity.supportFragmentManager
                .beginTransaction()
                .replace(frameLayout.id, fragment, fragment.javaClass.name)
                .addToBackStack(fragment.javaClass.name)
                .commit()
        } else {
            activity.supportFragmentManager
                .beginTransaction()
                .replace(frameLayout.id, fragment, fragment.javaClass.name)
                .commit()
        }
    }

    fun getJsonDataFromAsset(fileName: String, context: Context?): String? {
        var json: String? = null
        try {
            val inputStream: InputStream? = context?.assets?.open(fileName)
            json = inputStream?.bufferedReader().use { it?.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }


    fun getCurrencyList(activity: AppCompatActivity?): ArrayList<Currency> {
        var currencyList: ArrayList<Currency> = ArrayList()

        try {
            val currencyJsonString = getJsonDataFromAsset("json/currency.json", activity)
            val currencyJsonArray = JSONArray(currencyJsonString)
            val currencyType: Type? = object : TypeToken<ArrayList<Currency>>() {}.type

            currencyList =
                Gson().fromJson(currencyJsonArray.toString(), currencyType) as ArrayList<Currency>

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return currencyList
    }

    fun getCountryList(activity: AppCompatActivity?): ArrayList<CountryData> {
        var countryList: ArrayList<CountryData> = ArrayList()

        try {
            val countryJsonString = getJsonDataFromAsset("json/country.json", activity)
            val countryJsonArray = JSONArray(countryJsonString)
            val countryType: Type? = object : TypeToken<ArrayList<CountryData>>() {}.type

            countryList =
                Gson().fromJson(countryJsonArray.toString(), countryType) as ArrayList<CountryData>

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return countryList
    }

    fun getTimeZoneList(activity: AppCompatActivity?): ArrayList<TimeZoneModel> {
        var timeZoneList: ArrayList<TimeZoneModel> = ArrayList()

        try {
            val timeZoneJsonString = getJsonDataFromAsset("json/time-zone.json", activity)
            val timeZoneJsonArray = JSONArray(timeZoneJsonString)
            val timeZoneType: Type? = object : TypeToken<List<TimeZoneModel>>() {}.type

            timeZoneList =
                Gson().fromJson(
                    timeZoneJsonArray.toString(),
                    timeZoneType
                ) as ArrayList<TimeZoneModel>

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return timeZoneList
    }

    fun countryCodeToEmoji(code: String?): String? {

        // offset between uppercase ascii and regional indicator symbols
        var code = code
        val OFFSET = 127397

        // validate code
        if (code == null || code.length != 2) {
            return ""
        }

        //fix for uk -> gb
        if (code.equals("uk", ignoreCase = true)) {
            code = "gb"
        }

        // convert code to uppercase
        code = code.toUpperCase()
        val emojiStr = StringBuilder()

        //loop all characters
        for (i in 0 until code.length) {
            emojiStr.appendCodePoint(code[i].toInt() + OFFSET)
        }

        // return emoji
        return emojiStr.toString()
    }
}