package com.example.foodappsyncit.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String = "",
    val image: String = "",
    val name: String = "",
    var isFavorite: Boolean = false,
    var price: Double = 0.00,
    val type: String = ""
) : Parcelable {
    @Ignore
    val toppings: List<Topping> = listOf()

    @Ignore
    val variants: List<Variant> = listOf()

    @Ignore
    val markers: List<String> = listOf()
}
