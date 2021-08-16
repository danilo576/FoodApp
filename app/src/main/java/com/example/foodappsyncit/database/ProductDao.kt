package com.example.foodappsyncit.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodappsyncit.models.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query("SELECT * FROM product_table ORDER BY id ASC")
    fun readALlProducts(): LiveData<List<Product>>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM product_table")
    suspend fun deleteAllProducts()
}
