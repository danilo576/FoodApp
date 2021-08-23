package com.example.foodappsyncit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.example.foodappsyncit.network.responses.GetCategoriesResponse
import com.example.foodappsyncit.utils.ScreenState
import com.example.foodappsyncit.viewmodels.CategoryViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_menu.view.*

class MenuFragment : Fragment() {

    private val categoryViewModel: CategoryViewModel by lazy {
        ViewModelProvider(activity as MainActivity)[CategoryViewModel::class.java]
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

        categoryViewModel.categoriesLiveData.observe(viewLifecycleOwner, { state ->
            processCategoryResponse(state, view)
        })

        initCategoryRecyclerView(view)
        initProductRecyclerView(view)

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

    private fun processCategoryResponse(state: ScreenState<GetCategoriesResponse?>, view: View) {
        val progressBar = activity?.findViewById<ProgressBar>(R.id.progressBar)
        when (state) {
            is ScreenState.Loading -> {
                progressBar?.visibility = View.VISIBLE
            }
            is ScreenState.Success -> {
                progressBar?.visibility = View.GONE

                if (state.data != null) {

                    categoryList = state.data.categories as ArrayList<Category>

                    if (selectedCategory != null) {
                        val index = categoryList.indexOf(selectedCategory)
                        categoryAdapter.setData(state.data.categories, index)
                    } else {
                        selectedCategory = categoryList.first()
                        categoryAdapter.setData(state.data.categories, 0)
                    }

                    selectedCategory?.let {
                        productAdapter.setData(it.products)
                    }

                    categoryAdapter.setOnClickListener((object :
                        CategoryAdapter.OnItemClickListener {
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
                }
            }
            is ScreenState.Error -> {
                progressBar?.visibility = View.GONE
                progressBar?.rootView?.let {
                    Snackbar.make(
                        it,
                        state.message!!,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}
