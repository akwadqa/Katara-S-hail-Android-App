package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BannerListResponse {
    @SerializedName("appBannerId")
    @Expose
    var appBannerId: String? = null

    @SerializedName("mainBannerLink")
    @Expose
    var mainBannerLink: String? = null

    @SerializedName("banner01")
    @Expose
    var banner01: Any? = null

    @SerializedName("banner02")
    @Expose
    var banner02: Any? = null

    @SerializedName("banner03")
    @Expose
    var banner03: Any? = null

    @SerializedName("banner04")
    @Expose
    var banner04: Any? = null

    @SerializedName("banner05")
    @Expose
    var banner05: Any? = null

    @SerializedName("banner06")
    @Expose
    var banner06: Any? = null

    @SerializedName("banner07")
    @Expose
    var banner07: Any? = null

    @SerializedName("banner08")
    @Expose
    var banner08: Any? = null

    @SerializedName("banner09")
    @Expose
    var banner09: Any? = null

    @SerializedName("banner10")
    @Expose
    var banner10: Any? = null
}