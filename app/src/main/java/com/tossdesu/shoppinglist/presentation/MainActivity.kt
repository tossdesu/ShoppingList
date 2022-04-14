package com.tossdesu.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.ActivityMainBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initAdapter()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }

//        viewModel.shopList.value?.let {
//            viewModel.deleteShopItem(it[9])
//        }
//
//        viewModel.shopList.value?.let {
//            viewModel.changeEnabledState(it[8])
//        }

    }

    private fun initAdapter() {
        shopListAdapter = ShopListAdapter()
        mainBinding.rvShopList.apply {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        shopListAdapter.setOnLongClickListener = {
                viewModel.changeEnabledState(it)
        }
    }
}