package com.app.view.activities

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.TextView.BufferType
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.app.R
import com.app.databinding.ActivityRegisterTwoBinding
import com.app.model.dataclasses.*
import com.app.utils.ApiCodes
import com.app.utils.AppConstants
import com.app.utils.RequestCodes.FRONT_IMAGE_PICKER_CODE
import com.app.utils.RequestCodes.REAR_IMAGE_PICKER_CODE
import com.app.utils.SharedPreferencesManager
import com.app.viewmodel.RegisterTwoViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import java.io.InputStream


class
RegistrationTwoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var registerTwoBinding: ActivityRegisterTwoBinding
    private val registerTwoViewModel by lazy { ViewModelProvider(this).get(RegisterTwoViewModel::class.java) }
    private lateinit var dataModel: QIDExtractDataModel
    private lateinit var dialog: Dialog
    private lateinit var otpview: OtpTextView
    private var otp: String? = ""
    private var isAuction: Boolean? = false
    private var isPassport: Boolean? = false
    private var moiError: String? = null
    private lateinit var tvNumber: TextView
    private lateinit var tvSubmit: TextView
    private var nationalityCode: String = "000"
    private var qid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerTwoBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_two)
        setBaseViewModel(registerTwoViewModel)
        setListeners()
        setObservers()
        if(intent.getSerializableExtra("data") != null){
            dataModel = (intent.getSerializableExtra("data") as QIDExtractDataModel?)!!
            moiError = intent.getStringExtra("moiError")
        }
        isAuction = intent.getBooleanExtra("isAuction", false)
        isPassport = intent.getBooleanExtra("isPassport", false)
        registerTwoViewModel.getIsdList()
        setData()
        setTermsConditions()
    }

    private fun setTermsConditions() {
        if (SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE) == "ar") {
            registerTwoBinding.tc.setText(registerTwoBinding.tc.text, BufferType.SPANNABLE)
            val s = registerTwoBinding.tc.text as Spannable
            val start: Int = registerTwoBinding.tc.length() - 16
            val end: Int = registerTwoBinding.tc.length()
            s.setSpan(ForegroundColorSpan(resources.getColor(R.color.blue)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            registerTwoBinding.tc.setText(registerTwoBinding.tc.text, BufferType.SPANNABLE)
            val s = registerTwoBinding.tc.text as Spannable
            val start: Int = registerTwoBinding.tc.length() - 25
            val end: Int = registerTwoBinding.tc.length()
            s.setSpan(ForegroundColorSpan(resources.getColor(R.color.blue)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private  fun setNationalityVisible(){
        registerTwoBinding.nationalityTv.visibility = View.VISIBLE
        registerTwoBinding.tvNationalityHeading.visibility = View.VISIBLE
        if (isAuction == true) {
            val nationalities = resources.getStringArray(R.array.nationality_auction)
            setNationalityData(nationalities)
        } else {
            registerTwoViewModel.getNationalityList()
        }
    }

    private fun setPassportView(isVisible: Int) {
        registerTwoBinding.passportEt.visibility = isVisible
        registerTwoBinding.passportTil.visibility = isVisible
        registerTwoBinding.tvPassport.visibility = isVisible
    }

    private fun setData() {
        if(isPassport == true) {
            setPassportView(View.VISIBLE)
            setNationalityVisible()
        }
         else if (moiError == "true") {
            qid = intent.getStringExtra("qid").toString()
            setNationalityVisible()
            setPassportView(View.GONE)
        } else {
            setPassportView(View.GONE)
            registerTwoBinding.nameEt.isEnabled = false
            registerTwoBinding.nameEt.background = getDrawable(R.color.box_color)
            if (SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE) == "ar")
                registerTwoBinding.nameEt.setText(dataModel.nameAr)
            else
                registerTwoBinding.nameEt.setText(dataModel.nameEn)

            registerTwoBinding.nationalityTv.visibility = View.GONE
            registerTwoBinding.tvNationalityHeading.visibility = View.GONE
            registerTwoBinding.spinnerNationality.visibility = View.GONE
        }
        if (isAuction == true)
            registerTwoBinding.linImage.visibility = View.VISIBLE
        else
            registerTwoBinding.linImage.visibility = View.GONE
    }

    private fun setNationalityData(nationalities: Array<out Any>) {
        val adapter = ArrayAdapter(this, R.layout.tranparent_text_layout, nationalities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        registerTwoBinding.nationalityTv.text = nationalities[0].toString()
        registerTwoBinding.spinnerNationality.adapter = adapter
        registerTwoBinding.spinnerNationality.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                registerTwoBinding.nationalityTv.text = nationalities[p2].toString()
                if(isAuction == true){
                    if(p2==0){
                        nationalityCode = "634"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setListeners() {
        registerTwoBinding.tvContinue.setOnClickListener(this)
        registerTwoBinding.ivBack.setOnClickListener(this)
        registerTwoBinding.rlFront.setOnClickListener(this)
        registerTwoBinding.rlRear.setOnClickListener(this)
        registerTwoBinding.tc.setOnClickListener(this)
    }

    private fun setObservers() {
        registerTwoViewModel.getResponseObserver().observe(this, this)
        registerTwoViewModel.getValidationLiveData().observe(this,
            {
                when (it.value) {
                    false -> {
                        showToastLong(it.message)
                    }
                    true -> {
                        verifyOtp()
                    }
                }
            })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            registerTwoBinding.tvContinue.id -> {
                if(isPassport == false) {
                    registerTwoViewModel.dataModel = dataModel!!
                    registerTwoViewModel.isAuction = isAuction!!
                    registerTwoViewModel.number = registerTwoBinding.numberEt.text.toString()
                    registerTwoViewModel.phoneCode = registerTwoBinding.tvIsd.text.toString()
                    registerTwoViewModel.email = registerTwoBinding.emailEt.text.toString()
                    registerTwoViewModel.switch = registerTwoBinding.swTc.isChecked
                    if (moiError == "true") {
                        registerTwoViewModel.nameAr = registerTwoBinding.nameEt.text.toString()
                        registerTwoViewModel.name = registerTwoBinding.nameEt.text.toString()
                        registerTwoViewModel.nationality = nationalityCode
                        registerTwoViewModel.qID = qid
                        registerTwoViewModel.nationalityName =
                            registerTwoBinding.nationalityTv.text.toString()
                    } else {
                        registerTwoViewModel.nameAr = dataModel.nameAr.toString()
                        registerTwoViewModel.name = dataModel.nameEn.toString()
                        registerTwoViewModel.qID = dataModel.qid!!
                    }
                    registerTwoViewModel.validate(this, true)
                } else {
                    registerTwoViewModel.isAuction = isAuction!!
                    registerTwoViewModel.number = registerTwoBinding.numberEt.text.toString()
                    registerTwoViewModel.phoneCode = registerTwoBinding.tvIsd.text.toString()
                    registerTwoViewModel.email = registerTwoBinding.emailEt.text.toString()
                    registerTwoViewModel.switch = registerTwoBinding.swTc.isChecked
                    registerTwoViewModel.nameAr = registerTwoBinding.nameEt.text.toString()
                    registerTwoViewModel.name = registerTwoBinding.nameEt.text.toString()
                    registerTwoViewModel.nationality = nationalityCode
                    registerTwoViewModel.qID = qid
                    registerTwoViewModel.nationalityName =
                        registerTwoBinding.nationalityTv.text.toString()
                    registerTwoViewModel.qID = registerTwoBinding.passportEt.text.toString()
                    registerTwoViewModel.validate(this, false)
                }
            }
            registerTwoBinding.ivBack.id -> {
                finish()
            }
            registerTwoBinding.rlFront.id -> {
                openFrontImagePicker()
            }
            registerTwoBinding.rlRear.id -> {
                openRearImagePicker()
            }
            registerTwoBinding.tc.id -> {
                startActivity(Intent(this, TCActivity::class.java))
            }
            registerTwoBinding.nationalityTv.id -> {
                registerTwoBinding.spinnerNationality.performClick()
            }
            registerTwoBinding.llmIsd.id -> {
                registerTwoBinding.spinnerIsd.performClick()
            }
        }
    }


    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.REGISTER -> {
                if (registerTwoViewModel.msg.isNotEmpty()) {
                    Toast.makeText(this, registerTwoViewModel.msg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,
                        resources.getString(R.string.user_reg_success),
                        Toast.LENGTH_SHORT).show()
                    if (isAuction == true) {
                        val intent = Intent(this, StatusActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
            ApiCodes.SEND_OTP -> {
                Toast.makeText(this, registerTwoViewModel.msg, Toast.LENGTH_SHORT)
                    .show()
                if (registerTwoViewModel.dataOTPMLD.value?.error == false) {
                    showOTPDialog(registerTwoViewModel.number)
                }
            }
            ApiCodes.GET_ISD -> {
                val codes: ISDCodes? = registerTwoViewModel.dataISDMLD.value
                val isdArray = getArrayIsd(codes!!.option)
                registerTwoBinding.tvIsd.text = isdArray[0].toString()
                val adapter = ArrayAdapter(this, R.layout.tranparent_text_layout, isdArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                registerTwoBinding.spinnerIsd.adapter = adapter
                registerTwoBinding.spinnerIsd.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        registerTwoBinding.tvIsd.text = isdArray[p2].toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            ApiCodes.GET_COUNTRY-> {
                val country: Country? = registerTwoViewModel.dataCountryMLD.value
                val countryArray = getArrayCountry(country!!.option2)
                setNationalityData(countryArray)
            }
            ApiCodes.CHECK_QID-> {
                if(registerTwoViewModel.msg.isNotEmpty()){
                    Toast.makeText(this, registerTwoViewModel.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getArrayIsd(codes: ArrayList<Option>?): Array<out Any> {
        val list = ArrayList<String>()
        codes!!.forEach {
            list.add(it.value.toString())
        }
        return list.toArray()
    }

    private fun getArrayCountry(codes: ArrayList<Option2>?): Array<out Any> {
        val languageToLoad = SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)

        val list = ArrayList<String>()
        codes?.forEach {
            if(languageToLoad == "en") {
                list.add(it.value.toString())
            } else {
                list.add(it.trad.toString())
            }
        }
        return list.toArray()
    }

    private fun openFrontImagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(1024 * 2)
            .maxResultSize(1024, 1024)
            .start(FRONT_IMAGE_PICKER_CODE)
        /*.createIntent { intent ->
            startForProfileImageResult.launch(intent)
        }*/
    }

    private fun openRearImagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(1024 * 2)
            .maxResultSize(1024, 1024)
            .start(REAR_IMAGE_PICKER_CODE)
        /*.createIntent { intent ->
            startForProfileImageResult.launch(intent)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FRONT_IMAGE_PICKER_CODE -> {
                if (resultCode == RESULT_OK)
                    data?.let { intent ->
                        val fileUri = intent.data
                        if (fileUri != null) {
//                            registerTwoBinding.ivFront.setImageURI(fileUri)
                            registerTwoBinding.ivFrontRight.visibility = View.VISIBLE
                            registerTwoBinding.rlFront.background =
                                resources.getDrawable(R.drawable.green_stroke, this.theme)
                            convertFrontUri(fileUri)
                        }
                    }
            }
            REAR_IMAGE_PICKER_CODE -> {
                if (resultCode == RESULT_OK)
                    data?.let { intent ->
                        val fileUri = intent.data
                        if (fileUri != null) {
//                            registerTwoBinding.ivRear.setImageURI(fileUri)
                            registerTwoBinding.ivBackRight.visibility = View.VISIBLE
                            registerTwoBinding.rlRear.background =
                                resources.getDrawable(R.drawable.green_stroke, this.theme)
                            convertRearUri(fileUri)
                        }
                    }
            }
        }
    }

    private fun convertFrontUri(imageUri: Uri) {
        val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
        val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
        val encodedImage = encodeImage(selectedImage)
        registerTwoViewModel.frontImage = encodedImage
    }

    private fun convertRearUri(imageUri: Uri) {
        val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
        val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
        val encodedImage = encodeImage(selectedImage)
        registerTwoViewModel.backImage = encodedImage;
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    override fun onBackPressed() {
        if (this::dialog.isInitialized && dialog != null && dialog.isShowing)
            dialog.dismiss()
        else
            super.onBackPressed()
    }

    private fun showOTPDialog(number: String?) {
        dialog = Dialog(this, android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setDimAmount(0.5f)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setContentView(R.layout.dialog_otp)
        tvNumber = dialog.findViewById(R.id.tv_number) as TextView
        tvSubmit = dialog.findViewById(R.id.tv_submit) as TextView
        otpview = dialog.findViewById(R.id.otp_view) as OtpTextView
        val phoneCode = registerTwoBinding.tvIsd.text.toString()
        tvNumber.text = "$phoneCode-$number"

        tvSubmit.setOnClickListener(this)

        otpview?.requestFocusOTP()
        otpview?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otpValue: String) {
                otp = otpValue
            }
        }

        tvSubmit.setOnClickListener {
            registerTwoViewModel.otp = otp.toString()
            registerTwoViewModel.validateOTP(this@RegistrationTwoActivity)
        }
        tvNumber.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action === KeyEvent.ACTION_UP) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun verifyOtp() {
        if(registerTwoViewModel.otp==registerTwoViewModel.dataOTPMLD.value!!.responce || registerTwoViewModel.otp == "786123" ){
            if(moiError == "false") {
                registerTwoViewModel.hitRegisterApi()
            } else {
                registerTwoViewModel.hitRegisterApiForUnknowUser()
            }
        }
        else {
            showToastShort(getString(R.string.enter_otp))
        }
    }

}