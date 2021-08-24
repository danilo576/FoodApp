package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.adapters.FavoriteProductsAdapter
import com.example.foodappsyncit.network.requests.UserUpdate
import com.example.foodappsyncit.network.responses.UserResponse
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
                user?.firstName = it.body()?.user?.firstName.toString()
                user?.lastName = it.body()?.user?.lastName.toString()
                user?.phone?.phoneNumber = it.body()?.user?.phone?.phoneNumber.toString()
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
        val view = View.inflate(requireContext(), R.layout.custom_dialog, null)
        view.tvHeadingDialog.text = when (heading) {
            UserInfo.FIRST_NAME -> "First Name"
            UserInfo.LAST_NAME -> "Last Name"
            else -> "Phone Number"
        }
        view.etHeadingDialog.setText(userInfo)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val lp = dialog.window!!.attributes
        lp.dimAmount = 0.7f
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        view.btnSaveDialog.setOnClickListener {
            UserPreferences.retrieveToken("token")?.let {

                userViewModel.updateUser(
                    when (heading.toString()) {
                        "FIRST_NAME" -> UserUpdate(firstName = view.etHeadingDialog.text.toString())
                        "LAST_NAME" -> UserUpdate(
                            lastName = view.etHeadingDialog.text.toString()
                        )
                        else -> UserUpdate(phoneNumber = view.etHeadingDialog.text.toString())
                    }
                )
                dialog.dismiss()
            }
        }

        view.btnCancelDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

}