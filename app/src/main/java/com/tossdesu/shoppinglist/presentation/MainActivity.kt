package com.tossdesu.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tossdesu.shoppinglist.R

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            Log.d("ActivityTest", it.toString())
        }

        viewModel.getShopList()

//        viewModel.shopList.value?.let {
//            viewModel.deleteShopItem(it[0])
//        }

        viewModel.shopList.value?.let {
            viewModel.editShopItem(it[0])
        }

    }
}