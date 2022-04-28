package com.tossdesu.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
            shopListAdapter.submitList(it)
        }

        binding.fbAddItem.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddShopItem(this)
            startActivity(intent)
        }
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
            setupSwipeListener(this)
        }
        setupLongClickListener()
        setupClickListener()
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(shopItem)

                Snackbar.make(recyclerView, "Item was deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        viewModel.addShopItem(shopItem)
                    }.show()
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.8F
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun setupClickListener() {
        shopListAdapter.setOnClickListener = {
            val intent = ShopItemActivity.newIntentEditShopItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.setOnLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }
}