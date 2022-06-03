package com.tossdesu.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.ItemShopDisabledBinding
import com.tossdesu.shoppinglist.databinding.ItemShopEnabledBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemHolder>(ShopItemDiffCallback()) {

    var setOnLongClickListener: ((ShopItem) -> Unit)? = null
    var setOnClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemHolder {
        val layoutId = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ShopItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemHolder, position: Int) {
        val item = getItem(holder.adapterPosition)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            setOnLongClickListener?.invoke(getItem(holder.layoutPosition))
            true
        }
        binding.root.setOnClickListener {
            setOnClickListener?.invoke(getItem(holder.layoutPosition))
        }
        when (binding) {
            is ItemShopEnabledBinding -> {
                binding.shopItem = item
            }
            is ItemShopDisabledBinding -> {
                binding.shopItem = item
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 100
        const val VIEW_TYPE_ENABLED = 101

        const val MAX_POOL_SIZE = 10
    }
}