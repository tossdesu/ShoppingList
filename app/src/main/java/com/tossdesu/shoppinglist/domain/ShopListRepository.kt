package com.tossdesu.shoppinglist.domain

interface ShopListRepository {

    fun addShopItem(shopItemId: Int)

    fun deleteShopItem(shopItemId: Int)

    fun editShopItem(shopItemId: Int)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>
}