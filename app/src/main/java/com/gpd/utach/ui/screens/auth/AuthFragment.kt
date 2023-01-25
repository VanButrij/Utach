package com.gpd.utach.ui.screens.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gpd.utach.R
import com.gpd.utach.databinding.FragmentAuthBinding
import com.gpd.utach.ui.screens.auth.google.AuthGoogle
import com.gpd.utach.ui.screens.register.EnterPhoneNumberFragment
import com.gpd.utach.utilits.replaceFragment


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()

    }

    private fun bindViewModel() {
        binding.loginButtonPhone.setOnClickListener {
            replaceFragment(EnterPhoneNumberFragment())
        }
        binding.loginButtonGoogle.setOnClickListener { authByGoogle() }
    }

    private fun authByGoogle(){
        replaceFragment(AuthGoogle())
    }


}