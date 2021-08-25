package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.adapters.CartAdapter
import com.example.foodappsyncit.controllers.CartController
import com.example.foodappsyncit.models.CartItem
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlin.math.roundToInt

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCartRecyclerView()

        initSwipeToDelete()

        if (CartController.cartList.isEmpty()) {
            emptyCartUi()
        } else {
            updateUi()
        }

        btnClearProducts.setOnClickListener {
            CartController.cartList.clear()
            emptyCartUi()
            (activity as MainActivity).incrementBadge("empty")
        }

        btnCheckout.setOnClickListener {
            emptyCartUi()
            CartController.cartList.clear()
            progressBar.visibility = View.VISIBLE
            emptyCart.visibility = View.INVISIBLE
            btnCheckout.visibility = View.INVISIBLE

            (activity as MainActivity).incrementBadge("empty")

            Handler(Looper.getMainLooper()).postDelayed({
                progressBar.visibility = View.INVISIBLE
                emptyCart.text = "Successfully ordered"
                emptyCart.visibility = View.VISIBLE
            }, 1000)

        }

        cartAdapter.setOnClickListener((object : CartAdapter.OnCartItemClickListener {
            override fun onCartItemClick(cartProduct: CartItem) {
                findNavController().navigate(
                    CartFragmentDirections.actionCartFragmentToDetailFragment(
                        cartProduct.product,
                        null,
                        cartProduct
                    )
                )
            }
        }))

    }

    private fun initSwipeToDelete() {

        class MyClass : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cartAdapter.deleteItem(viewHolder.absoluteAdapterPosition)

                if (CartController.cartList.isEmpty()) {
                    emptyCartUi()
                    (activity as MainActivity).incrementBadge("empty")
                } else {
                    updateUi()
                    (activity as MainActivity).incrementBadge("-")
                }
            }
        }

        ItemTouchHelper(MyClass()).attachToRecyclerView(recyclerViewCart)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        sumOfSubTotals?.text = "$${(CartController.totalSum() * 100.0).roundToInt() / 100.0}"
        sumOfDiscount?.text = "$0"
        sumOfTotalOrder?.text = "$${(CartController.totalSum() * 100.0).roundToInt() / 100.0}"
    }

    private fun initCartRecyclerView() {
        cartAdapter = CartAdapter()
        cartAdapter.setData(CartController.cartList)

        recyclerViewCart.layoutManager =
            LinearLayoutManager(activity)

        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun emptyCartUi() {
        emptyCart.visibility = View.VISIBLE
        nestedScrollView.visibility = View.INVISIBLE
        btnCheckout.visibility = View.INVISIBLE
    }

}