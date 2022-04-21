package com.tossdesu.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.tossdesu.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.rvShopList.apply {
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
        shopListAdapter.setOnClickListener = {
            Log.d("setOnClickListener", "Item id = ${it.id}")
        }

        val swipeToDeleteCallback = SwipeToDelete { positionForRemove: Int ->
            val shopItem = shopListAdapter.shopList[positionForRemove]
            viewModel.deleteShopItem(shopItem)
//            binding.rvShopList.adapter?.notifyItemRemoved(positionForRemove)

            Snackbar.make(binding.rvShopList, "Item was deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    viewModel.addShopItem(shopItem)
                }.show()
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvShopList)
    }
}