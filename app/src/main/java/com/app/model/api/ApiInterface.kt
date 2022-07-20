package com.app.model.api

import com.app.model.api.constants.ApiEndPoints
import com.app.model.dataclasses.*
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.SEND_OTP)
    suspend fun hitSendOTP(@Body userData: UserParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.VERIFY_OTP)
    suspend fun hitVerifyOTPApi(@Body userData: OTPParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.EXTRACT_QID)
    suspend fun hitExtractQID(@Body userData: QIDExtractParamModel ): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.LOGIN)
    suspend fun hitLogin(@Body userData: LoginParamModel): Response<Any>

    @GET(ApiEndPoints.CHECK_QID + "/{qid}")
    suspend fun checkQID(@Path("qid") qid : String ): Response<Any>

    @GET(ApiEndPoints.SEND_MOBILE_OTP)
    suspend fun sendMobileOtpApi( @Query("phone", encoded = true) phone : String): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REGISTER)
    suspend fun hitRegisterApi(@Body userData: RegisterParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REGISTER_UNKNOWN_USER)
    suspend fun hitRegisterApiForUnknowUser(@Body userData: RegisterParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REGISTER_TICKET_USER)
    suspend fun hitRegisterApiForTicketUser(@Body userData: RegisterParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REGISTER_NONE_QATARI_USER)
    suspend fun hitRegisterApiForNoneQatariUser(@Body userData: RegisterParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REQUEST_ACCOUNT_UPGRADE)
    suspend fun requestAccountUpgrade(@Body params: AccountStatusParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REQUEST_DELETE_ACCOUNT)
    suspend fun requestDeleteAccount(@Body params: AccountStatusParamModel): Response<Any>

    @GET(ApiEndPoints.GET_NEWS)
    suspend fun getNewsApi(): Response<Any>

    @GET(ApiEndPoints.GET_BANNER)
    suspend fun getBannerApi(): Response<Any>

    @GET(ApiEndPoints.GET_FALCON_CATEGORY)
    suspend fun getFalconCategoryApi( ): Response<Any>

    @GET(ApiEndPoints.GET_FALCON + "/{falconCategoryId}")
    suspend fun getFalconApi(@Path("falconCategoryId") falconCategoryId: String?): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_EXPIRED )
    suspend fun getPreviousAuctionList(): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_10 + "/{falcon_id}")
    suspend fun getAuction10List(@Path("falcon_id") falcon_id: String?): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_LAST_BET )
    suspend fun getLastBet(): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_DETAILS  + "/{falcon_ID}")
    suspend fun getAuctionDetails(@Path("falcon_ID") falcon_id: String?): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_PREVIOUS_DETAILS  + "/{falcon_ID}")
    suspend fun getAuctionPreviousDetails(@Path("falcon_ID") falcon_id: String?): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_MAX_PRICE + "/{falcon_id}")
    suspend fun getAuctionMaxPrice(@Path("falcon_id") falcon_id: String?): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.GET_AUCTION_MAKE_BET)
    suspend fun hitMakeBetApi(@Body betModel: BetParamModel): Response<Any>

    @GET(ApiEndPoints.GET_TICKET_CONFIG)
    suspend fun getTicketConfigApi(): Response<Any>

    @GET(ApiEndPoints.GET_TICKET_DATA)
    suspend fun getTicketDataApi(): Response<Any>

    @GET(ApiEndPoints.GET_TICKET+ "/{userId}")
    suspend fun getTicketListApi( @Path("userId") userId : String ): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.BOOK_TICKET)
    suspend fun hitBookTicketApi(@Body userData: BookTicketMainParamModel): Response<Any>

    @Headers("Content-Type: application/json")
    @POST(ApiEndPoints.REFRESH_USER)
    suspend fun hitRefreshUser(@Body userData: AccountStatusParamModel): Response<Any>

    @GET(ApiEndPoints.FORCE_UPDATE)
    suspend fun checkForceUpdate(): Response<Any>

    @GET(ApiEndPoints.SHOP)
    suspend fun getShopList(): Response<Any>

    @GET(ApiEndPoints.PRODUCT)
    suspend fun getProductList(): Response<Any>

    @GET(ApiEndPoints.SHOP_DETAILS+ "/{shopId}")
    suspend fun getShopDetailsApi( @Path("shopId") shopId: String?): Response<Any>

    @GET(ApiEndPoints.GET_ISD)
    suspend fun getIsdList(): Response<Any>

    @GET(ApiEndPoints.GET_COUNTRIES)
    suspend fun getNationalityList(): Response<Any>

    @GET(ApiEndPoints.GET_AUCTION_IS_FOR_QATARI)
    suspend fun getAuctionIsForQatar(): Response<Any>
}