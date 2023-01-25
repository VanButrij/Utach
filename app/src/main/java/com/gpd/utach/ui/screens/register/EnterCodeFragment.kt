package com.gpd.utach.ui.screens.register

import androidx.fragment.app.Fragment
import com.gpd.utach.R
import com.gpd.utach.database.*
import com.gpd.utach.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*


class EnterCodeFragment(private val phoneNumber: String,val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher{

                val string = register_input_code.text.toString()
                if (string.length == 6) {
                    enterCode()
                }
            })
        }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) { authUtilPhone(phoneNumber) }
            else showToast(authResult.exception?.message.toString())
        }
    }
}