package com.app.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.BaseApplication
import com.app.R
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.dataclasses.UserTokenModel
import com.app.model.repos.LoginRepo
import com.app.model.repos.TicketRepo
import com.app.utils.*
import com.google.gson.Gson

class TicketViewModel : BaseViewModel(), Observable {
    private val ticketRepo by lazy { TicketRepo() }

    val dataMLD = MutableLiveData<TicketMainResponse?>()

    var msg: String = ""


    fun getTicketList() {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val userId = SharedPreferencesManager.getString(AppConstants.USER_ID)
            val ticketResponse = ticketRepo.getTicketList(userId)
//            val ticketResponse = ticketRepo.getTicketList("1cdb2b80-dcad-4291-8730-6495a4617a15")
            updateView(ApiCodes.GET_TICKET,ticketResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        val json = Gson().fromJson(
                            Gson().toJson(it.data),
                            TicketMainResponse::class.java
                        )
                        dataMLD.value = json

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