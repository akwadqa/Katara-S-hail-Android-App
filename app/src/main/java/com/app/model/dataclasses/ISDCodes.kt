package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ISDCodes {
    @SerializedName("option")
    @Expose
    var option : ArrayList<Option> ? = null
}

class Option {
    @SerializedName("value")
    @Expose
   var value : String ? = null
}