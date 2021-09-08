package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class Country {
    @SerializedName("option2")
    @Expose
    var option2 : ArrayList<Option2> ? = null
}

public class Option2 {
    @SerializedName("value")
    @Expose
    var value : String ? = null


    @SerializedName("trad")
    @Expose
    var trad : String ? = null
}