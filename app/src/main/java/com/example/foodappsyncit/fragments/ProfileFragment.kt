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
import com.example.foodappsyncit.viewmodels.ProductViewModel
import com.example.foodappsyncit.viewmodels.UserLoginViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*
import android.view.WindowManager
import android.widget.Toast
import com.example.foodappsyncit.network.requests.UserUpdate
import kotlinx.android.synthetic.main.custom_dialog.view.*

class ProfileFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserLoginViewModel

    private var firstName: String? = null
    private var lastName: String? = null
    private var phoneNumber: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)

        val adapter = FavoriteProductsAdapter()
        productViewModel.readAllProducts.observe(viewLifecycleOwner, {
            adapter.setData(it)
            if (it.isEmpty()) {
                view.tvMyFavorites.visibility = View.GONE
            }
        })

        val recyclerView = view.recyclerViewProfile
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        UserPreferences.retrieveToken(requireContext(), "token")?.let {
            userViewModel.getLoggedInUser(
                "Bearer $it"
            )
        }

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

        view.ivEdit1.setOnClickListener {

            val view = View.inflate(requireContext(), R.layout.custom_dialog, null)
            view.tvFirstNameDialog.text = "First Name"
            view.etFirstNameDialog.setText(firstName)
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
                        UserUpdate(firstName = view.etFirstNameDialog.text.toString())
                    )
                    dialog.dismiss()
                }
            }

            view.btnCancelDialog.setOnClickListener {
                dialog.dismiss()
            }
        }

        view.ivEdit2.setOnClickListener {
            val view = View.inflate(requireContext(), R.layout.custom_dialog, null)
            view.tvFirstNameDialog.text = "Last Name"
            view.etFirstNameDialog.setText(lastName)
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
                        UserUpdate(lastName = view.etFirstNameDialog.text.toString())
                    )
                    dialog.dismiss()
                }
            }

            view.btnCancelDialog.setOnClickListener {
                dialog.dismiss()
            }
        }

        view.ivEdit4.setOnClickListener {
            val view = View.inflate(requireContext(), R.layout.custom_dialog, null)
            view.tvFirstNameDialog.text = "Phone Number"
            view.etFirstNameDialog.setText(phoneNumber)
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
                        UserUpdate(phoneNumber = view.etFirstNameDialog.text.toString())
                    )
                    dialog.dismiss()
                }
            }

            view.btnCancelDialog.setOnClickListener {
                dialog.dismiss()
            }
        }

        return view
    }

}