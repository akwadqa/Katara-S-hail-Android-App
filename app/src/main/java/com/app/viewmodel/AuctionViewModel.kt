package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.AuctionRepo
import com.app.utils.*
import com.google.gson.Gson

class AuctionViewModel : BaseViewModel(), Observable {
    private val auctionRepo by lazy { AuctionRepo() }

    val dataAuction10MLD = MutableLiveData<FalconPreviousResponse?>()
    val dataAuctionMLD = MutableLiveData<FalconListResponse?>()
    val dataLastBetMLD = MutableLiveData<AuctionLastBetMainResponse?>()
    val dataMaxPriceMLD = MutableLiveData<AuctionMaxPriceMainResponse?>()

    var msg: String = ""

    fun getAuctionDetailsList(falconId: String?) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getAuctionDetails(falconId)
            updateView(ApiCodes.GET_AUCTION,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            FalconListResponse::class.java
                        )
                        dataAuctionMLD.value = json
                    }
                }
            }
//            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getAuctionPreviousDetailsList(falconId: String?) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getAuctionPreviousDetails(falconId)
            updateView(ApiCodes.GET_AUCTION,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            FalconListResponse::class.java
                        )
                        dataAuctionMLD.value = json
                    }
                }
            }
//            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getAuction10List(falconId: String?) {
        CoroutinesBase.main {
//            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getAuction10List(falconId)
//            val homeResponse = auctionRepo.getAuction10List("ad1a000d-7736-4891-8317-3410fb5ef039")
            updateView(ApiCodes.GET_LAST_10,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                FalconPreviousResponse::class.java
                            )
                            dataAuction10MLD.value = json
                        }
                    }
                }
            }
//            setLoadingState(LoadingState.LOADED())
        }
    }

    /*fun getLastBet() {
        CoroutinesBase.main {
//            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getLastBet()
            updateView(ApiCodes.GET_MAX_PRICE,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                AuctionLastBetMainResponse::class.java
                            )
                            dataMaxPriceMLD.value = json
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }*/

    fun getAuctionMaxPrice(falconId: String) {
        CoroutinesBase.main {
//            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getAuctionMaxPrice(falconId)
            updateView(ApiCodes.GET_MAX_PRICE,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                AuctionMaxPriceMainResponse::class.java
                            )
                            dataMaxPriceMLD.value = json
                        }
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun hitMakeBet(falconId : String, amount : Int) {
        val betParamModel = BetParamModel(
            userId = SharedPreferencesManager.getString(AppConstants.USER_ID),
            falconId = falconId,
            amount = amount
        )

        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = auctionRepo.hitMakeBet(betParamModel)
            updateView(ApiCodes.MAKE_BET,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
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