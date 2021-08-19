package com.example.foodappsyncit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Product(
    val id: Int,
    val description: String = "",
    val image: String = "",
    val name: String = "",
    var isFavorite: Boolean = false,
    var price: Double = 0.00,
    val type: String = "",
    val toppings: @RawValue List<Topping> = listOf(),
    val variants: @RawValue List<Variant> = listOf(),
    val markers: List<String> = listOf(),
    val category_name: String = ""
) : Parcelable
