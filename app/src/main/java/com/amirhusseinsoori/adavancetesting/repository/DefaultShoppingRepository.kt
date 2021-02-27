package com.amirhusseinsoori.adavancetesting.repository

import androidx.lifecycle.LiveData
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingDao
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingItem
import com.amirhusseinsoori.adavancetesting.data.remote.PixabayApi
import com.amirhusseinsoori.adavancetesting.data.remote.response.ImageResponse
import com.amirhusseinsoori.adavancetesting.util.Resource
import retrofit2.Response
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    val shoppingDao: ShoppingDao,
    val api: PixabayApi
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = api.searchForImage(imageQuery)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?: Resource.error("An unknown error occured",null)
            }else{
                Resource.error("An unknown error occured",null)
            }

        } catch (ex: Exception) {
            Resource.error("Couldn't reach the server.Check your internet Connection  :${ex.message} ", null)

        }
    }
}