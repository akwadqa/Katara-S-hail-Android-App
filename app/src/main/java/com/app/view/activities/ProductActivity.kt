package com.app.view.activities

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.app.R
import com.app.databinding.ActivityNewsViewBinding
import com.app.databinding.ActivityProductBinding
import com.app.model.dataclasses.NewsListResponse
import com.app.model.dataclasses.NewsMainResponse
import com.app.model.dataclasses.ProductListResponse
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProductBinding
    private lateinit var dataModel : ProductListResponse
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        setListeners()
        getIntentData()
        setData()
    }

    private fun getIntentData() {
        dataModel = (intent.getSerializableExtra("data") as ProductListResponse?)!!
    }

    private fun setData() {
        binding.tvName.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel.productNameAr else dataModel.productName
        binding.tvPrice.text = dataModel.productPrice.toString()
        binding.tvDescription.text = if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC)) dataModel.productDescriptionAr else dataModel.productDescription
        Glide.with(this).load(dataModel.docImage).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivImage)
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener(this)
        binding.tvVendor.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivBack.id -> {
                finish()
            }
            binding.tvVendor.id -> {
                showContactDialog()
            }
        }
    }

    private fun showContactDialog() {
        dialog = Dialog(this,R.style.yourCustomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.semi_transparent)))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.setContentView(R.layout.dialog_vendor)
        val tvPhone = dialog.findViewById(R.id.tv_phone) as TextView
        val tvEmail = dialog.findViewById(R.id.tv_email) as TextView
        tvPhone.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + dataModel.shop!!.shopPhone) //change the number
            startActivity(callIntent)
            dialog.dismiss()
        }
        tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf(dataModel.shop!!.shopEmail)
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)
            intent.type = "text/html"
            startActivity(Intent.createChooser(intent, resources.getString(R.string.send_email)))
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

    override fun onBackPressed() {
        if(this::dialog.isInitialized && dialog!=null && dialog.isShowing) {
            dialog.dismiss()
        }
        else {
            super.onBackPressed()
        }
    }

}