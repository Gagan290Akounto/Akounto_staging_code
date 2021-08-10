package com.akounto.accountingsoftware.util

import android.util.Log
import com.akounto.accountingsoftware.ui.base.model.DateTimeModel
import java.text.SimpleDateFormat
import java.util.*

object DateTime {
    //dd	Date in numeric value
    val dateFormatter = SimpleDateFormat("dd")

    //E	Day in String (short form. Ex: Mon)
    val dayShortStringFormatter = SimpleDateFormat("E")

    //EEEE	Day in String (full form. Ex: Monday)
    val dayFullStringFormatter = SimpleDateFormat("EEEE")


    //MM	Month in numeric value
    val monthFormatter = SimpleDateFormat("MM")

    //LLL	Month in String (short form. Ex: Mar)
    val monthShortStringFormatter = SimpleDateFormat("LLL")

    //LLLL	Month in String (full form. Ex: March)
    val monthFullStringFormatter = SimpleDateFormat("LLLL")


    //yyyy	Year in numeric value
    val yearStringFormatter = SimpleDateFormat("yyyy")


    //HH	Hour in numeric value (24hrs timing format)
    val hours24Formatter = SimpleDateFormat("HH")

    //KK	Hour in numeric value (12hrs timing format)
    val hours12Formatter = SimpleDateFormat("KK")


    //mm	Minute in numeric value
    val minuteFormatter = SimpleDateFormat("mm")


    //ss	Seconds in numeric value
    val secondFormatter = SimpleDateFormat("ss")


    //aaa	Displays AM or PM (according to 12hrs timing format)
    val am_pm12Formatter = SimpleDateFormat("aaa")


    //z	Displays the time zone of the region
    val timeZoneFormatter = SimpleDateFormat("z")


    // ****************  Methods ****************
    fun getCurrentYear(): Int {
        var currentYear: Int = 12

        Calendar.getInstance().let { calendar ->
            calendar.add(Calendar.MONTH, -11)
            currentYear = yearStringFormatter.format(calendar.timeInMillis).toInt()
        }

        return currentYear
    }

    fun getMonthListInYear(): ArrayList<DateTimeModel> {
        //val formatter = SimpleDateFormat("MMM, yyyy")
        //val formatter = SimpleDateFormat("LLLL, yyyy")
        //val formatter = SimpleDateFormat("LLLL")

        val list: ArrayList<DateTimeModel> = arrayListOf()

        Calendar.getInstance().let { calendar ->
            calendar.add(Calendar.MONTH, -11)

            for (i in 0 until 12) {
                val dateTimeModel = DateTimeModel()

                dateTimeModel.monthInInteger = monthFormatter.format(calendar.timeInMillis).toInt()
                dateTimeModel.monthInShortString =
                    monthShortStringFormatter.format(calendar.timeInMillis)
                dateTimeModel.monthInFullString =
                    monthFullStringFormatter.format(calendar.timeInMillis)
                dateTimeModel.yearInInteger =
                    yearStringFormatter.format(calendar.timeInMillis).toInt()

                list.add(dateTimeModel)

                calendar.add(Calendar.MONTH, 1)
            }
        }

        return list
    }

    fun getDatesWithInMonth(year: Int, month: Int): ArrayList<DateTimeModel> {
        //val fmt = SimpleDateFormat("dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar[year, month - 1] = 1

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list: ArrayList<DateTimeModel> = arrayListOf()

        for (i in 0 until daysInMonth) {
            Log.e("AddBusinessDetailsFragment", "" + dateFormatter.format(calendar.time))

            val dateTimeModel = DateTimeModel()

            dateTimeModel.dayInInteger = dateFormatter.format(calendar.time).toInt()
            dateTimeModel.dayInShortString = dayShortStringFormatter.format(calendar.time)
            dateTimeModel.dayInFullString = dayFullStringFormatter.format(calendar.time)

            list.add(dateTimeModel)

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }
}