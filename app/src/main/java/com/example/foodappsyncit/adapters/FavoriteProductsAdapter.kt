package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.fragments.ProfileFragmentDirections
import com.example.foodappsyncit.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteProductsAdapter :
    RecyclerView.Adapter<FavoriteProductsAdapter.FavoriteProductsViewHolder>() {

    private var favoriteList = emptyList<Product>()

    inner class FavoriteProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            itemView.tvHeading.text = product.name
            itemView.tvPrice.text = "$${product.price}"
            Picasso.get().load(product.image.replace("http:", "https:")).resize(300, 300)
                .into(itemView.ivPicture)

            val nameOfMarker = when (product.markers.firstOrNull()) {
                "Hot" -> R.drawable.ic_hot
                "Veg" -> R.drawable.ic_veg
                else -> 0
            }
            itemView.ivMarker.setImageResource(nameOfMarker)

            itemView.rowLayout.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToDetailFragment(
                        product,
                        null,
                        null
                    )
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteProductsAdapter.FavoriteProductsViewHolder {
        return FavoriteProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        )
    }

    override fun getItemCount(): Int = favoriteList.size

    override fun onBindViewHolder(
        holder: FavoriteProductsAdapter.FavoriteProductsViewHolder,
        position: Int
    ) {
        holder.bind(favoriteList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        this.favoriteList = list
        notifyDataSetChanged()
    }
}