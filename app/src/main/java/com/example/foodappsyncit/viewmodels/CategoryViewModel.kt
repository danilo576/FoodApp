package com.example.foodappsyncit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.repository.SharedRepository
import com.example.foodappsyncit.utils.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class CategoryViewModel(private val repository: SharedRepository = SharedRepository()) :
    ViewModel() {

    private val _categoriesLiveData = MutableLiveData<ScreenState<GetCategoriesResponse?>>()
    val categoriesLiveData: LiveData<ScreenState<GetCategoriesResponse?>>
        get() = _categoriesLiveData

    init {
        refreshCategories()
    }

    private fun refreshCategories() {
        _categoriesLiveData.postValue(ScreenState.Loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCategories()
                _categoriesLiveData.postValue(ScreenState.Success(response))
            } catch (e: Exception) {
                _categoriesLiveData.postValue(ScreenState.Error(e.message!!, null))
            }
        }
    }

}