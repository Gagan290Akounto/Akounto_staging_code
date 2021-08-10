package com.akounto.accountingsoftware.ui.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.ui.dashboard.activity.DashboardProfileSetupActivity

class AddItemsAndServicesFragment : Fragment() {

    var relSteppers: RelativeLayout? = null
    var relStepperOne: RelativeLayout? = null
    var relStepperTwo: RelativeLayout? = null
    var relStepperThree: RelativeLayout? = null
    var relStepperFour: RelativeLayout? = null

    var tvStepperOne: TextView? = null
    var tvStepperAddBusiness: TextView? = null

    var tvStepperTwo: TextView? = null
    var tvStepperAddItemAndServices: TextView? = null

    var tvStepperThree: TextView? = null
    var tvStepperAddCustomer: TextView? = null

    var tvStepperFour: TextView? = null
    var tvStepperStartInvoice: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_item_services, container, false)

        initView(view)
        clickListener()
        setInitialData()

        return view
    }

    private fun initView(view: View) {
        relSteppers = view.findViewById(R.id.relSteppers)
        relStepperOne = view.findViewById(R.id.relStepperOne)
        relStepperTwo = view.findViewById(R.id.relStepperTwo)
        relStepperThree = view.findViewById(R.id.relStepperThree)
        relStepperFour = view.findViewById(R.id.relStepperFour)

        tvStepperOne = view.findViewById(R.id.tvStepperOne)
        tvStepperAddBusiness = view.findViewById(R.id.tvStepperAddBusiness)

        tvStepperTwo = view.findViewById(R.id.tvStepperTwo)
        tvStepperAddItemAndServices = view.findViewById(R.id.tvStepperAddItemAndServices)

        tvStepperThree = view.findViewById(R.id.tvStepperThree)
        tvStepperAddCustomer = view.findViewById(R.id.tvStepperAddCustomer)

        tvStepperFour = view.findViewById(R.id.tvStepperFour)
        tvStepperStartInvoice = view.findViewById(R.id.tvStepperStartInvoice)
    }

    private fun clickListener() {

    }

    private fun setInitialData() {
        (activity as DashboardProfileSetupActivity).setToolbarTitle("Add Items and Services")
    }
}