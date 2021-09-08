package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class OTPModel(

    @SerializedName(AppConstants.ERROR)
    var error: Boolean? = false,

    @SerializedName("otp")
    var otp: String? = null,

    @SerializedName("msgEn")
    var msgEn: String? = null,

    @SerializedName("msgAr")
    var msgAr: String? = null,

    @SerializedName("number")
    var number: String? = null,

    )
