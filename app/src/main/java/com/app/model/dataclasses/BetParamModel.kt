package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class BetParamModel(

    @SerializedName("userId")
    var userId: String? = null,

    @SerializedName("falconId")
    var falconId: String? = null,

    @SerializedName("amount")
    var amount: Int? = 0,

    )
