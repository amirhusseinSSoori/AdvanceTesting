package com.amirhusseinsoori.adavancetesting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhusseinsoori.adavancetesting.data.local.ShoppingItem
import com.amirhusseinsoori.adavancetesting.data.remote.response.ImageResponse
import com.amirhusseinsoori.adavancetesting.repository.ShoppingRepository
import com.amirhusseinsoori.adavancetesting.util.Constance
import com.amirhusseinsoori.adavancetesting.util.Event
import com.amirhusseinsoori.adavancetesting.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {


    val shoppingItems = repository.observeAllShoppingItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if(name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The fields must not be empty", null)))
            return
        }
        if(name.length > Constance.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The name of the item" +
                    "must not exceed ${Constance.MAX_NAME_LENGTH} characters", null)))
            return
        }
        if(priceString.length > Constance.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price of the item" +
                    "must not exceed ${Constance.MAX_PRICE_LENGTH} characters", null)))
            return
        }
        val amount = try {
            amountString.toInt()
        } catch(e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid amount", null)))
            return
        }
        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if(imageQuery.isEmpty()) {
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}