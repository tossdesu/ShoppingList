package com.tossdesu.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingCompleteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var shopListAdapter: ShopListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val component by lazy {
        (application as ShoppingListApp).component
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        Log.d("LifecycleTest", "MainActivity -> CREATE")
        setContentView(binding.root)

        initAdapter()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        binding.fbAddItem.setOnClickListener {
            if (isScreenPortraitMode()) {
                val intent = ShopItemActivity.newIntentAddShopItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    override fun onEditingComplete() {
        supportFragmentManager.popBackStack()
        Toast.makeText(this, "Complete", Toast.LENGTH_LONG).show()
    }

    private fun isScreenPortraitMode() = binding.shopItemContainer == null

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .addToBackStack(null)
            .commit()
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
            if (isScreenPortraitMode()) {
                val intent = ShopItemActivity.newIntentEditShopItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.setOnLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }
}