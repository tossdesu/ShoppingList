package com.tossdesu.shoppinglist.di

import android.app.Application
import com.tossdesu.shoppinglist.data.AppDatabase
import com.tossdesu.shoppinglist.data.ShopListDao
import com.tossdesu.shoppinglist.data.ShopListRepositoryImpl
import com.tossdesu.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepositoryImpl(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}