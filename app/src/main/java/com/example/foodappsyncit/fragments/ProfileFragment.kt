package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.adapters.FavoriteProductsAdapter
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.UserResponse
import com.example.foodappsyncit.utils.ProfileDialog
import com.example.foodappsyncit.utils.UserPreferences
import com.example.foodappsyncit.utils.ValidationUtil
import com.example.foodappsyncit.viewmodels.UserLoginViewModel
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

enum class UserInfo() {
    FIRST_NAME,
    LAST_NAME,
    PHONE_NUMBER
}

class ProfileFragment : Fragment() {

    private lateinit var userViewModel: UserLoginViewModel

    private lateinit var favoriteProductAdapter: FavoriteProductsAdapter

    private lateinit var profileDialog: ProfileDialog

    private var user: UserResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        setupViewModels()

        setupFavoriteProductsRecyclerView(view)

        findUserByToken()

        setupLoggedInUserObserver(view)

        setupProductObserver(view)

        setupUpdateUserObserver(view)

        view.btnUpdateFirstName.setOnClickListener {
            updateUser(UserInfo.FIRST_NAME, user?.firstName)
        }

        view.btnUpdateLastName.setOnClickListener {
            updateUser(UserInfo.LAST_NAME, user?.lastName)
        }

        view.btnUpdatePhoneNumber.setOnClickListener {
            updateUser(UserInfo.PHONE_NUMBER, user?.phone?.phoneNumber)
        }

        favoriteProductAdapter.setOnClickListener(object :
            FavoriteProductsAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemLongClick(): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClickListener(productId: Int, position: Int) {
                (activity as MainActivity).favoriteList.removeIf {
                    it.id == productId
                }
                favoriteProductAdapter.notifyItemRemoved(position)
                (activity as MainActivity).productViewModel.deleteProductFromFavorites(productId)
            }
        })

        return view
    }

    private fun setupFavoriteProductsRecyclerView(view: View) {
        favoriteProductAdapter = FavoriteProductsAdapter()
        view.recyclerViewProfile.apply {
            adapter = favoriteProductAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductObserver(view: View) {
        favoriteProductAdapter.setData((activity as MainActivity).favoriteList)
        if ((activity as MainActivity).favoriteList.isEmpty()) {
            view.tvMyFavorites.visibility = View.GONE
        }
    }

    private fun setupViewModels() {
        userViewModel = ViewModelProvider(this)[UserLoginViewModel::class.java]
    }

    private fun findUserByToken() {
        UserPreferences.retrieveToken("token")?.let {
            userViewModel.getLoggedInUser()
        }
    }

    private fun setupLoggedInUserObserver(view: View) {
        userViewModel.loggedInUser.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                user = it.body()?.user
                view.tvFirstName.text = it.body()?.user?.firstName
                view.tvLastName.text = it.body()?.user?.lastName
                view.tvEmail.text = it.body()?.user?.email
                view.tvPhone.text = it.body()?.user?.phone?.phoneNumber
            } else {
                ValidationUtil.showToast(requireContext(), "Something went wrong")
            }
        }
    }

    private fun setupUpdateUserObserver(view: View) {
        userViewModel.updatedUser.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show()
                view.tvFirstName.text = it.body()?.user?.firstName
                view.tvLastName.text = it.body()?.user?.lastName
                view.tvPhone.text = it.body()?.user?.phone?.phoneNumber
                user = it.body()?.user
            } else {
                Toast.makeText(
                    requireContext(),
                    "Unsuccessfully network response",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUser(heading: UserInfo, userInfo: String?) {
        profileDialog = ProfileDialog(context, heading, userInfo)
        profileDialog.showDialog()

        profileDialog.btnSaveDialog.setOnClickListener {
            UserPreferences.retrieveToken("token")?.let {

                userViewModel.updateUser(
                    when (heading.toString()) {
                        "FIRST_NAME" -> UserUpdate(firstName = profileDialog.getUserResult())
                        "LAST_NAME" -> UserUpdate(
                            lastName = profileDialog.getUserResult()
                        )
                        else -> UserUpdate(phoneNumber = profileDialog.getUserResult())
                    }
                )
                profileDialog.dismissDialog()
            }

        }

        profileDialog.view.btnCancelDialog.setOnClickListener {
            profileDialog.dismissDialog()
        }
    }
}