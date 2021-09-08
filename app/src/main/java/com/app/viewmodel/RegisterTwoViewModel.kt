package com.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.R
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.RegisterRepo
import com.app.utils.*
import com.google.gson.Gson

class RegisterTwoViewModel : BaseViewModel(), Observable {
    private val registerRepo by lazy { RegisterRepo() }

    var qID: String = ""

    var name: String = ""

    var nameAr: String = ""

    var number: String = ""

    var phoneCode: String = ""

    var email: String = ""

    var frontImage: String = ""

    var backImage: String = ""

    var nationality:String = ""

    var nationalityName:String = ""

    var switch: Boolean = false

    var isAuction: Boolean = false

    var otp: String = ""

    var msg: String = ""

    val dataMLD = MutableLiveData<OTPModel>()

    val dataTokenMLD = MutableLiveData<UserTokenModel>()

    val dataOTPMLD = MutableLiveData<SendOTPModel>()

    val dataISDMLD = MutableLiveData<ISDCodes>()

    val dataCountryMLD = MutableLiveData<Country>()

    var dataModel: QIDExtractDataModel = QIDExtractDataModel()

    fun validate(context : Context, isNotPassport: Boolean) {
        if (name.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.enter_name),
                false
            )
        }
        else if (number.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.enter_number),
                false
            )
        }else if (isAuction && frontImage.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.select_front),
                false
            )
        }
        else if (isAuction && backImage.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.select_back),
                false
            )
        }
        else if (!switch) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.select_switch),
                false
            )
        }else {
            if(isNotPassport){
                sendOTP()
            } else {
                checkIdExist()
            }
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
                            sendOTP()
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }



    fun hitRegisterApi() {
        if(isAuction) {
            val registerImageParamModel = RegisterImageParamModel(
                docPhotoFileContent = "",
                docQid1 = "qid1",
                docQid1FileContent = frontImage,
                docQid2 = "qid2",
                docQid2FileContent = backImage,
            )

            val registerParamModel = RegisterParamModel(
                userQid = dataModel.qid,
                userNationalityCode = dataModel.nationalityCode.toString(),
                userName = name,
                userNameAr = nameAr,
                phoneNumber = if (number.split("-").size >1)  number.split("-")[1] else number,
                userEmail = email,
                userType = 0,
                phoneCode = phoneCode,
                birthDate = dataModel.birthDatDate,
                userDocCollection = registerImageParamModel,
            )

            CoroutinesBase.main {
                setLoadingState(LoadingState.LOADING())
                val loginResponse = registerRepo.hitRegisterApi(registerParamModel)
                updateView(ApiCodes.REGISTER,loginResponse) {
                    when(it) {
                        is API_VIEWMODEL_DATA.API_SUCCEED -> {
                            msg = "";
                            var map: Map<String, Any> = HashMap()
                            map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                            print(map.toString())
                            if(!map.containsKey("userId"))
                            {
                                msg = "Something went wrong"
                            }
                            else {
                                val loginResponseData =
                                    Gson().fromJson(Gson().toJson(it.data), UserTokenModel::class.java)
                                dataTokenMLD.value = loginResponseData
                                storeUserData(loginResponseData)
                            }
                        }
                    }
                }
                setLoadingState(LoadingState.LOADED())
            }
        }
        else
        {
            val registerParamModel = RegisterParamModel(
                userQid = dataModel.qid,
                userNationalityCode = dataModel.nationalityCode.toString(),
                userName = name,
                userNameAr = nameAr,
                phoneNumber = if (number.split("-").size >1)  number.split("-")[1] else number,
                userEmail = email,
                userType = 0,
                phoneCode = phoneCode,
                birthDate = dataModel.birthDatDate,
            )

            CoroutinesBase.main {
                setLoadingState(LoadingState.LOADING())
                val loginResponse = registerRepo.hitRegisterApiForTicketUser(registerParamModel)
                updateView(ApiCodes.REGISTER,loginResponse) {
                    when(it) {
                        is API_VIEWMODEL_DATA.API_SUCCEED -> {
                            msg = "";
                            var map: Map<String, Any> = HashMap()
                            map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                            print(map.toString())
                            if(!map.containsKey("userId"))
                            {
                                msg = "Something went wrong"
                            }
                            else {
                                val loginResponseData =
                                    Gson().fromJson(Gson().toJson(it.data), UserTokenModel::class.java)
                                dataTokenMLD.value = loginResponseData
                                storeUserData(loginResponseData)
                            }
                        }
                    }
                }
                setLoadingState(LoadingState.LOADED())
            }
        }

    }

    fun hitRegisterApiForUnknowUser(){
        if(isAuction) {
            val registerImageParamModel = RegisterImageParamModel(
                docPhotoFileContent = "",
                docQid1 = "qid1",
                docQid1FileContent = frontImage,
                docQid2 = "qid2",
                docQid2FileContent = backImage,
            )

            val registerParamModel = RegisterParamModel(
                userQid = qID,
                userNationalityCode = nationality,
                userName = name,
                userNameAr = nameAr,
                phoneNumber = number,
                userEmail = email,
                userType = 0,
                phoneCode = phoneCode,
                birthDate = dataModel.birthDatDate,
                userDocCollection = registerImageParamModel,
            )

            CoroutinesBase.main {
                setLoadingState(LoadingState.LOADING())
                val loginResponse = registerRepo.hitRegisterApiForUnknowUser(registerParamModel)
                updateView(ApiCodes.REGISTER,loginResponse) {
                    when(it) {
                        is API_VIEWMODEL_DATA.API_SUCCEED -> {
                            msg = "";
                            var map: Map<String, Any> = HashMap()
                            map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                            print(map.toString())
                            if(!map.containsKey("userId"))
                            {
                                msg = "Something went wrong"
                            }
                            else {
                                val loginResponseData =
                                    Gson().fromJson(Gson().toJson(it.data), UserTokenModel::class.java)
                                dataTokenMLD.value = loginResponseData
                                storeUserData(loginResponseData)
                            }
                        }
                    }
                }
                setLoadingState(LoadingState.LOADED())
            }
        }
        else
        {
            val registerParamModel = RegisterParamModel(
                userQid = qID,
                userNationalityCode = nationality,
                userNationality= nationalityName,
                userName = name,
                userNameAr = nameAr,
                phoneNumber = if (number.split("-").size >1)  number.split("-")[1] else number,
                userEmail = email,
                userType = 0,
                phoneCode = phoneCode,
                birthDate = dataModel.birthDatDate,
            )

            CoroutinesBase.main {
                setLoadingState(LoadingState.LOADING())
                val loginResponse = registerRepo.hitRegisterApiForTicketUser(registerParamModel)
                updateView(ApiCodes.REGISTER,loginResponse) {
                    when(it) {
                        is API_VIEWMODEL_DATA.API_SUCCEED -> {
                            msg = "";
                            var map: Map<String, Any> = HashMap()
                            map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                            print(map.toString())
                            if(!map.containsKey("userId"))
                            {
                                msg = "Something went wrong"
                            }
                            else {
                                val loginResponseData =
                                    Gson().fromJson(Gson().toJson(it.data), UserTokenModel::class.java)
                                dataTokenMLD.value = loginResponseData
                                storeUserData(loginResponseData)
                            }
                        }
                    }
                }
                setLoadingState(LoadingState.LOADED())
            }
        }

    }

    private fun sendOTP() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = registerRepo.sendMobileOtpApi(number, phoneCode)
            updateView(ApiCodes.SEND_OTP,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        Log.d("code", map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val loginResponseData =
                                Gson().fromJson(Gson().toJson(it.data), SendOTPModel::class.java)
                            dataOTPMLD.value = loginResponseData
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun validateOTP(context : Context) {
        if (otp.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.otp_empty),
                false
            )
        } else if (otp.length != 6) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.otp_invalid),
                false
            )
        } else {
            mValidationLiveData.value = DataValidation("",
                true
            )
        }
    }

    private fun storeUserData(userData: UserTokenModel) {
        SharedPreferencesManager.put(AppConstants.USER_ID, userData.userId!!)
        SharedPreferencesManager.put(AppConstants.USER_FIRST_NAME, userData.userName!!)
        SharedPreferencesManager.put(AppConstants.USER_FIRST_NAME_AR, userData.userNameAr!!)
        SharedPreferencesManager.put(AppConstants.PHONECODE, userData.phoneCode!!)
        SharedPreferencesManager.put(AppConstants.USER_PHONE, userData.phoneNumber!!)
        SharedPreferencesManager.put(AppConstants.TOKEN, userData.token!!)
        SharedPreferencesManager.put(AppConstants.isInHold, userData.isInHold!!)
        SharedPreferencesManager.put(AppConstants.isPayed, userData.isPayed!!)
        SharedPreferencesManager.put(AppConstants.isValidated, userData.isValidated!!)
        SharedPreferencesManager.put(AppConstants.IS_FOR_ACTION, userData.isForAction!!)
        if(userData.userPaymentLink!= null) {
            SharedPreferencesManager.put(AppConstants.PAY_LINK, userData.userPaymentLink!!)
        }
        if(userData!!.userRoles!!.size>1)
        {
            if(userData!!.userRoles!![0].role!!.name.toString()=="USER" || userData!!.userRoles!![1].role!!.name.toString()=="USER")
                SharedPreferencesManager.put(AppConstants.USER_ROLE,"USER")
            else
                SharedPreferencesManager.put(AppConstants.USER_ROLE, "GEST")
        }
        else {
            SharedPreferencesManager.put(AppConstants.USER_ROLE,
                userData!!.userRoles!![0].role!!.name.toString())
        }
    }




    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    fun getIsdList() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val isdListResponse = registerRepo.getIsdList()
            updateView(ApiCodes.GET_ISD,isdListResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val isdList = Gson().fromJson(Gson().toJson(it.data), ISDCodes::class.java)
                        dataISDMLD.value = isdList
                    } else -> {
                        msg = "Something went wrong"
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getNationalityList(){
        CoroutinesBase.main {
            val countryObj = registerRepo.getNationalityList()
            updateView(ApiCodes.GET_COUNTRY,countryObj) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val countryList = Gson().fromJson(Gson().toJson(it.data), Country::class.java)
                        dataCountryMLD.value = countryList
                    } else -> {
                        msg = "Something went wrong"
                    }
                }
            }
        }
    }

}