package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappsyncit.R
import com.example.foodappsyncit.adapters.FavoriteProductsAdapter
import com.example.foodappsyncit.utils.UserPreferences
import com.example.foodappsyncit.utils.ValidationUtil
import com.example.foodappsyncit.viewmodels.UserLoginViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*
import android.view.WindowManager
import android.widget.Toast
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.network.requests.UserUpdate
import kotlinx.android.synthetic.main.custom_dialog.view.*

class ProfileFragment : Fragment() {

    private lateinit var userViewModel: UserLoginViewModel

    private var firstName: String? = null
    private var lastName: String? = null
    private var phoneNumber: String? = null
    private lateinit var favoriteProductAdapter: FavoriteProductsAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        setupViewModels()

        setupFavoriteProductsRecyclerView(view)

        setupProductObserver(view)

        findUserByToken()

        setupLoggedInUserObserver(view)

        setupUpdateUserObserver(view)

        view.btnUpdateFirstName.setOnClickListener {
            updateUser("First Name", firstName)
        }

        view.btnUpdateLastName.setOnClickListener {
            updateUser("Last Name", lastName)
        }

        view.btnUpdatePhoneNumber.setOnClickListener {
            updateUser("Phone Number", phoneNumber)
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
        userViewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)
    }

    private fun findUserByToken() {
        UserPreferences.retrieveToken(requireContext(), "token")?.let {
            userViewModel.getLoggedInUser(
                "Bearer $it"
            )
        }
    }

    private fun setupLoggedInUserObserver(view: View) {
        userViewModel.loggedInUser.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                view.tvFirstName.text = it.body()?.user?.firstName
                firstName = it.body()?.user?.firstName.toString()
                view.tvLastName.text = it.body()?.user?.lastName
                lastName = it.body()?.user?.lastName.toString()
                view.tvEmail.text = it.body()?.user?.email
                view.tvPhone.text = it.body()?.user?.phone?.phoneNumber
                phoneNumber = it.body()?.user?.phone?.phoneNumber.toString()
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
                firstName = it.body()?.user?.firstName.toString()
                lastName = it.body()?.user?.lastName.toString()
                phoneNumber = it.body()?.user?.phone?.phoneNumber.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Unsuccessfully network response",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUser(heading: String, etHeading: String?) {
        val view = View.inflate(requireContext(), R.layout.custom_dialog, null)
        view.tvHeadingDialog.text = heading
        view.etHeadingDialog.setText(etHeading)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val lp = dialog.window!!.attributes
        lp.dimAmount = 0.7f
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        view.btnSaveDialog.setOnClickListener {
            UserPreferences.retrieveToken(requireContext(), "token")?.let {

                userViewModel.updateUser(
                    "Bearer $it",
                    when (heading) {
                        "First Name" -> UserUpdate(firstName = view.etHeadingDialog.text.toString())
                        "Last Name" -> UserUpdate(
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