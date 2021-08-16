package com.example.foodappsyncit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.adapters.CategoryAdapter
import com.example.foodappsyncit.adapters.ProductAdapter
import com.example.foodappsyncit.models.Category
import com.example.foodappsyncit.viewmodels.CategoryViewModel
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.view.*

class MenuFragment : Fragment() {

    private val viewModelMenu: CategoryViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(CategoryViewModel::class.java)
    }

    private var selectedCategory: Category? = null
    private var categoryList = ArrayList<Category>()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        initCategoryRecyclerView(view)
        initProductRecyclerView(view)

        getCategories()

        categoryAdapter.setOnClickListener((object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                selectedCategory = categoryList[position]
                productAdapter.setData(categoryList[position].products)

            }
        }))

        productAdapter.setOnClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val product = selectedCategory?.products?.get(position)
                val action = product?.let {
                    selectedCategory?.name?.let { categoryName ->
                        MenuFragmentDirections.actionMenuFragmentToDetailFragment(
                            it,
                            categoryName,
                            null
                        )
                    }
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
        })

        return view
    }

    private fun initProductRecyclerView(view: View) {
        productAdapter = ProductAdapter()
        view.recyclerViewProducts.layoutManager =
            LinearLayoutManager(activity)
        view.recyclerViewProducts.adapter = productAdapter
        view.recyclerViewProducts.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initCategoryRecyclerView(view: View) {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.setData(categoryList, 0)
        view.recyclerViewCategories.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.recyclerViewCategories.adapter = categoryAdapter
    }

    private fun getCategories() {
        viewModelMenu.refreshCategories()
        viewModelMenu.categoriesLiveData.observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(
                    activity as MainActivity,
                    "Unsuccessful network call!",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            } else {
                categoryList = response.categories as ArrayList<Category>

                if (selectedCategory != null) {
                    val index = categoryList.indexOf(selectedCategory)
                    categoryAdapter.setData(response.categories, index)
                } else {
                    selectedCategory = categoryList.first()
                    categoryAdapter.setData(response.categories, 0)
                }

                selectedCategory?.let {
                    productAdapter.setData(it.products)
                }
            }
        }
    }

}
