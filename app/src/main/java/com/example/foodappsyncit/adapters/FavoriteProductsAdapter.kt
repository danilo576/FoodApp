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

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemLongClick(): Boolean
        fun onItemClickListener(productId: Int, position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class FavoriteProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(product: Product, position: Int) {
            itemView.tvHeading.text = product.name
            itemView.tvPrice.text =
                if (product.type == "simple") "$${product.price}" else "$${product.variants[0].price}"
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
                        product.category_name,
                        null
                    )
                itemView.findNavController().navigate(action)
            }

            itemView.btnCancelProfile.setOnClickListener {
                itemView.rowLayout.animate().apply {
                    scaleX(0f)
                    scaleY(0f)
                    alpha(0f)
                    duration = 1000
                    withEndAction {
                        mListener?.onItemClickListener(product.id, position)
                    }
                }
            }

            itemView.rowLayout.setOnLongClickListener {
                it.btnCancelProfile.visibility = View.VISIBLE
                mListener?.onItemLongClick()!!
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
        holder.bind(favoriteList[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        this.favoriteList = list
        notifyDataSetChanged()
    }
}