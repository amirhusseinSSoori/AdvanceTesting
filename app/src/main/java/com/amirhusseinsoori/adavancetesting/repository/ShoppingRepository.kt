package com.amirhusseinsoori.adavancetesting.repository

import androidx.lifecycle.LiveData
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingDao
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingItem
import com.amirhusseinsoori.adavancetesting.data.remote.response.ImageResponse
import com.amirhusseinsoori.adavancetesting.util.Resource
import retrofit2.Response

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItem(): LiveData<List<ShoppingItem>>
    fun observeTotalPrice(): LiveData<Float>
    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>

}