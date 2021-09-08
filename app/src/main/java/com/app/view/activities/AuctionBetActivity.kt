package com.app.view.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.app.R
import com.app.databinding.ActivityAuctionBinding
import com.app.model.dataclasses.*
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.app.view.adaptors.AuctionImageAdapter
import com.app.view.adaptors.AuctionPreviousAdapter
import com.app.viewmodel.AuctionViewModel
import com.zhpan.indicator.enums.IndicatorOrientation
import java.text.SimpleDateFormat
import java.util.*


class AuctionBetActivity : BaseActivity(), View.OnClickListener,
    AuctionPreviousAdapter.OnItemClickListener {

    private val auctionViewModel by lazy { ViewModelProvider(this).get(AuctionViewModel::class.java) }
    private lateinit var auctionBinding: ActivityAuctionBinding
    private var falconId : String= ""
    private var maxPrice : Int = 0
    private var isOn : Boolean = true
    private lateinit var auctionAdapter: AuctionPreviousAdapter
    private lateinit var imageAdapter: AuctionImageAdapter
    private lateinit var addBetDialog : Dialog
    private lateinit var confirmBetDialog : Dialog
    lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auctionBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction)
        setBaseViewModel(auctionViewModel)
        setObservers()
        setListeners()
        getIntentData()
