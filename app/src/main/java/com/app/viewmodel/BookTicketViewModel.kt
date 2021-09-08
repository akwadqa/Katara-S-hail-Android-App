package com.app.viewmodel

import android.content.Context
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.app.R
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.*
import com.app.model.repos.BookTicketRepo
import com.app.utils.*
import com.app.view.activities.BookTicketActivity
import com.google.gson.Gson

class BookTicketViewModel : BaseViewModel(), Observable {
    private val bookTicketRepo by lazy { BookTicketRepo() }

    var msg : String = ""

    var msgAddTicket : String = ""

    var configMsg : String = ""

    var name : String = ""

    var dateSlot : String = ""

    var timeSlot : String  = ""

    var timeSlotId : String  = ""

    var companion : List<String> = ArrayList()

    val dataMLD = MutableLiveData<OTPModel>()

    val dataConfigMLD = MutableLiveData<TicketConfigMainResponse>()

    val dataSlotMLD = MutableLiveData<TicketDateTimeMainResponse>()


    fun validateCredentials(context : Context) {
        if (name.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.enter_name),
                false
            )
        }
        else if (dateSlot.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.enter_visit_date),
                false
            )
        } else if (timeSlot.isBlank()) {
            mValidationLiveData.value = DataValidation(
                context.getString(R.string.qid_time),
                false
            )
        }
        else {
           addTicket()
        }
    }

     fun addTicket() {
        val bookTicketParamModel = BookTicketParamModel(
            userId = SharedPreferencesManager.getString(AppConstants.USER_ID),
            holderName = name,
            timeSlotId = timeSlotId
        )
         val bookTicketMainParamModel = BookTicketMainParamModel(
             ticket = bookTicketParamModel,
             ticketsHolderNames = companion
         )
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val loginResponse = bookTicketRepo.hitBookTicketApi(bookTicketMainParamModel)
            updateView(ApiCodes.ADD_TICKET,loginResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = ""
                        msgAddTicket = ""
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else
                            msgAddTicket = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                    }
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getTicketConfigApi() {
        CoroutinesBase.main {
//            setLoadingState(LoadingState.LOADING())
             val ticketResponse = bookTicketRepo.getTicketConfigApi()
            updateView(ApiCodes.GET_CONFIG,ticketResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        configMsg = ""
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            configMsg = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))  map["msgAr"].toString() else map["msgEn"].toString()
                        }
                        else {
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                TicketConfigMainResponse::class.java
                            )
                            dataConfigMLD.value = json
                        }
                    }
                }
            }
//            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getTicketDataApi(context: Context) {
        CoroutinesBase.main {
            setLoadingState(LoadingState.LOADING())
            val ticketResponse = bookTicketRepo.getTicketDataApi()
            updateView(ApiCodes.GET_TICKET,ticketResponse) {
                when(it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        msg = "";
                        var map: Map<String, Any> = HashMap()
                        map = Gson().fromJson(Gson().toJson(it.data), map.javaClass)
                        print(map.toString())
                        if(map.containsKey("error") && map["error"] == true)
                        {
                            msg = context.getString(R.string.no_slots)
                        }
                        else {
                            val json = Gson().fromJson(
                                Gson().toJson(it.data),
                                TicketDateTimeMainResponse::class.java
                            )
                            dataSlotMLD.value = json
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