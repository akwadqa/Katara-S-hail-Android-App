package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TicketListResponse {
    @SerializedName("ticketsId")
    @Expose
    var ticketsId: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("ticketDate")
    @Expose
    var ticketDate: String? = null

    @SerializedName("validTicket")
    @Expose
    var validTicket: Boolean? = null

    @SerializedName("checkIn")
    @Expose
    var checkIn: Boolean? = null

    @SerializedName("checkOut")
    @Expose
    var checkOut: Boolean? = null

    @SerializedName("checkInDate")
    @Expose
    var checkInDate: String? = null

    @SerializedName("checkOutDate")
    @Expose
    var checkOutDate: String? = null

    @SerializedName("qrCodeLink")
    @Expose
    var qrCodeLink: String? = null

    @SerializedName("downloadLink")
    @Expose
    var downloadLink: String? = null

    @SerializedName("holderName")
    @Expose
    var holderName: String? = null

    @SerializedName("timeSlot")
    @Expose
    var timeSlot: TicketSlotsResponse? = null
}