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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: FavoriteProductsAdapter.FavoriteProductsViewHolder,
        position: Int
    ) {
        val currentProduct = favoriteList[position]
        holder.itemView.tvHeading.text = currentProduct.name
        holder.itemView.tvPrice.text = "$${currentProduct.price}"
        Picasso.get().load(currentProduct.image.replace("http:", "https:")).resize(300, 300)
            .into(holder.itemView.ivPicture)

        val nameOfMarker = when (currentProduct.markers.firstOrNull()) {
            "Hot" -> R.drawable.ic_hot
            "Veg" -> R.drawable.ic_veg
            else -> 0
        }
        holder.itemView.ivMarker.setImageResource(nameOfMarker)

        holder.itemView.rowLayout.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToDetailFragment(
                    currentProduct,
                    null,
                    null
                )
            holder.itemView.findNavController().navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        this.favoriteList = list
        notifyDataSetChanged()
    }
}