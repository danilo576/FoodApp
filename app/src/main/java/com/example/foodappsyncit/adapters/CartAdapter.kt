package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.controllers.CartController
import com.example.foodappsyncit.models.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_item.view.*

class CartAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var products = mutableListOf<CartItem>()

    private var mListener: OnCartItemClickListener? = null

    interface OnCartItemClickListener {
        fun onCartItemClick(cartProduct: CartItem)
    }

    fun setOnClickListener(listener: OnCartItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartViewHolder).onBind(this.products[position])

        holder.itemView.cartItemLayout.setOnClickListener {
            mListener?.onCartItemClick(products[position])
        }

    }

    override fun getItemCount(): Int = this.products.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<CartItem>) {
        this.products = products.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        this.products.removeAt(position)
        CartController.cartList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class CartViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
    ) {
        private val image: ImageView = itemView.findViewById(R.id.ivImageCart)
        private val heading: TextView = itemView.findViewById(R.id.tvHeadingCart)
        private val price: TextView = itemView.findViewById(R.id.tvPriceCart)
        private val marker: ImageView = itemView.findViewById(R.id.ivMarker)
        private val nameAndValue: TextView = itemView.findViewById(R.id.tvNameValueCart)
        private val categoryName: TextView = itemView.findViewById(R.id.tvToppings)

        @SuppressLint("SetTextI18n")
        fun onBind(cartProduct: CartItem) {
            Picasso.get().load(cartProduct.product.image.replace("http:", "https:"))
                .resize(300, 300)
                .into(image)
            heading.text = cartProduct.product.name
            price.text = "$${cartProduct.price}"
            if (cartProduct.product.type == "simple") {
                nameAndValue.visibility = View.GONE
            }
            nameAndValue.text = "${cartProduct.variant.name} - ${cartProduct.variant.value}"

            val blackText = "Add to ${cartProduct.categoryName}: "

            val toppingString = cartProduct.toppings.joinToString {
                "${it.topping.name} ($${it.topping.price})"
            }

            val spannableString = SpannableString(blackText + toppingString)
            val foregroundSpan = ForegroundColorSpan(Color.BLACK)
            spannableString.setSpan(
                foregroundSpan,
                0,
                blackText.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            categoryName.text = spannableString
            val nameOfMarker = when (cartProduct.product.markers.firstOrNull()) {
                "Hot" -> R.drawable.ic_hot
                "Veg" -> R.drawable.ic_veg
                else -> 0
            }
            marker.setImageResource(nameOfMarker)
        }

    }
}