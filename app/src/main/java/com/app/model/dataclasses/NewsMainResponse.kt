package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewsMainResponse(

    @SerializedName("responce")
    @Expose
    var responce: List<NewsListResponse>? = null

) : Serializable