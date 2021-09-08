package com.app.model.api

interface NetworkListener {

    /**
     * This method is called when response ir received with code 200
     * @param responseCode - code of response
     * @param response - response in String
     */
    fun onSuccess(responseCode: Int, response: String)


    /**
     * This method is called when response ir received in the error body
     * @param response - response in th error body
     */
    fun onFailure(response: String)


    /**
     * This method is called when we receive a call in failure
     */
    fun onError(t: Throwable)

}