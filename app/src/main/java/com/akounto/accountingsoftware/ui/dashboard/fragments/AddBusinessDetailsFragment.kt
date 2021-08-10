package com.akounto.accountingsoftware.ui.dashboard.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.akounto.accountingsoftware.Data.CountryData
import com.akounto.accountingsoftware.Data.Currency
import com.akounto.accountingsoftware.R
import com.akounto.accountingsoftware.response.BusinessDetails
import com.akounto.accountingsoftware.response.TransactionStatus
import com.akounto.accountingsoftware.ui.base.model.DateTimeModel
import com.akounto.accountingsoftware.ui.base.model.TimeZoneModel
import com.akounto.accountingsoftware.ui.dashboard.activity.DashboardProfileSetupActivity
import com.akounto.accountingsoftware.ui.dashboard.adapter.*
import com.akounto.accountingsoftware.ui.dashboard.viewModel.DashboardProfileSetupViewModel
import com.akounto.accountingsoftware.util.*
import com.bumptech.glide.Glide
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddBusinessDetailsFragment : Fragment() {
    var context: AppCompatActivity? = null

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


    var btnCompleteBusinessProfile: Button? = null
    var linBusinessProfileTemplate: LinearLayout? = null
    var linBusinessProfileDetails: LinearLayout? = null
    var btnSave: Button? = null

    var btnUploadLogo: Button? = null
    var linLogoUploaded: LinearLayout? = null
    var linChangeLogo: LinearLayout? = null
    var imgUploadedLogo: ImageView? = null

    var etBusinessName: EditText? = null
    var etAddress1: EditText? = null
    var etAddress2: EditText? = null
    var etPostcode: EditText? = null
    var etMobileNumber: EditText? = null
    var etFax: EditText? = null
    var etTollFree: EditText? = null
    var etBusinessPhone: EditText? = null
    var etCity: EditText? = null

    var spCountry: Spinner? = null
    var spState: Spinner? = null
    var spBusinessPhone: Spinner? = null
    var spCurrency: Spinner? = null
    var spTimeZone: Spinner? = null
    var spFiscalMonth: Spinner? = null
    var spFiscalDays: Spinner? = null


    var countryAdapter: CountrySpinnerAdapter? = null
    var countryList: ArrayList<CountryData> = ArrayList()

    var countryCodeAdapter: CountryCodeSpinnerAdapter? = null
    var countryCodeList: ArrayList<CountryData> = ArrayList()

    var stateAdapter: StateSpinnerAdapter? = null
    var stateList: ArrayList<String> = ArrayList()

    var currencyAdapter: CurrencySpinnerAdapter? = null
    var currencyList: ArrayList<Currency> = ArrayList()

    var timeZoneAdapter: TimeZoneSpinnerAdapter? = null
    var timeZoneList: ArrayList<TimeZoneModel> = ArrayList()

    var monthAdapter: MonthSpinnerAdapter? = null
    var monthList: ArrayList<DateTimeModel> = ArrayList()

    var daysAdapter: DaysSpinnerAdapter? = null
    var daysList: ArrayList<DateTimeModel> = ArrayList()


    //ViewModel
    var dashboardProfileViewModel: DashboardProfileSetupViewModel? = null
    var file: File? = null
    var businessDetailsModel: BusinessDetails? = null
    var errorMessage: String? = null

    var selectedCountry: String? = null
    var selectedState: String? = null
    var selectedBusinessPhoneCode: String? = null
    var selectedCurrency: String? = null
    var selectedTimeZone: String? = null
    var selectedFisalMonth: String? = null
    var selectedFisalDay: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context = activity as AppCompatActivity?

        //setup ViewModel
        dashboardProfileViewModel = ViewModelProviders.of(this@AddBusinessDetailsFragment)
            .get(DashboardProfileSetupViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_bussiness_details, container, false)

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


        btnCompleteBusinessProfile = view.findViewById(R.id.btnCompleteBusinessProfile)
        linBusinessProfileTemplate = view.findViewById(R.id.linBusinessProfileTemplate)
        linBusinessProfileDetails = view.findViewById(R.id.linBusinessProfileDetails)
        btnSave = view.findViewById(R.id.btnSave)


        btnUploadLogo = view.findViewById(R.id.btnUploadLogo)
        linLogoUploaded = view.findViewById(R.id.linLogoUploaded)
        linChangeLogo = view.findViewById(R.id.linChangeLogo)
        imgUploadedLogo = view.findViewById(R.id.imgUploadedLogo)


        etBusinessName = view.findViewById(R.id.etBusinessName)
        etAddress1 = view.findViewById(R.id.etAddress1)
        etAddress2 = view.findViewById(R.id.etAddress2)
        etPostcode = view.findViewById(R.id.etPostcode)
        etMobileNumber = view.findViewById(R.id.etMobileNumber)
        etFax = view.findViewById(R.id.etFax)
        etTollFree = view.findViewById(R.id.etTollFree)
        etBusinessPhone = view.findViewById(R.id.etBusinessPhone)
        etCity = view.findViewById(R.id.etCity)

        spCountry = view.findViewById(R.id.spCountry)
        spState = view.findViewById(R.id.spState)
        spBusinessPhone = view.findViewById(R.id.spBusinessPhone)
        spCurrency = view.findViewById(R.id.spCurrency)
        spTimeZone = view.findViewById(R.id.spTimeZone)
        spFiscalMonth = view.findViewById(R.id.spFiscalMonth)
        spFiscalDays = view.findViewById(R.id.spFiscalDays)


        //Set layout visibility
        relSteppers?.visibility = View.GONE
        linBusinessProfileDetails?.visibility = View.GONE
    }

    private fun clickListener() {
        relStepperOne?.setOnClickListener {
            if (relSteppers?.visibility == View.GONE) {
                relSteppers?.visibility = View.VISIBLE
            } else {
                relSteppers?.visibility = View.GONE
            }
        }

        btnCompleteBusinessProfile?.setOnClickListener {
            linBusinessProfileTemplate?.visibility = View.GONE
            linBusinessProfileDetails?.visibility = View.VISIBLE

            getCompanyByIdData()
        }

        spCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                val countryModel: CountryData? = countryList[position]
                countryModel?.run {
                    selectedCountry = this.phoneCode
                    val countryId: Int = Integer.parseInt(this.id)
                    getStateListData(countryId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spState?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectedState = stateList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spBusinessPhone?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectedBusinessPhoneCode = countryCodeList[position].phoneCode
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spCurrency?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectedCurrency = currencyList[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spTimeZone?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                selectedTimeZone = timeZoneList[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spFiscalMonth?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                monthList?.let {
                    selectedFisalMonth = monthList[position].monthInFullString

                    val model = monthList[position]
                    if (daysList.size > 0) {
                        daysList.clear()
                    }
                    daysList.addAll(
                        DateTime.getDatesWithInMonth(
                            model.yearInInteger ?: 0,
                            model.monthInInteger ?: 0
                        )
                    )
                    if (daysList.isNotEmpty()) {
                        daysAdapter?.notifyDataSetChanged()
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spFiscalDays?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                monthList?.let {
                    selectedFisalDay = daysList[position].dayInInteger.toString()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        linChangeLogo?.setOnClickListener {
            btnUploadLogo?.performClick()
        }

        btnUploadLogo?.setOnClickListener {
            if (context?.let { it1 -> PermissionUtility.checkPermissions(it1) } == true) {
                if (CameraUtils.isDeviceSupportCamera(requireContext())) {
                    CameraUtils.captureImageFromCameraGallery(
                        requireContext() as Activity,
                        "ProfileImage"
                    )
                } else {
                    Toast.makeText(
                        context, "Camera is not supported into the device", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                context?.let { it1 -> PermissionUtility.requestMultiplePermissions(it1) };
            }
        }


        btnSave?.setOnClickListener {
            if (checkSaveData()) {
                saveAddBusinessData()
            } else {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkSaveData(): Boolean {
        var isSaveSuccess = true

        if (isSaveSuccess) {
            if (TextUtils.isEmpty(etBusinessName?.text)) {
                isSaveSuccess = false
                errorMessage = "Please enter the business name"
            }
        }

        if (isSaveSuccess) {
            if (TextUtils.isEmpty(etBusinessPhone?.text)) {
                isSaveSuccess = false
                errorMessage = "Please enter the business phone number"
            }
        }

        if (isSaveSuccess) {
            if (TextUtils.isEmpty(selectedTimeZone)) {
                isSaveSuccess = false
                errorMessage = "Please choose your timeZone"
            }
            if (isSaveSuccess) {
                if (!TextUtils.isEmpty(selectedTimeZone) && selectedTimeZone == "Choose your timeZone") {
                    isSaveSuccess = false
                    errorMessage = "Please choose your timeZone"
                }
            }
        }

        return isSaveSuccess
    }

    private fun setInitialData() {
        (activity as DashboardProfileSetupActivity).setToolbarTitle("Add Business details")

        //Country
        countryAdapter = context?.let { CountrySpinnerAdapter(it, countryList) }
        spCountry?.adapter = countryAdapter

        countryList.addAll(CommonMethod.getCountryList(context))
        if (countryList.isNotEmpty()) {
            countryAdapter?.notifyDataSetChanged()
        }

        //Country Postal Code
        countryCodeAdapter = context?.let { CountryCodeSpinnerAdapter(it, countryCodeList) }
        spBusinessPhone?.adapter = countryCodeAdapter

        countryCodeList.addAll(CommonMethod.getCountryList(context))
        if (countryCodeList.isNotEmpty()) {
            countryCodeAdapter?.notifyDataSetChanged()
        }

        //State
        stateAdapter = context?.let { StateSpinnerAdapter(it, stateList) }
        spState?.adapter = stateAdapter

        //Currency
        currencyAdapter = context?.let { CurrencySpinnerAdapter(it, currencyList) }
        spCurrency?.adapter = currencyAdapter

        currencyList.addAll(CommonMethod.getCurrencyList(context))
        if (currencyList.isNotEmpty()) {
            currencyAdapter?.notifyDataSetChanged()
        }

        //TimeZone
        timeZoneAdapter = context?.let { TimeZoneSpinnerAdapter(it, timeZoneList) }
        spTimeZone?.adapter = timeZoneAdapter

        timeZoneList.addAll(CommonMethod.getTimeZoneList(context))

        val timeZoneModel = TimeZoneModel()
        timeZoneModel.name = "Choose your timeZone"
        timeZoneList.add(0, timeZoneModel)

        if (timeZoneList.isNotEmpty()) {
            timeZoneAdapter?.notifyDataSetChanged()
        }


        //Month
        monthAdapter = context?.let { MonthSpinnerAdapter(it, monthList) }
        spFiscalMonth?.adapter = monthAdapter

        monthList.addAll(DateTime.getMonthListInYear())
        if (monthList.isNotEmpty()) {
            monthAdapter?.notifyDataSetChanged()
        }

        for (i in monthList.indices) {
            if (monthList[i].monthInInteger == 12) {
                spFiscalMonth?.setSelection(i)
                break
            }
        }

        //Days
        daysAdapter = context?.let { DaysSpinnerAdapter(it, daysList) }
        spFiscalDays?.adapter = daysAdapter

        daysList.addAll(DateTime.getDatesWithInMonth(DateTime.getCurrentYear(), 12))
        if (daysList.isNotEmpty()) {
            daysAdapter?.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (context?.let {
                PermissionUtility.onRequestPermissionsResult(
                    it, requestCode, permissions, grantResults
                )
            } == true
        ) {
            btnUploadLogo?.performClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if the result is capturing Image
        when (resultCode) {
            RESULT_OK -> {
                when (requestCode) {
                    Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE -> {
                        btnUploadLogo?.visibility = View.GONE
                        linLogoUploaded?.visibility = View.VISIBLE

                        imgUploadedLogo?.let {
                            Glide.with(it).clear(it)
                            file = CameraUtils.getMediaFilePath(context, "ProfileImage")
                            CameraUtils.displayImage(context, file, it)
                        }
                    }
                    Constants.GALLERY_IMAGE_REQUEST_CODE -> {
                        btnUploadLogo?.visibility = View.GONE
                        linLogoUploaded?.visibility = View.VISIBLE

                        val realPath = data?.data?.let {
                            CameraUtils.getPath(requireContext(), it)
                        }
                        file = File(realPath)
                        imgUploadedLogo?.let { CameraUtils.displayImage(context, file, it) }
                    }
                }
            }
            RESULT_CANCELED -> {
                //User cancelled Image capture
                Toast.makeText(context, "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                //Failed to capture image
                Toast.makeText(context, "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    //************ Api Calling ************
    private fun getCompanyByIdData() {
        val companyId: Int = Integer.parseInt(UiUtil.getComp_Id(context))
        activity?.let {
            dashboardProfileViewModel?.getCompanyByIdData(context, companyId)?.observe(
                it, Observer { companyResponse ->

                    companyResponse?.let { it1 ->
                        val businessDetails: BusinessDetails? = it1.businessDetails

                        businessDetails?.run {
                            businessDetailsModel = businessDetails

                            if (!TextUtils.isEmpty(businessName)) {
                                etBusinessName?.setText(businessName)
                            }
                            if (!TextUtils.isEmpty(address1)) {
                                etAddress1?.setText(address1)
                            }
                            if (!TextUtils.isEmpty(address2)) {
                                etAddress2?.setText(address2)
                            }
                            if (!TextUtils.isEmpty(postCode)) {
                                etPostcode?.setText(postCode)
                            }
                            if (!TextUtils.isEmpty(mobile)) {
                                etMobileNumber?.setText(mobile)
                            }
                            if (!TextUtils.isEmpty(fax)) {
                                etFax?.setText(fax)
                            }
                            if (!TextUtils.isEmpty(tollFree)) {
                                etTollFree?.setText(tollFree)
                            }
                            if (!TextUtils.isEmpty(city)) {
                                etCity?.setText(city)
                            }

                            //Set the default country
                            if (country != null) {
                                for (i in countryList.indices) {
                                    if ((countryList[i].phoneCode).toInt() == country) {
                                        spCountry?.setSelection(i)

                                        //set the default state
                                        getStateListData(country)
                                        break
                                    }
                                }
                            }

                            //Set the default country code in business phone
                            if (country != null) {
                                for (i in countryCodeList.indices) {
                                    if ((countryCodeList[i].phoneCode).toInt() == country) {
                                        spBusinessPhone?.setSelection(i)
                                        break
                                    }
                                }
                            }

                            //Set the default Time Zone
                            if (!TextUtils.isEmpty(timeZone)) {
                                for (i in timeZoneList.indices) {
                                    if (timeZoneList[i].name == timeZone) {
                                        spTimeZone?.setSelection(i)
                                        break
                                    }
                                }
                            }

                            //Set the default currency
                            if (!TextUtils.isEmpty(businessCurrency)) {
                                for (i in currencyList.indices) {
                                    if (currencyList[i].name == businessCurrency) {
                                        spCurrency?.setSelection(i)
                                        break
                                    }
                                }
                            }
                        }
                    }
                })
        }
    }

    private fun getStateListData(countryId: Int) {
        activity?.let {
            dashboardProfileViewModel?.getStateByCountryIdData(context, countryId)?.observe(
                it, Observer { stateResponse ->
                    stateResponse?.let { it1 ->
                        val list: ArrayList<String> = stateResponse.states as ArrayList<String>

                        if (stateList.size > 0) {
                            stateList.clear()
                        }
                        if (list.isNotEmpty()) {
                            stateList.addAll(list)
                            stateAdapter?.notifyDataSetChanged()
                        }

                        //Set the default state
                        if (!TextUtils.isEmpty(businessDetailsModel?.state)) {
                            for (i in stateList.indices) {
                                if (stateList[i] == businessDetailsModel?.state) {
                                    spState?.setSelection(i)
                                    break
                                }
                            }
                        }
                    }
                })
        }
    }

    private fun saveAddBusinessData() {
        val hashMap = HashMap<String, String>()
        hashMap["Id"] = businessDetailsModel?.id.toString() ?: "0"
        hashMap["BusinessName"] = etBusinessName?.text.toString()
        hashMap["Country"] = selectedCountry ?: "0"
        hashMap["BusinessCurrency"] = selectedCurrency ?: ""
        hashMap["Address1"] = etAddress1?.text.toString()
        hashMap["Address2"] = etAddress2?.text.toString()
        hashMap["City"] = etCity?.text.toString()
        hashMap["State"] = selectedState ?: ""
        hashMap["PostCode"] = etPostcode?.text.toString()
        hashMap["TimeZone"] = selectedTimeZone ?: ""
        hashMap["Phone"] = etBusinessPhone?.text.toString()
        hashMap["Mobile"] = etMobileNumber?.text.toString()
        hashMap["Fax"] = etFax?.text.toString()
        hashMap["TollFree"] = etTollFree?.text.toString()
        hashMap["DomainUrl"] = businessDetailsModel?.domainUrl ?: ""
        hashMap["Logo"] = businessDetailsModel?.logo ?: ""

        //Save api calling data
        activity?.let {
            dashboardProfileViewModel?.saveAddBusinessData(context, hashMap)?.observe(
                it, Observer { response ->
                    response?.let { it1 ->
                        val transactionData: TransactionStatus? = it1.transactionStatus

                        if (transactionData?.isIsSuccess == true) {
                            //Upload Logo Image
                            if (file != null) {
                                uploadLogoImage(file)
                            } else {
                                (activity as DashboardProfileSetupActivity).flDashboardProfileSetup?.let { it1 ->
                                    CommonMethod.replaceFragmentNotToStack(
                                        activity as AppCompatActivity,
                                        it1, AddItemsAndServicesFragment(), false
                                    )
                                }
                            }
                        } else {
                            Toast.makeText(context, response.statusMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        }
    }

    @SuppressLint("NewApi")
    private fun uploadLogoImage(file: File?) {
        var base64String: String = ""
        val bytes = file?.readBytes()
        base64String = Base64.getEncoder().encodeToString(bytes)

        val hashMap = HashMap<String, String>()
        hashMap["LogoData"] = base64String

        activity?.let {
            dashboardProfileViewModel?.uploadLogoAddBusiness(context, hashMap)?.observe(
                it, Observer { response ->
                    response?.let { it1 ->
                        val transactionData: TransactionStatus? = it1.transactionStatus

                        if (transactionData?.isIsSuccess == true) {
                            (activity as DashboardProfileSetupActivity).flDashboardProfileSetup?.let { it1 ->
                                CommonMethod.replaceFragmentNotToStack(
                                    activity as AppCompatActivity,
                                    it1, AddItemsAndServicesFragment(), false
                                )
                            }
                        }
                    }
                })
        }
    }


    /*fun encoder(filePath: String): String {
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }*/
}