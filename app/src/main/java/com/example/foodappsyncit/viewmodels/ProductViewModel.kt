package com.example.foodappsyncit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappsyncit.network.responses.FavoriteProductsResponse
import com.example.foodappsyncit.network.responses.MessageResponse
import com.example.foodappsyncit.repository.ProductRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductViewModel() : ViewModel() {

    private val repository = ProductRepository()
    var readAllFavoriteProducts: MutableLiveData<Response<FavoriteProductsResponse>> =
        MutableLiveData()
    var addFavoriteProduct: MutableLiveData<Response<MessageResponse>> = MutableLiveData()
    var deleteFavoriteProduct: MutableLiveData<Response<MessageResponse>> = MutableLiveData()

    fun readAllFavorites(token: String) {
        viewModelScope.launch {
            val response = repository.readAllFavorites(token)
            readAllFavoriteProducts.value = response
        }
    }

    fun addProductToFavorites(token: String, productId: Int) {
        viewModelScope.launch {
            val response = repository.addProductToFavorites(token, productId)
            addFavoriteProduct.value = response
        }
    }

    fun deleteProductFromFavorites(token: String, productId: Int) {
        viewModelScope.launch {
            val response = repository.deleteProductFromFavorites(token, productId)
            deleteFavoriteProduct.value = response
        }
    }

}