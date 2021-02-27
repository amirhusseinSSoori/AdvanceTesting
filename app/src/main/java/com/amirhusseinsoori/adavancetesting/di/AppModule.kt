package com.amirhusseinsoori.adavancetesting.di


import android.content.Context
import androidx.room.Room
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingDao
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingDataBase
import com.amirhusseinsoori.adavancetesting.data.remote.PixabayApi
import com.amirhusseinsoori.adavancetesting.util.Constance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideShoppingItemDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingDataBase::class.java, Constance.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(dataBase: ShoppingDataBase): ShoppingDao = dataBase.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi():PixabayApi{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constance.URL_API).
                build().create(PixabayApi::class.java)
    }

}