//        hitAllApis()
        mainHandler = Handler(Looper.getMainLooper())
    }

    private fun hitAllApis() {
        auctionViewModel.getAuctionDetailsList(falconId)
        auctionViewModel.getAuction10List(falconId)
        auctionViewModel.getAuctionMaxPrice(falconId)
    }

    private fun getIntentData() {
        falconId = intent.getStringExtra("falconId").toString()
        maxPrice = intent.getIntExtra("maxPrice",0)
    }

    private fun setListeners() {
        auctionBinding.ivBack.setOnClickListener(this)
        auctionBinding.ivAdd.setOnClickListener(this)
        auctionBinding.ivNext.setOnClickListener(this)
        auctionBinding.ivPrevious.setOnClickListener(this)
        auctionBinding.ivRefresh.setOnClickListener(this)
    }

    private fun setObservers() {
        auctionViewModel.getResponseObserver().observe(this, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            auctionBinding.ivBack.id -> {
                finish()
            }
            auctionBinding.ivAdd.id -> {
                showAddBetDialog()
            }
            auctionBinding.ivNext.id -> {
                val pos = auctionBinding.viewPager.currentItem
                if(pos < (auctionViewModel.dataAuctionMLD.value!!.falconImageMobiles!!.size - 1) )
                    auctionBinding.viewPager.currentItem = pos + 1
            }
            auctionBinding.ivPrevious.id -> {
                val pos = auctionBinding.viewPager.currentItem
                if(pos > 0 )
                    auctionBinding.viewPager.currentItem = pos - 1
            }
            auctionBinding.ivRefresh.id -> {
                if(isOn)
                {
                    isOn = false
                    auctionBinding.ivRefresh.setImageDrawable(resources.getDrawable(R.drawable.auto_refresh_off))
                }
                else
                {
                    mainHandler.post(updateTextTask)
                    isOn = true
                    auctionBinding.ivRefresh.setImageDrawable(resources.getDrawable(R.drawable.auto_refresh_on))
                }
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_AUCTION -> {
                setAuctionDetails(auctionViewModel.dataAuctionMLD)
            }
            ApiCodes.GET_MAX_PRICE -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    setMaxPriceData(auctionViewModel.dataMaxPriceMLD.value)
                }
            }
            ApiCodes.GET_LAST_10 -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    setFalconAdaptor(auctionViewModel.dataAuction10MLD.value)
                }
            }
            ApiCodes.MAKE_BET -> {
                if(auctionViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, auctionViewModel.msg, Toast.LENGTH_SHORT).show()
                }
                else {
                    confirmBetDialog.dismiss()
                    addBetDialog.dismiss()
                    hitAllApis()
                }
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun setAuctionDetails(dataAuctionMLD: MutableLiveData<FalconListResponse?>) {
        maxPrice = dataAuctionMLD.value!!.falconMaxPrice!!
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvName.text = dataAuctionMLD.value!!.falconName
        else
            auctionBinding.tvName.text = dataAuctionMLD.value!!.falconNameEn
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvDescription.text = dataAuctionMLD.value!!.falconDescription
        else
            auctionBinding.tvDescription.text = dataAuctionMLD.value!!.falconDescriptionEn
        auctionBinding.tvDate.text = dataAuctionMLD.value!!.falconBirth!!.split("T")[0]
        setBannerAdaptor(dataAuctionMLD.value!!.falconImageMobiles)

        var spf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH)
        val date1: Date = spf.parse(dataAuctionMLD.value!!.limitAucctionTime)
        val date2: Date = Calendar.getInstance().time
        val millis: Long = date1.time - date2.time

        val hours: Long = millis / (1000 * 60 * 60)
        val mins: Long = millis / (1000 * 60) % 60
        val sec: Long = millis / (1000) % 60
        val diff = Date()
        diff.hours = hours.toInt()
        diff.minutes = mins.toInt()
        diff.seconds = sec.toInt()
        val diffTime = SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(diff)
        auctionBinding.tvEndTime.text = diffTime

        val calendar = Calendar.getInstance()
        calendar.time = date1
        calendar.add(Calendar.SECOND, 1)

        val currentTime = SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(calendar.time)
        auctionBinding.tvTime.text = currentTime

        object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
                    val hours: Long = millisUntilFinished / (1000 * 60 * 60)
                    val mins: Long = millisUntilFinished / (1000 * 60) % 60
                    val sec: Long = millisUntilFinished / (1000) % 60
                    val diff = "$hours:$mins:$sec"
                    auctionBinding.tvEndTime.text = diff
            }

            override fun onFinish() {
            }
        }.start()

    }

    private fun setBannerAdaptor(value: List<FalconImageResponse>?)
    {
        var sortedList = value!!.sortedWith(compareBy { it.docOrder })

        auctionBinding.viewPager.apply {
            imageAdapter = AuctionImageAdapter(this@AuctionBetActivity, sortedList)
            adapter = imageAdapter
            offscreenPageLimit = 1
        }
        var myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (imageAdapter != null) {
                    imageAdapter.pauseVideo()
                }
            }
        }
        auctionBinding.viewPager.registerOnPageChangeCallback(myPageChangeCallback)
        auctionBinding.indicatorView.apply {
            setSliderHeight(10F)
            setupWithViewPager(auctionBinding.viewPager)
            if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                setOrientation(IndicatorOrientation.INDICATOR_RTL)
        }
        if(value!!.size>1)
        {
            auctionBinding.ivNext.visibility = View.VISIBLE
            auctionBinding.ivPrevious.visibility = View.VISIBLE
        }
        else
        {
            auctionBinding.ivNext.visibility = View.GONE
            auctionBinding.ivPrevious.visibility = View.GONE
        }
    }

    private fun setMaxPriceData(value: AuctionMaxPriceMainResponse?) {
        auctionBinding.tvPrice.text = value!!.responce!!.maxPrice.toString()
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            auctionBinding.tvUserName.text = value.responce!!.memberNameAr
        else
            auctionBinding.tvUserName.text = value.responce!!.memberNameEn
    }

    private fun setFalconAdaptor(value: FalconPreviousResponse?)
    {
        auctionBinding.tvNoData.visibility = View.VISIBLE
        if(value!!.responce!!.isNotEmpty()) {
            auctionBinding.tvNoData.visibility = View.GONE
            auctionAdapter = AuctionPreviousAdapter(this, value!!.responce, this)
            auctionBinding.rvAuction.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            auctionBinding.rvAuction.adapter = auctionAdapter
            auctionBinding.rvAuction.isNestedScrollingEnabled = false
        }
    }

    override fun onItemClick(position: Int) {

    }

    private fun showAddBetDialog() {
        addBetDialog = Dialog(this,R.style.yourCustomDialog)
        addBetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        addBetDialog.setCancelable(false)
        addBetDialog.setCanceledOnTouchOutside(false)
        addBetDialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addBetDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.semi_transparent)))
        addBetDialog.window!!.setGravity(Gravity.BOTTOM)
        addBetDialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        addBetDialog.setContentView(R.layout.dialog_add_bet)
        val tvName = addBetDialog.findViewById(R.id.tv_name) as TextView
        val tvPrice = addBetDialog.findViewById(R.id.tv_price) as TextView
        val tvMinBet = addBetDialog.findViewById(R.id.tv_min_bet) as TextView
        val etPrice = addBetDialog.findViewById(R.id.et_price) as EditText
        val tvSubmit = addBetDialog.findViewById(R.id.tv_submit) as TextView

        var minBet = auctionViewModel.dataAuctionMLD.value!!.minimalBet
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR)
        else
            tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)

        val finalBet = maxPrice + minBet!!
        tvMinBet.text = getString(R.string.min_bet)+ " " + finalBet
        tvPrice.text = finalBet.toString()

        etPrice.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            etPrice.post(Runnable {
                val inputMethodManager =
                    this@AuctionBetActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(etPrice, InputMethodManager.SHOW_IMPLICIT)
            })
        }
        etPrice.requestFocus()


        tvSubmit.setOnClickListener(this)

        tvSubmit.setOnClickListener {
            var price = etPrice.text.toString()

            when {
                price.isNullOrBlank() -> showToastLong(getString(R.string.pep))
                price.toInt() < finalBet -> showToastLong(getString(R.string.min_bet)+ " " + finalBet)
                else -> showConfirmBetDialog(price)
            }
        }
        addBetDialog.show()
        addBetDialog.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action === KeyEvent.ACTION_UP) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun showConfirmBetDialog( price : String) {
        confirmBetDialog = Dialog(this,R.style.yourDialog)
        confirmBetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        confirmBetDialog.setCancelable(false)
        confirmBetDialog.setCanceledOnTouchOutside(false)
        confirmBetDialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        confirmBetDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.semi_transparent)))
        confirmBetDialog.window!!.setGravity(Gravity.CENTER)
        confirmBetDialog.setContentView(R.layout.dialog_confirm_bet)
        val tvName = confirmBetDialog.findViewById(R.id.tv_name) as TextView
        val tvPrice = confirmBetDialog.findViewById(R.id.tv_price) as TextView
        val tvSubmit = confirmBetDialog.findViewById(R.id.tv_submit) as TextView
        val tvCancel = confirmBetDialog.findViewById(R.id.tv_cancel) as TextView

        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
            tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR)
        else
            tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)
        tvPrice.text = price
        tvSubmit.setOnClickListener(this)
        tvCancel.setOnClickListener(this)
        tvSubmit.setOnClickListener {
            auctionViewModel.hitMakeBet(falconId ,price.toInt() )
        }
        tvCancel.setOnClickListener {
            confirmBetDialog.dismiss()
            confirmBetDialog
        }
        confirmBetDialog.show()

        confirmBetDialog.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action === KeyEvent.ACTION_UP) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onBackPressed() {
        if(this::confirmBetDialog.isInitialized && confirmBetDialog!=null && confirmBetDialog.isShowing) {
            confirmBetDialog.dismiss()
        }
        else if(this::addBetDialog.isInitialized && addBetDialog!=null && addBetDialog.isShowing) {
            addBetDialog.dismiss()
        }
        else {
            super.onBackPressed()
        }
    }

    private val updateTextTask = object : Runnable {
        override fun run() {
            if(isOn) {
                hitAllApis()
                mainHandler.postDelayed(this, 20000)
            }
        }
    }

    override fun onPause() {
        mainHandler.removeCallbacks(updateTextTask)
        if (imageAdapter != null) {
            imageAdapter.pauseVideo()
        }
        super.onPause()
    }

    override fun onResume() {
        mainHandler.post(updateTextTask)
        super.onResume()
    }

}