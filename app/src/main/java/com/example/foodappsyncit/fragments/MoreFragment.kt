package com.example.foodappsyncit.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.AuthActivity
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.controllers.CartController
import com.example.foodappsyncit.utils.UserPreferences
import com.example.foodappsyncit.utils.ValidationUtil
import com.example.foodappsyncit.viewmodels.UserLoginViewModel
import kotlinx.android.synthetic.main.fragment_more.view.*

class MoreFragment : Fragment() {

    private lateinit var userViewModel: UserLoginViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userViewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_more, container, false)

        if ((activity as MainActivity).intent.getStringExtra("Guest") != null) {
            setupLogin(view)
        } else {
            setupLogout(view)
        }

        return view
    }

    private fun setupLogin(view: View) {
        view.ivLogout.setImageResource(R.drawable.ic_login)
        view.tvLogout.text = "Login"
        view.logoutLayout.setOnClickListener {
            Intent(requireContext(), AuthActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setupLogout(view: View) {
        view.logoutLayout.setOnClickListener {

            UserPreferences.retrieveToken("token")?.let {
                userViewModel.logoutUser()
            }

            setupLogoutObserver()
        }
    }

    private fun setupLogoutObserver() {
        userViewModel.logoutUser.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Intent(activity, AuthActivity::class.java).apply {
                    startActivity(this)
                    activity?.finish()
                }
                CartController.cartList.clear()
                requireContext().getSharedPreferences("myPrefs", 0).edit().clear().apply()
            } else {
                ValidationUtil.showToast(
                    requireContext(),
                    "It seems like something went wrong with token"
                )
            }
        }
    }

}