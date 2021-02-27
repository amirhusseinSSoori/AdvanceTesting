package com.amirhusseinsoori.adavancetesting.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingItem
import com.amirhusseinsoori.adavancetesting.data.remote.response.ImageResponse
import com.amirhusseinsoori.adavancetesting.util.Resource

class FakeShoppingRepository : ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableShoppingItem = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()
    private var shouldReturnNetworkError = false
    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData(){
         observableShoppingItem.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }
    private fun getTotalPrice():Float{
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()

    }
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return observeAllShoppingItem()
    }

    override fun observeTotalPrice(): LiveData<Float> {
       return observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if(shouldReturnNetworkError){
          Resource.error("Error",null)
        }else{
            Resource.success(ImageResponse(listOf(),0,0))
        }
    }
}