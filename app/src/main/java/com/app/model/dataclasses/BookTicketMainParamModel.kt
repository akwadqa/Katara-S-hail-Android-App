package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.util.*

data class BookTicketMainParamModel(

    @SerializedName("ticket")
    var ticket: BookTicketParamModel? = null,

    @SerializedName("ticketsHolderNames")
    var ticketsHolderNames: List<String>? = null,

    )
