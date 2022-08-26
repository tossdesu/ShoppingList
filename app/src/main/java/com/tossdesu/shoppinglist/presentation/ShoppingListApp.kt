package com.tossdesu.shoppinglist.presentation

import android.app.Application
import com.tossdesu.shoppinglist.di.DaggerApplicationComponent

class ShoppingListApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}