package com.tossdesu.shoppinglist.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tossdesu.shoppinglist.databinding.ItemShopEnabledBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopItemEnabledHolder(val itemShopEnabledBinding: ItemShopEnabledBinding) :
    RecyclerView.ViewHolder(itemShopEnabledBinding.root) {

    fun bind(shopItem: ShopItem) = with(itemShopEnabledBinding) {
        tvName.text = shopItem.name
        tvCount.text = shopItem.count.toString()
    }
}