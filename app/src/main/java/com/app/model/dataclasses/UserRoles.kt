package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRoles {
    @SerializedName("userRoleId")
    @Expose
    var userRoleId: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("roleId")
    @Expose
    var roleId: String? = null

    @SerializedName("role")
    @Expose
    var role: Role? = null
}