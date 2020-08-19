package com.unbounds.trakt.view.login

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginWrapperFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        startActivityForResult(LoginActivity.createIntent(requireContext()), LOGIN_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        findNavController().navigate(LoginWrapperFragmentDirections.actionPopOut())
    }

    companion object {
        private const val LOGIN_REQUEST = 1
    }
}
