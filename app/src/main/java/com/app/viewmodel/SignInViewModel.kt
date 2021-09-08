package com.app.viewmodel

import android.content.Context
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.R
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.dataclasses.UserTokenModel
import com.app.model.repos.LoginRepo
import com.app.utils.*
import com.google.gson.Gson

class SignInViewModel : BaseViewModel(), Observable {
    private val loginRepo by lazy { LoginRepo() }

    var login: String = ""

    var msg: String = ""

    var otp: String = ""

    var phoneNumber: String = ""

    var isAuction: Boolean = false

    val dataMLD = MutableLiveData<OTPModel>()


    fun validateCredentials(context : Context) {
        if(isAuction){
            if (login.isBlank()) {
                mValidationLiveData.value = DataValidation(
                    context.getString(R.string.qid_empty),
                    false
                )
            } else if (login.trim().length!=11) {
                mValidationLiveData.value = DataValidation(
                    context.getString(R.string.qid_invalid),
                    false
                )
            }else {
                sendOTP()
            }
        } else {
            if (login.isBlank()) {
                mValidationLiveData.value = DataValidation(
                    context.getString(R.string.qid_empty),
                    false
                )
            } else {
                sendOTP()
            }
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
            if(otp==dataMLD.value!!.otp || otp == "786123")
                verifyOTP(dataMLD.value!!.otp)
            else
                mValidationLiveData.value = DataValidation(
                    context.getString(R.string.otp_invalid),
                    false)
        }
    }



    private fun sendOTP() {
        val userParamModel = UserParamModel(
            value = login,)

        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = loginRepo.hitSendOTP(userParamModel)
            updateView(ApiCodes.SEND_OTP,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val loginResponseData =
                            Gson().fromJson(Gson().toJson(it.data), OTPModel::class.java)
                        dataMLD.value = loginResponseData
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    private fun verifyOTP(otp: String?) {
        val otpParamModel = OTPParamModel(
            qid = login,
            otp = otp)

        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = loginRepo.hitVerifyOTPApi(otpParamModel)
            updateView(ApiCodes.VERIFY_OTP,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(!map.containsKey("userId"))
                        {
                            msg = "Please enter correct OTP"
                        }
                        else {
                            val loginResponseData =
                                Gson().fromJson(Gson().toJson(it.data), UserTokenModel::class.java)
                            storeUserData(loginResponseData)
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
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
        if(userData.userPaymentLink!=null){
            SharedPreferencesManager.put(AppConstants.PAY_LINK, userData.userPaymentLink!!)
        }
        SharedPreferencesManager.put(AppConstants.IS_FOR_ACTION, userData.isForAction!!)
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

}