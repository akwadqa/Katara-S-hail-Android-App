package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TicketConfigResponse {
    @SerializedName("session1Name")
    @Expose
    var session1Name: String? = null

    @SerializedName("session2Name")
    @Expose
    var session2Name: String? = null

    @SerializedName("session3Name")
    @Expose
    var session3Name: String? = null

    @SerializedName("maxTicketNumberForReservation")
    @Expose
    var maxTicketNumberForReservation: Int? = 0

    @SerializedName("maxTicketNumberPerSession")
    @Expose
    var maxTicketNumberPerSession: Int? = 0

    @SerializedName("maxTicketNumberPerDayForUser")
    @Expose
    var maxTicketNumberPerDayForUser: Int? = 0
}