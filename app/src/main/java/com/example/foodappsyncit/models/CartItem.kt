package com.example.foodappsyncit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CartItem(
    val product: Product,
    val variant: @RawValue Variant,
    val toppings: @RawValue ArrayList<ToppingWrapper>,
    val categoryName: String = "",
    val price: Double = 0.00
) : Parcelable
