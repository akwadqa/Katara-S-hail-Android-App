package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.AuctionRepo
import com.app.model.repos.RegisterRepo
import com.app.utils.*
import com.google.gson.Gson

class OtherViewModel : BaseViewModel(), Observable {
    private val registerRepo by lazy { RegisterRepo() }
    private val auctionRepo by lazy { AuctionRepo() }

    var msg: String = ""

    val dataMLD = MutableLiveData<ForceUpdateResponse?>()
    val auctionIsForQatariData = MutableLiveData<GeneralResponseModel>()

    fun hitLogin() {
        val loginParamModel = LoginParamModel(
            qid = AppConstants.QID,
            password = AppConstants.PASSWORD)

        CoroutinesBase.main {
//            setLoadingState(LoadingState.LOADING())
            val loginResponse = registerRepo.hitLogin(loginParamModel)
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
//            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getAuctionIsForQatari() {
        CoroutinesBase.main {
            val response = auctionRepo.getAuctionIsForQatari()
            updateView(ApiCodes.GET_AUCTION_IS_FOR_QATARI,response) {
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
                            val data =
                                Gson().fromJson(Gson().toJson(it.data), GeneralResponseModel::class.java)
                            auctionIsForQatariData.value = data
                        }
                    }
                }
            }
        }
    }


    fun checkForceUpdate() {
        CoroutinesBase.main {
            val userId = SharedPreferencesManager.getString(AppConstants.USER_ID)
            val ticketResponse = registerRepo.checkForceUpdate()
            updateView(ApiCodes.FORCE_UPDATE,ticketResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            ForceUpdateResponse::class.java
                        )
                        dataMLD.value = json
                    }
                }
            }
        }
    }


    private fun storeUserData(userData: UserTokenModel) {
        SharedPreferencesManager.put(AppConstants.TOKEN, userData.token!!)
        SharedPreferencesManager.put(AppConstants.USER_ROLE, "ADMIN")
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

}