package com.tossdesu.shoppinglist.data

import com.tossdesu.shoppinglist.domain.ShopItem
import com.tossdesu.shoppinglist.domain.ShopListRepository

object ShopListRepositoryImpl: ShopListRepository {

    val shoppingList = mutableListOf<ShopItem>()
    var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            addShopItem(ShopItem("Item $i", i, true))
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shoppingList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shoppingList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        shoppingList.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shoppingList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("ShopItem with id=$shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shoppingList.toList()
    }
}