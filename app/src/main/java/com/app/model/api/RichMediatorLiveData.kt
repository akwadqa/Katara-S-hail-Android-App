package com.app.model.api

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.app.model.dataclasses.FailureResponse

abstract class RichMediatorLiveData<T> : MediatorLiveData<T>() {

    private var errorLiveData: MutableLiveData<Throwable>? = null
    private var failureResponseLiveData: MutableLiveData<FailureResponse>? = null

    protected abstract val failureObserver: Observer<FailureResponse>

    protected abstract val errorObserver: Observer<Throwable>

    private fun initLiveData() {
        errorLiveData = MutableLiveData()
        failureResponseLiveData = MutableLiveData()
    }

    override fun onInactive() {
        super.onInactive()
        removeSource(failureResponseLiveData!!)
        removeSource(errorLiveData!!)
    }

    override fun onActive() {
        super.onActive()
        initLiveData()
        addSource(failureResponseLiveData!!, failureObserver)
        addSource(errorLiveData!!, errorObserver)
    }

    fun setFailure(failureResponse: FailureResponse) {
        failureResponseLiveData!!.value = failureResponse
    }

    fun setError(t: Throwable) {
        errorLiveData!!.value = t
    }
}