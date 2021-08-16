package com.example.foodappsyncit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.AuthActivity
import com.example.foodappsyncit.activities.MainActivity
import com.example.foodappsyncit.network.requests.LoginRequest
import com.example.foodappsyncit.utils.UserPreferences
import com.example.foodappsyncit.utils.ValidationUtil
import com.example.foodappsyncit.viewmodels.UserLoginViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: UserLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(this).get(UserLoginViewModel::class.java)

        view.btnLogin.setOnClickListener {
            if (ValidationUtil.isValidEmail(requireContext(), view.etEmail.text.toString())
                && ValidationUtil.isValidPassword(requireContext(), view.etPassword.text.toString())
            ) {
                val email = view.etEmail.text.toString()
                val password = view.etPassword.text.toString()
                val loginUser = LoginRequest(email, password)
                viewModel.loginUser(loginUser)
                viewModel.loginResponse.observe(viewLifecycleOwner) {
                    if (it != null) {
                        if (it.isSuccessful) {
                            UserPreferences.saveToken(
                                requireContext(),
                                "token", it.body()?.token
                            )
                            Intent(activity, MainActivity::class.java).apply {
                                startActivity(this)
                            }
                            activity?.finish()
                        }
                        if (it.code() == 401) {
                            ValidationUtil.showToast(requireContext(), "Wrong email or password")
                        }
                        viewModel.loginResponse.value = null
                    }

                }
            }
        }

        view.btnRegister.setOnClickListener {
            (activity as AuthActivity).replaceFragment(RegisterFragment())
        }

        view.btnGuest.setOnClickListener {
            Intent(activity, MainActivity::class.java).apply {
                putExtra("Guest", "guest")
                startActivity(this)
            }
            requireActivity().finish()
        }

        return view
    }

}