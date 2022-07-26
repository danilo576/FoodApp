package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.adapters.ChooseSizeAdapter
import com.example.foodappsyncit.adapters.ToppingAdapter
import com.example.foodappsyncit.controllers.CartController
import com.example.foodappsyncit.models.*
import com.example.foodappsyncit.utils.UserPreferences
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlin.math.roundToInt

class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var toppingAdapter: ToppingAdapter
    private var chooseSizeAdapter: ChooseSizeAdapter? = null

    private var variant = Variant()

    private var selectedToppings = ArrayList<ToppingWrapper>()
    private var toppingPrice = 0.00

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        if (args.cartItem != null) {
            val index = CartController.cartList.indexOf(args.cartItem)
            selectedToppings = CartController.cartList[index].toppings
            selectedToppings.forEach {
                toppingPrice += it.topping.price
            }
            variant = args.cartItem!!.variant
        }

        Picasso.get().load(args.product?.image?.replace("http:", "https:")).into(view.ivPizzaDetail)
        view.tvHeadingDetail.text = args.product?.name
        view.tvDescriptionDetail.text = args.product?.description

        initToppingRecyclerView(view)

        if (args.product?.type == "configurable") {
            variant.price = if (args.cartItem != null) {
                args.cartItem!!.variant.price
            } else {
                args.product!!.variants[0].price
            }
            initChooseSizeRecyclerView(view)
            updateInitialUi(view)
        } else {
            hideConfigurableUi(view)
            val price = args.product?.price
            if (price != null) {
                view.btnAddToCart.text = "Add to cart for $${getTotalSum(price, toppingPrice)}"
            }
        }

        if ((activity as MainActivity).favoriteList.contains(args.product)) {
            view.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            view.ivFavorite.setImageResource(R.drawable.ic_favorite_outline)
        }

        val nameOfMarker = when (args.product!!.markers.firstOrNull()) {
            "Hot" -> R.drawable.ic_hot
            "Veg" -> R.drawable.ic_veg
            else -> 0
        }
        view.ivMarker.setImageResource(nameOfMarker)

        if ((activity as MainActivity).intent.getStringExtra("Guest") != null) {
            view.ivFavorite.visibility = View.GONE
        }

        view.ivFavorite.setOnClickListener {
            if ((activity as MainActivity).favoriteList.contains(args.product)) {
                view.ivFavorite.setImageResource(R.drawable.ic_favorite_outline)
                args.product?.let { product ->
                    deleteProductFromFavorites(product)
                    (activity as MainActivity).favoriteList.removeIf {
                        it.id == product.id
                    }
                }
            } else {
                view.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
                args.product?.let { product ->
                    addProductToFavorites(product)
                    (activity as MainActivity).favoriteList.add(product)
                }
            }

        }

        view.btnAddToCart.setOnClickListener {
            var price =
                if (args.product?.type == "configurable") getTotalSum(
                    variant.price,
                    toppingPrice
                ) else getTotalSum(args.product!!.price, toppingPrice)

            if (variant.name.isEmpty() && args.product?.type == "configurable") {
                variant.name = args.product!!.variants[0].name
                variant.value = args.product!!.variants[0].value
                price = args.product!!.variants[0].price
            }

            // Check the destination where we came form, args.cartItem was the action from CartFragment
            if (args.cartItem != null) {
                val index = CartController.cartList.indexOf(args.cartItem)
                CartController.cartList[index] = CartItem(
                    args.product!!,
                    variant,
                    selectedToppings,
                    args.cartItem!!.categoryName,
                    price
                )
                Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToCartFragment())
            } else {
                args.categoryName?.let { categoryName ->
                    CartItem(
                        args.product!!,
                        variant,
                        selectedToppings,
                        categoryName,
                        price
                    )
                }?.let { product ->
                    CartController.cartList.add(
                        product
                    )
                }

                (activity as MainActivity).incrementBadge("+")
                Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
            }

            AlphaAnimation(0.5f, 1.0f).apply {
                duration = 50
                startOffset = 200
                repeatMode = Animation.REVERSE
                it.startAnimation(this)
            }
        }

        chooseSizeAdapter?.setOnClickListener((object : ChooseSizeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                variant = args.product!!.variants[position]
                updateUi(position, view)
            }
        }))

        toppingAdapter.setOnClickListener((object : ToppingAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, isChecked: Boolean) {
                if (isChecked) {
                    toppingPrice += args.product!!.toppings[position].price
                    selectedToppings.add(ToppingWrapper(args.product!!.toppings[position], true))
                } else {
                    toppingPrice -= args.product!!.toppings[position].price
                    selectedToppings.remove(ToppingWrapper(args.product!!.toppings[position], true))
                }
                if (args.product!!.type == "configurable") {
                    updateButtonUi(variant.price, toppingPrice, view)
                } else {
                    updateButtonUi(args.product!!.price, toppingPrice, view)
                }
            }
        }))

        return view
    }

    private fun initChooseSizeRecyclerView(view: View) {
        chooseSizeAdapter = ChooseSizeAdapter(args.product!!.variants, args.cartItem?.variant)
        view.recyclerViewChooseSize?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.recyclerViewChooseSize?.adapter = chooseSizeAdapter
    }

    private fun initToppingRecyclerView(view: View?) {
        toppingAdapter = ToppingAdapter(args.product!!.toppings, selectedToppings)
        view?.recyclerViewTopping?.layoutManager = LinearLayoutManager(activity)
        view?.recyclerViewTopping?.adapter = toppingAdapter
    }

    private fun addProductToFavorites(product: Product) {
        UserPreferences.retrieveToken("token")?.let {
            (activity as MainActivity).productViewModel.addProductToFavorites(
                product.id
            )
        }
        Toast.makeText(
            requireContext(),
            "Product added in favorites",
            Toast.LENGTH_SHORT
        ).show()

    }

    private fun deleteProductFromFavorites(product: Product) {
        UserPreferences.retrieveToken("token")?.let {
            (activity as MainActivity).productViewModel.deleteProductFromFavorites(
                product.id
            )
        }
        Toast.makeText(
            requireContext(),
            "Successfully removed ${product.name} from favorite list",
            Toast.LENGTH_SHORT
        ).show()

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(position: Int, view: View) {
        view.tvChooseName.text = args.product!!.variants[position].name
        view.tvChooseValue.text = args.product!!.variants[position].value
        view.btnAddToCart.text =
            "Add to cart for $${((variant.price + toppingPrice) * 100.0).roundToInt() / 100.0}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateButtonUi(sum1: Double, sum2: Double, view: View) {
        view.btnAddToCart.text =
            "Add to cart for $${((sum1 + sum2) * 100.0).roundToInt() / 100.0}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateInitialUi(view: View) {
        view.tvChooseName.text =
            if (args.cartItem != null) args.cartItem!!.variant.name else args.product!!.variants[0].name
        view.tvChooseValue.text =
            if (args.cartItem != null) args.cartItem!!.variant.value else args.product!!.variants[0].value

        val price =
            if (args.cartItem != null) args.cartItem!!.price else args.product!!.variants[0].price

        view.btnAddToCart.text = "Add to cart for $${price}"

    }

    private fun hideConfigurableUi(view: View) {
        view.tvChooseName.visibility = View.GONE
        view.smallDesh.visibility = View.GONE
        view.tvChooseValue.visibility = View.GONE
        view.tvChooseSize.visibility = View.GONE
        view.line1.visibility = View.GONE
    }

    private fun getTotalSum(sum1: Double, sum2: Double): Double {
        return ((sum1 + sum2) * 100.0).roundToInt() / 100.0
    }

}
