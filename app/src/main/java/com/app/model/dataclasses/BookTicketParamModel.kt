package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName
import java.util.*

data class BookTicketParamModel(

    @SerializedName("userId")
    var userId: String? = "",

    @SerializedName("holderName")
    var holderName: String? = "",

    @SerializedName("timeSlotId")
    var timeSlotId: String? = "",

    )
