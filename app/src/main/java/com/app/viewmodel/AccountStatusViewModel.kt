package com.app.viewmodel

import androidx.databinding.Observable
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.AccountStatusRepo
import com.app.utils.*
import com.google.gson.Gson

class AccountStatusViewModel : BaseViewModel(), Observable {
    private val repo by lazy { AccountStatusRepo() }

    var msg: String = ""

    fun hitRefresh() {
        val loginParamModel = AccountStatusParamModel(
            value = "refresh",
            key = SharedPreferencesManager.getString(AppConstants.USER_ID))

        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = repo.hitRefreshUser(loginParamModel)
            updateView(ApiCodes.LOGIN,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = ""
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
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
        SharedPreferencesManager.put(AppConstants.PAY_LINK, userData.userPaymentLink!!)
        SharedPreferencesManager.put(AppConstants.IS_FOR_ACTION, userData.isForAction!!)
        SharedPreferencesManager.put(AppConstants.LANGUAGE, SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE))
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