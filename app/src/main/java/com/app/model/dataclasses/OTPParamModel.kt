package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class OTPParamModel(

    @SerializedName("qid")
    var qid: String? = null,

    @SerializedName("otp")
    var otp: String? = null,

    )
