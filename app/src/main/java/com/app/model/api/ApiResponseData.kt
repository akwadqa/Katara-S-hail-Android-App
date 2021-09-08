package com.app.model.api

import com.app.model.dataclasses.ApiError

/**
 * Sealed class to observe changes on [android.app.Activity]
 *
 */
sealed class ApiResponseData {
    /**
     * contains data which comes on success call
     *
     * @property statusCode code to identify weather api succeed or fail
     * @property apiCode code to identify the api which is called based on [com.app.core.util.ApiCodes]
     * @property msg any error or success message
     */
    class API_SUCCEED(val apiCode: Int) : ApiResponseData()

    /**
     * called when there is any API error
     */
    class API_TOKEN_EXPIRED() : ApiResponseData()

    /**
     *called when there is no internet connection
     */
    class API_EXCEPTION() : ApiResponseData()
}

/**
 * Sealed class to observe changes on [BaseViewModel]
 *
 */
sealed class API_VIEWMODEL_DATA {
    /**
     * contains data which comes on success call
     *
     * @property data any contained data on success callback
     * @property apiCode code to identify the api which is called based on [com.app.core.util.ApiCodes]
     */
    class API_SUCCEED(val data: Any?, val apiCode: Int) : API_VIEWMODEL_DATA()

    /**
     *called when there is any API error
     */
    class API_TOKEN_EXPIRED() : API_VIEWMODEL_DATA()

    /**
     * called when there is no internet connection
     */
    class API_EXCEPTION() : API_VIEWMODEL_DATA()
}
