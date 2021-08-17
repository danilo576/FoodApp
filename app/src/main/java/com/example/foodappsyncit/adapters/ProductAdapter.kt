package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.models.Product
import com.squareup.picasso.Picasso

class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var products = mutableListOf<Product>()

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductViewHolder).onBind(products[position])
        holder.itemView.setOnClickListener {
            mListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = this.products.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<Product>) {
        this.products = products.toMutableList()
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        ) {
        private val image: ImageView = itemView.findViewById(R.id.ivPizza)
        private val heading: TextView = itemView.findViewById(R.id.tvHeading)
        private val description: TextView = itemView.findViewById(R.id.tvDescription)
        private val price: TextView = itemView.findViewById(R.id.tvPrice)
        private val marker: ImageView = itemView.findViewById(R.id.ivMarker)

        fun onBind(product: Product) {
            Picasso.get().load(product.image.replace("http:", "https:")).into(image)
            heading.text = product.name
            description.text = product.description
            price.text =
                if (product.type == "configurable") product.variants[0].price.toString() else product.price.toString()
            val nameOfMarker = when (product.markers.firstOrNull()) {
                "Hot" -> R.drawable.ic_hot
                "Veg" -> R.drawable.ic_veg
                else -> 0
            }
            marker.setImageResource(nameOfMarker)
        }
    }
}
