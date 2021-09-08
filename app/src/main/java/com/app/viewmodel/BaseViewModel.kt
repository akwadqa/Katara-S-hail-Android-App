package com.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.api.ApiResponseData
import com.app.model.dataclasses.ApiError
import com.app.utils.ApiCodes
import com.app.utils.DataValidation
import com.app.utils.LoadingState
import retrofit2.Response

open class BaseViewModel : ViewModel() {

    private val _response = MutableLiveData<ApiResponseData>()

    private val _loadingState = MutableLiveData<LoadingState>()

    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val response: LiveData<ApiResponseData>
        get() = _response

    val mValidationLiveData by lazy { MutableLiveData<DataValidation>() }

    fun getValidationLiveData() = mValidationLiveData

    private fun updateResponseObserver(response: ApiResponseData) {
        _response.value = response
    }

    fun getResponseObserver() = response

    fun setLoadingState(state: LoadingState) {
        _loadingState.value = state
    }

    /**
     * @param baseData response coming from backend
     * @param callBack lambda callback update view model
     */
    fun updateView(apiCode: Int, baseData: Any?, callBack: (API_VIEWMODEL_DATA) -> Unit) {
        if (baseData != null && baseData==401) {
            callBack(API_VIEWMODEL_DATA.API_TOKEN_EXPIRED())
            updateResponseObserver(ApiResponseData.API_TOKEN_EXPIRED())
            return
        }
        else if (baseData != null) {
            callBack(API_VIEWMODEL_DATA.API_SUCCEED(baseData, apiCode))
            updateResponseObserver(ApiResponseData.API_SUCCEED(apiCode))
            return
        }
        else
        {
            callBack(API_VIEWMODEL_DATA.API_EXCEPTION())
            updateResponseObserver(ApiResponseData.API_EXCEPTION())
            return
        }
    }
}
