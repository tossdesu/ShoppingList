package com.tossdesu.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.ItemShopDisabledBinding
import com.tossdesu.shoppinglist.databinding.ItemShopEnabledBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, RecyclerView.ViewHolder>(ShopItemDiffCallback()) {

    var setOnLongClickListener: ((ShopItem) -> Unit)? = null
    var setOnClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
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
        val adapterPosition = holder.adapterPosition
        val view = when (val viewType = getItemViewType(adapterPosition)) {
            VIEW_TYPE_ENABLED -> {
                (holder as ShopItemEnabledHolder).apply {
                    bind(getItem(adapterPosition))
                }.itemShopEnabledBinding.root
            }
            VIEW_TYPE_DISABLED -> {
                (holder as ShopItemDisabledHolder).apply {
                    bind(getItem(adapterPosition))
                }.itemShopDisabledBinding.root
            }
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        view.setOnLongClickListener {
            setOnLongClickListener?.invoke(getItem(holder.layoutPosition))
            true
        }
        view.setOnClickListener {
            setOnClickListener?.invoke(getItem(holder.layoutPosition))
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



