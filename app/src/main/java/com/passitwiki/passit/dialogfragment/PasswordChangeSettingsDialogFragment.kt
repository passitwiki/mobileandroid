package com.passitwiki.passit.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_password_change_dialog.*
import kotlinx.android.synthetic.main.fragment_password_change_dialog.view.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

/**
 * Pop-up that enables you to change the password of the current user.
 */
class PasswordChangeSettingsDialogFragment(private val key: String) : DialogFragment() {
    private val repository: Repository by inject()

    /**
     * Changes the layout's size to below.
     */
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    /**
     * Prepares the two listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_password_change_dialog, container, false)

        view.arrowBack.setOnClickListener {
            dismiss()
        }
        view.buttonCross.setOnClickListener {
            dismiss()
        }

        return view
    }

    /**
     * Sets a listener and posts the input passwords.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonCheck.setOnClickListener {
            val currPassword = editTextCurrentPassword.text.toString().trim()
            val newPassword = editTextNewPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (currPassword.isEmpty()) {
                editTextCurrentPassword.error = "Current password required"
                editTextCurrentPassword.requestFocus()
                return@setOnClickListener
            }
            if (newPassword.isEmpty()) {
                editTextNewPassword.error = "New password required"
                editTextNewPassword.requestFocus()
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                editTextConfirmPassword.error = "Confirmation required"
                editTextConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                editTextNewPassword.error = "The new passwords don't match"
                editTextNewPassword.requestFocus()
                return@setOnClickListener
            }

            postInputPassword(newPassword, currPassword)
        }
    }

    /**
     * Takes the input values and posts them to the API. Closes the dialog.
     */
    private fun postInputPassword(newPassword: String, currPassword: String) {
        var resource: Resource<Unit>? = null
        runBlocking {
            resource = repository.handlePostSetPassword(accessToken, newPassword, currPassword)
        }

        when (resource!!.status) {
//                Status.LOADING -> null //TODO loading
            Status.ERROR -> {
                if (resource!!.message == "Unauthorised. Please refresh.") {
                    runBlocking {
                        Utilities(repository).justRefresh(key)
                        postInputPassword(newPassword, currPassword)
                    }
                } else {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        resource!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Status.SUCCESS -> {
                Log.d(
                    "MyTagExplicitNetworking",
                    "PasswordChangeSettingsDialogFragment " +
                            "response ${resource.toString()}"
                )
                Toast.makeText(
                    requireActivity().applicationContext,
                    getString(R.string.success_password),
                    Toast.LENGTH_LONG
                ).show()
                dismiss()

            }
        }
    }
}