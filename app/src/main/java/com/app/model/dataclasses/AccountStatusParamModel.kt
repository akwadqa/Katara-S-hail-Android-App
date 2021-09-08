package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class AccountStatusParamModel(

    @SerializedName("value")
    var value: String? = null,

    @SerializedName("key")
    var key: String? = null,

    )
