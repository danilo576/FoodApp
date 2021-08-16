package com.example.foodappsyncit.controllers

import com.example.foodappsyncit.models.CartItem

class CartController {
    companion object {
        var cartList: ArrayList<CartItem> = ArrayList()

        fun totalSum(): Double {
            var sum = 0.00
            cartList.forEach { cartProduct ->
                sum += cartProduct.price
            }
            return sum
        }
    }
}