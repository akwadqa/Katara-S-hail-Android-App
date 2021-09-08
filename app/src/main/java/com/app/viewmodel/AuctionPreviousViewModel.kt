package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.AuctionRepo
import com.app.model.repos.HomeRepo
import com.app.utils.*
import com.google.gson.Gson

class AuctionPreviousViewModel : BaseViewModel(), Observable {
    private val auctionRepo by lazy { AuctionRepo() }

    val dataFalconMLD = MutableLiveData<FalconMainResponse?>()

    var msg: String = ""

    fun getPreviousAuctionList() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = auctionRepo.getPreviousAuctionList()
            updateView(ApiCodes.GET_FALCON,homeResponse) {
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
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                FalconMainResponse::class.java
                            )
                            dataFalconMLD.value = json
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