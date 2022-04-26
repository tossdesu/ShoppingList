package com.tossdesu.shoppinglist.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tossdesu.shoppinglist.databinding.ItemShopDisabledBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopItemDisabledHolder(val itemShopDisabledBinding: ItemShopDisabledBinding) :
    RecyclerView.ViewHolder(itemShopDisabledBinding.root) {

    fun bind(shopItem: ShopItem) = with(itemShopDisabledBinding) {
        tvName.text = shopItem.name
        tvCount.text = shopItem.count.toString()
    }
}