package com.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.BaseApplication
import com.app.R
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.RegisterRepo
import com.app.utils.*
import com.google.gson.Gson
import java.text.SimpleDateFormat

class RegisterOneViewModel : BaseViewModel(), Observable {
    private val registerRepo by lazy { RegisterRepo() }

    var qID: String = ""

    var msg: String = ""

    var date: String = ""

    var otp: String = ""

    val dataMLD = MutableLiveData<QIDExtractResponseModel>()

    val dataOTPMLD = MutableLiveData<OTPModel>()

    val dataTokenMLD = MutableLiveData<UserTokenModel>()

    fun validate(context : Context) {
        if (qID.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.qid_empty),
                false
            )
        } else if (qID.trim().length!=11) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.qid_invalid),
                false
            )
        } else if (date.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.enter_date),
                false
            )
        }else {
            checkIdExist()
        }
    }


    private fun checkIdExist() {
     CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = registerRepo.checkQId(qID)
            updateView(ApiCodes.CHECK_QID,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("isExist") && map["isExist"] == true)
                        {
                            msg = "You are already registered"
                        }
                        else {
                            extractQID()
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }


    private fun extractQID() {
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        val qidExtractParamModel = QIDExtractParamModel(
            qid = qID,
            exp = date)
//            exp = sdf.parse(date))

        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = registerRepo.hitExtractQID(qidExtractParamModel)
            updateView(ApiCodes.EXTRACT_QID,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val loginResponseData =
                                Gson().fromJson(Gson().toJson(it.data), QIDExtractResponseModel::class.java)
                            dataMLD.value = loginResponseData
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }






    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

}