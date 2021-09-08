package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.HomeRepo
import com.app.model.repos.ProductRepo
import com.app.utils.*
import com.google.gson.Gson

class ProductViewModel : BaseViewModel(), Observable {
    private val productRepo by lazy { ProductRepo() }

    val dataProductMLD = MutableLiveData<List<ProductListResponse?>>()
    val dataShopMLD = MutableLiveData<List<ShopListResponse?>>()
    val dataShopDetailMLD = MutableLiveData<ShopDetailResponse?>()

    fun getProductList() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = productRepo.getProductList()
            updateView(ApiCodes.GET_PRODUCT,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            Array<ProductListResponse>::class.java
                        ).toList()
                        dataProductMLD.value = json
                    }
                }
            }
        }
    }

    fun getShopList() {
        CoroutinesBase.main {
            val homeResponse = productRepo.getShopList()
            updateView(ApiCodes.GET_SHOP,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            Array<ShopListResponse>::class.java
                        ).toList()
                        dataShopMLD.value = json
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getShopDetails(shopId: String?) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val homeResponse = productRepo.getShopDetails(shopId)
            updateView(ApiCodes.GET_SHOP_DETAILS,homeResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            ShopDetailResponse::class.java
                        )
                        dataShopDetailMLD.value = json
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