package com.example.foodappsyncit.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.foodappsyncit.R
import com.example.foodappsyncit.activities.AuthActivity
import com.example.foodappsyncit.models.User
import com.example.foodappsyncit.utils.ValidationUtil
import com.example.foodappsyncit.viewmodels.UserRegistryViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {

    private lateinit var userRegisterViewModel: UserRegistryViewModel

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val telephonyManager =
            requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val permission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_PHONE_NUMBERS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_PHONE_NUMBERS),
                1
            )
        }

        userRegisterViewModel = ViewModelProvider(this).get(UserRegistryViewModel::class.java)

        view.btnSignUp.setOnClickListener {

            if (ValidationUtil.isValidFirstName(requireContext(), view.etFirstName.text.toString())
                && ValidationUtil.isValidLastName(
                    requireContext(),
                    view.etLastName.text.toString()
                )
                && ValidationUtil.isValidEmail(
                    requireContext(),
                    view.etEmailRegister.text.toString()
                )
                && ValidationUtil.isValidPassword(
                    requireContext(),
                    view.etPasswordRegister.text.toString()
                )
                && ValidationUtil.isValidConfirmPassword(
                    requireContext(), view.etConfirmPasswordRegister.text.toString()
                )
                && ValidationUtil.areEqual(
                    requireContext(),
                    view.etPasswordRegister.text.toString(),
                    view.etConfirmPasswordRegister.text.toString()
                )
            ) {
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val email = etEmailRegister.text.toString()
                val password = etPasswordRegister.text.toString()
                val passwordConfirmation = etConfirmPasswordRegister.text.toString()
                val phoneModel = Build.MODEL.toString()
                val phoneNumber = telephonyManager.line1Number.toString()
                val appVersion = "Version1"
                val os = "Android"
                val status = "enabled"

                val user = User(
                    appVersion,
                    email,
                    firstName,
                    lastName,
                    os,
                    password,
                    passwordConfirmation,
                    phoneModel,
                    phoneNumber,
                    status
                )

                userRegisterViewModel.addUser(user)
                userRegisterViewModel.registryResponse.observe(viewLifecycleOwner) { response ->
                    if (!response.isSuccessful) {
                        ValidationUtil.showToast(
                            requireContext(),
                            "The email has already been take!"
                        )
                        return@observe
                    }
                    (activity as AuthActivity).replaceFragment(LoginFragment())
                    Toast.makeText(requireContext(), "Successfully registered", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().finish()
                }
            }
        }

        return view
    }

}