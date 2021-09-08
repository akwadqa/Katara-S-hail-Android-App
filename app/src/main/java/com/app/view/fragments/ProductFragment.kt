
package com.app.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.R
import com.app.databinding.FragmentProductBinding
import com.app.model.dataclasses.*
import com.app.utils.ApiCodes
import com.app.view.activities.*
import com.app.view.adaptors.*
import com.app.viewmodel.ProductViewModel


class ProductFragment : BaseFragment(), View.OnClickListener,
    ShopAdapter.OnShopItemClickListener, ProductAdapter.OnItemClickListener {

    private lateinit var binding: FragmentProductBinding
    private val productViewModel by lazy { ViewModelProvider(requireActivity()).get(ProductViewModel::class.java) }
    private lateinit var shopAdapter: ShopAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        return binding.root
    }

    override fun initialiseFragmentBaseViewModel() {
        binding.lifecycleOwner = this
        setFragmentBaseViewModel(productViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        productViewModel.getProductList()
        productViewModel.getShopList()
    }

    private fun setObservers() {
        productViewModel.getResponseObserver().observe(this@ProductFragment.requireActivity(), this)
    }

    private fun setListeners() {
        binding.tvAll.setOnClickListener(this)
        binding.ivTicket.setOnClickListener(this)
        binding.ivMenu.setOnClickListener(this)
        binding.tvHome.setOnClickListener(this)
    }

    private fun setShopAdaptor(value: List<ShopListResponse?>?) {
        if(activity!=null && isAdded) {
            shopAdapter = ShopAdapter(this@ProductFragment.requireActivity(), value, this@ProductFragment)
            binding.rvShops.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvShops.adapter = shopAdapter
        }
    }

    private fun setProductAdaptor(value: List<ProductListResponse?>?)
    {
        if(activity!=null && isAdded) {
            productAdapter = ProductAdapter(this@ProductFragment.requireActivity(),
                value,
                this@ProductFragment)
            binding.rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvProducts.adapter = productAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvAll.id -> {
            }
            binding.ivTicket.id -> {
                (activity as MainActivity).hitTicketTab()
            }
            binding.ivMenu.id -> {
                (activity as MainActivity).openDrawer()
            }
            binding.tvHome.id -> {
                (activity as MainActivity).openHomeFragment()
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.GET_PRODUCT -> {
                setProductAdaptor(productViewModel.dataProductMLD.value)
            }
            ApiCodes.GET_SHOP -> {
                setShopAdaptor(productViewModel.dataShopMLD.value)
            }
        }
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(this@ProductFragment.requireActivity(), ProductActivity::class.java)
            .putExtra("data",productViewModel.dataProductMLD.value!![position]))
    }

    override fun onShopItemClick(position: Int) {
        startActivity(Intent(this@ProductFragment.requireActivity(), ShopActivity::class.java)
            .putExtra("shop_id",productViewModel.dataShopMLD.value!![position]!!.shopId))
    }

}