package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class RegisterParamModel(

    @SerializedName("userQid")
    var userQid: String? = null,

    @SerializedName("userNationalityCode")
    var userNationalityCode: String? = null,

    @SerializedName("userNationality")
    var userNationality: String? = null,

    @SerializedName("userName")
    var userName: String? = null,

    @SerializedName("userNameAr")
    var userNameAr: String? = null,

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,

    @SerializedName("userEmail")
    var userEmail: String? = null,

    @SerializedName("userType")
    var userType: Int? = 0,

    @SerializedName("phoneCode")
    var phoneCode: String? = null,

    @SerializedName("birthDate")
    var birthDate: String? = null,

    @SerializedName("userDocCollection")
    var userDocCollection: RegisterImageParamModel? = null,


)
