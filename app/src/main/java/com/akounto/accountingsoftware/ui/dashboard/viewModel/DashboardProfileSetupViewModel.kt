package com.akounto.accountingsoftware.ui.dashboard.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akounto.accountingsoftware.response.GetCompanyResponse
import com.akounto.accountingsoftware.response.GetStatesResponse
import com.akounto.accountingsoftware.ui.base.model.BaseResponseModel
import com.akounto.accountingsoftware.ui.dashboard.repository.DashboardProfileSetupRepository
import kotlin.collections.HashMap

class DashboardProfileSetupViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DashboardProfileSetupRepository? = null

    private var companyData: MutableLiveData<GetCompanyResponse>
    private var stateData: MutableLiveData<GetStatesResponse>
    private var saveResponseData: MutableLiveData<BaseResponseModel>? = null
    private var uploadLogoResponseData: MutableLiveData<BaseResponseModel>? = null

    init {
        repository = DashboardProfileSetupRepository()
        companyData = MutableLiveData()
        stateData = MutableLiveData()
        saveResponseData = MutableLiveData()
        uploadLogoResponseData = MutableLiveData()
    }

    fun getCompanyByIdData(mContext: Context?, companyId: Int): LiveData<GetCompanyResponse> {
        companyData =
            repository?.getBusinessById(mContext, companyId) as MutableLiveData<GetCompanyResponse>

        return companyData
    }

    fun getStateByCountryIdData(mContext: Context?, countryId: Int): LiveData<GetStatesResponse> {
        stateData = repository?.getStateByCompanyId(
            mContext, countryId
        ) as MutableLiveData<GetStatesResponse>

        return stateData
    }

    fun saveAddBusinessData(
        mContext: Context?, hashMap: HashMap<String, String>
    ): LiveData<BaseResponseModel>? {

        saveResponseData = repository?.saveAddBusinessData(mContext, hashMap)
        return saveResponseData
    }

    fun uploadLogoAddBusiness(
        mContext: Context?, hashMap: HashMap<String, String>
    ): LiveData<BaseResponseModel>? {
        uploadLogoResponseData = repository?.uploadLogoAddBusinessData(mContext, hashMap)
        return uploadLogoResponseData
    }
}