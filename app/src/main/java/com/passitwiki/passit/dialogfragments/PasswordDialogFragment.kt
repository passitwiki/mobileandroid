package com.passitwiki.passit.dialogfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.api.RetrofitClient
import kotlinx.android.synthetic.main.fragment_password_dialog.*
import kotlinx.android.synthetic.main.fragment_password_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pop-up that enables you to change the password of the current user.
 */
class PasswordDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "PasswordDialogFragment"
        fun newInstance(key: String): DialogFragment {
            val fragment = PasswordDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

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
        val view = inflater.inflate(R.layout.fragment_password_dialog, container, false)

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

            postInputPassword(accessToken, newPassword, currPassword)
        }
    }

    /**
     * Takes the input values and posts them to the API. Closes the dialog.
     */
    fun postInputPassword(access: String, newPassword: String, currPassword: String) {
        RetrofitClient.instance.postSetPassword(access, newPassword, currPassword)
            .enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "PasswordDialogFragment response ${response.body()}"
                    )
                    Toast.makeText(
                        activity!!.applicationContext,
                        getString(R.string.success_password),
                        Toast.LENGTH_LONG
                    ).show()
                    dismiss()

                }
            })
    }
}