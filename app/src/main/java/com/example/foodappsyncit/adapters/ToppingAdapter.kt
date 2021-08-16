package com.example.foodappsyncit.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.models.CartItem
import com.example.foodappsyncit.models.Topping
import com.example.foodappsyncit.models.ToppingWrapper
import kotlinx.android.synthetic.main.toping_item.view.*

class ToppingAdapter(
    private val toppings: List<Topping>,
    private val toppingWrapper: List<ToppingWrapper>?
) :
    RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, isChecked: Boolean)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class ToppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val checkBox: CheckBox = itemView.findViewById(R.id.cbTopping)
        private val toppingName: TextView = itemView.findViewById(R.id.tvToppingName)
        private val toppingPrice: TextView = itemView.findViewById(R.id.tvToppingPrice)

        @SuppressLint("SetTextI18n")
        fun bind(topping: Topping) {
            toppingName.text = topping.name
            toppingPrice.text = "$${topping.price}"
            toppingWrapper?.forEach {
                if (topping == it.topping) {
                    checkBox.isChecked = true
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.toping_item, parent, false)
        return ToppingViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ToppingViewHolder, position: Int) {
        holder.bind(toppings[position])
        holder.itemView.cbTopping.setOnClickListener {
            mListener?.onItemClick(position, holder.itemView.cbTopping.isChecked)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = toppings.size
}
