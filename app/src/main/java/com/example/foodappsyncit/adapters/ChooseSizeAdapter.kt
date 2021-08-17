package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.models.Variant
import kotlinx.android.synthetic.main.choose_size_item.view.*

class ChooseSizeAdapter(
    private val variants: List<Variant>,
    variant: Variant?
) :
    RecyclerView.Adapter<ChooseSizeAdapter.ChooseSizeViewHolder>() {

    private var selectedItemPosition: Int = if (variant != null) variants.indexOf(variant) else 0
    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class ChooseSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val button: Button = itemView.findViewById(R.id.buttonChooseSize)

        fun bind(variant: Variant) {
            button.text = variant.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSizeViewHolder {
        val view1 =
            LayoutInflater.from(parent.context).inflate(R.layout.choose_size_item, parent, false)
        return ChooseSizeViewHolder(view1)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: ChooseSizeViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.bind(variants[position])

        holder.itemView.buttonChooseSize.setOnClickListener {
            mListener?.onItemClick(position)
            selectedItemPosition = position
            notifyDataSetChanged()
        }

        if (selectedItemPosition == position) {
            holder.itemView.buttonChooseSize.setBackgroundResource(R.drawable.rounded_button)
            holder.itemView.buttonChooseSize.setTextColor(Color.WHITE)
        } else {
            holder.itemView.buttonChooseSize.setBackgroundResource(R.drawable.guest_button)
            holder.itemView.buttonChooseSize.setTextColor(Color.parseColor("#1FA5E1"))
        }
    }

    override fun getItemCount(): Int = variants.size
}