package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.models.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categories = mutableListOf<Category>()
    private var mListener: OnItemClickListener? = null
    private var selectedItemPosition: Int = 0

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(parent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        (holder as CategoryViewHolder).onBind(categories[position])

        holder.itemView.btnCategory.setOnClickListener {
            mListener?.onItemClick(position)
            selectedItemPosition = position
            notifyDataSetChanged()
        }

        if (selectedItemPosition == position) {
            holder.itemView.btnCategory.setBackgroundResource(R.drawable.rounded_button)
            holder.itemView.btnCategory.setTextColor(Color.WHITE)
        } else {
            holder.itemView.btnCategory.setBackgroundResource(R.drawable.rounded_button_white)
            holder.itemView.btnCategory.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = categories.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(categories: List<Category>, selectedIndex: Int) {
        this.categories = categories.toMutableList()
        this.selectedItemPosition = selectedIndex
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
    ) {
        private val categoryButton: Button = itemView.findViewById(R.id.btnCategory)

        fun onBind(category: Category) {
            categoryButton.text = category.name
        }

    }
}
