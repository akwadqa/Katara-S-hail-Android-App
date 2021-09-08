package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ShopDetailResponse : Serializable {
    @SerializedName("shopId")
    @Expose
    var shopId: String? = null

    @SerializedName("shopName")
    @Expose
    var shopName: String? = null

    @SerializedName("shopDescription")
    @Expose
    var shopDescription: String? = null

    @SerializedName("shopLocation")
    @Expose
    var shopLocation: String? = null

    @SerializedName("shopCompany")
    @Expose
    var shopCompany: String? = null

    @SerializedName("shopNameAr")
    @Expose
    var shopNameAr: String? = null

    @SerializedName("shopDescriptionAr")
    @Expose
    var shopDescriptionAr: String? = null

    @SerializedName("shopLocationAr")
    @Expose
    var shopLocationAr: String? = null

    @SerializedName("shopCompanyAr")
    @Expose
    var shopCompanyAr: String? = null

    @SerializedName("docImage")
    @Expose
    var docImage: String? = null

    @SerializedName("docImageContent")
    @Expose
    var docImageContent: Any? = null

    @SerializedName("shopEmail")
    @Expose
    var shopEmail: String? = null

    @SerializedName("shopPhone")
    @Expose
    var shopPhone: String? = null

    @SerializedName("shopManager")
    @Expose
    var shopManager: String? = null

    @SerializedName("products")
    @Expose
    var products: List<ProductList>? = null
}