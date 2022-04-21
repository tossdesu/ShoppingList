package com.tossdesu.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.ItemShopDisabledBinding
import com.tossdesu.shoppinglist.databinding.ItemShopEnabledBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var setOnLongClickListener: ((ShopItem) -> Unit)? = null
    var setOnClickListener: ((ShopItem) -> Unit)? = null

    class ShopItemEnabledHolder(val itemShopEnabledBinding: ItemShopEnabledBinding) :
        RecyclerView.ViewHolder(itemShopEnabledBinding.root) {
        fun bind(shopItem: ShopItem) = with(itemShopEnabledBinding) {
            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()
        }
    }

    class ShopItemDisabledHolder(val itemShopDisabledBinding: ItemShopDisabledBinding) :
        RecyclerView.ViewHolder(itemShopDisabledBinding.root) {
        fun bind(shopItem: ShopItem) = with(itemShopDisabledBinding) {
            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ENABLED -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_shop_enabled,
                    parent,
                    false
                )
                ShopItemEnabledHolder(ItemShopEnabledBinding.bind(view))
            }
            VIEW_TYPE_DISABLED -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_shop_disabled,
                    parent,
                    false
                )
                ShopItemDisabledHolder(ItemShopDisabledBinding.bind(view))
            }
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = when(val viewType = getItemViewType(position)) {
            VIEW_TYPE_ENABLED -> {
                (holder as ShopItemEnabledHolder).apply {
                    bind(shopList[position])
                }.itemShopEnabledBinding.root
            }
            VIEW_TYPE_DISABLED -> {
                (holder as ShopItemDisabledHolder).apply {
                    bind(shopList[position])
                }.itemShopDisabledBinding.root
            }
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        view.setOnLongClickListener {
            setOnLongClickListener?.invoke(shopList[position])
            true
        }
        view.setOnClickListener {
            setOnClickListener?.invoke(shopList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    override fun getItemCount() = shopList.size

    companion object {
        const val VIEW_TYPE_DISABLED = 100
        const val VIEW_TYPE_ENABLED = 101

        const val MAX_POOL_SIZE = 10
    }

}



