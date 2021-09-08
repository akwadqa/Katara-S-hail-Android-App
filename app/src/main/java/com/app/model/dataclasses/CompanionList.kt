package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CompanionList {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("isAdd")
    @Expose
    var isAdd: Boolean? = false


}