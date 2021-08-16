package com.example.foodappsyncit.repository

import androidx.lifecycle.LiveData
import com.example.foodappsyncit.database.ProductDao
import com.example.foodappsyncit.models.Product

class ProductRepository(private val productDao: ProductDao) {

    val readALlProducts: LiveData<List<Product>> = productDao.readALlProducts()

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}
