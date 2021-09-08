package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class LoginParamModel(

    @SerializedName("qid")
    var qid: String? = null,

    @SerializedName("password")
    var password: String? = null,

    )
