package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.HomeRepo
import com.app.utils.*
import com.google.gson.Gson

class HomeViewModel : BaseViewModel(), Observable {
    private val homeRepo by lazy { HomeRepo() }

    val dataNewsMLD = MutableLiveData<List<NewsListResponse?>>()
    val dataBannersMLD = MutableLiveData<BannerMainResponse?>()
    val dataFalconCategoryMLD = MutableLiveData<FalconCategoryMainResponse?>()
    val dataFalconMLD = MutableLiveData<FalconMainResponse?>()
    val upgradeResponseData = MutableLiveData<GeneralResponseModel?>()
    val deleteResponseData = MutableLiveData<GeneralResponseModel?>()

    var msg: String = ""

    fun getNewsList() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = homeRepo.getNewsList()
            updateView(ApiCodes.GET_NEWS,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            Array<NewsListResponse>::class.java
                        ).toList()
                        dataNewsMLD.value = json
                    }
                }
            }
        }
    }

    fun getBannerList() {
        CoroutinesBase.main {
            val homeResponse = homeRepo.getBannerList()
            updateView(ApiCodes.GET_BANNER,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            BannerMainResponse::class.java
                        )
                        dataBannersMLD.value = json
                    }
                }
            }
        }
    }

    fun getCategoryList() {
        CoroutinesBase.main {
            val homeResponse = homeRepo.getFalconCategory()
            updateView(ApiCodes.GET_FALCON_CATEGORY,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            FalconCategoryMainResponse::class.java
                        )
                        dataFalconCategoryMLD.value = json
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getFalconList(falconCategoryId: String?) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = homeRepo.getFalconList(falconCategoryId)
            updateView(ApiCodes.GET_FALCON,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            FalconMainResponse::class.java
                        )
                        dataFalconMLD.value = json
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun requestAccountUpgrade(userId: String) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val response = homeRepo.requestAccountUpgrade(userId)
            updateView(ApiCodes.REQUEST_ACCOUNT_UPGRADE,response) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            GeneralResponseModel::class.java
                        )
                        upgradeResponseData.value = json
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun requestDeleteAccount(userId: String) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val response = homeRepo.requestDeleteAccount(userId)
            updateView(ApiCodes.REQUEST_DELETE_ACCOUNT,response) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            GeneralResponseModel::class.java
                        )
                        deleteResponseData.value = json
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