package com.example.foodappsyncit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.repository.SharedRepository
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val repository = SharedRepository()

    private val _categoriesLiveData = MutableLiveData<GetCategoriesResponse?>()
    val categoriesLiveData: LiveData<GetCategoriesResponse?> = _categoriesLiveData

    fun refreshCategories() {
        viewModelScope.launch {
            val response = repository.getCategories()
            _categoriesLiveData.postValue(response)
        }
    }

}