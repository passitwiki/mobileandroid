package com.passitwiki.passit.dialogfragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.fragments.SettingsFragment
import kotlinx.android.synthetic.main.fragment_password_dialog.*
import kotlinx.android.synthetic.main.fragment_password_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PasswordDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "FragmentSettings"
        const val ACCESS_TOKEN = "AccessToken"
        fun newInstance(token: String, key: String): Fragment {
            val fragment = SettingsFragment()
            val argument = Bundle()
            argument.putString(ACCESS_TOKEN, token)
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

    //TODO making the view display btm nav bar
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = activity!!.layoutInflater
            .inflate(R.layout.fragment_password_dialog, LinearLayout(activity), false)


        val builder = Dialog(activity!!)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setContentView(view)
        return builder
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.let {
            val accessToken = it?.getString(ACCESS_TOKEN)


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



                RetrofitClient.instance.postSetPassword(
                        accessToken!!,
                        newPassword,
                        currPassword
                    )
                    .enqueue(object : Callback<Unit> {
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<Unit>,
                            response: Response<Unit>
                        ) {
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_password_dialog, container, false)

        view.arrowBack.setOnClickListener {
            dismiss()
        }
        view.buttonCross.setOnClickListener {
            dismiss()
        }

//        view.buttonCheck.setOnClickListener {
//            val currPassword = view.editTextCurrentPassword.text.toString().trim()
//            val newPassword = view.editTextNewPassword.text.toString().trim()
//            val confirmPassword = view.editTextConfirmPassword.text.toString().trim()
//
//            if (currPassword.isEmpty()) {
//                editTextCurrentPassword.error = "Current password required"
//                editTextCurrentPassword.requestFocus()
//                return@setOnClickListener
//            }
//            if (newPassword.isEmpty()) {
//                editTextNewPassword.error = "New password required"
//                editTextNewPassword.requestFocus()
//                return@setOnClickListener
//            }
//            if (confirmPassword.isEmpty()) {
//                editTextConfirmPassword.error = "Confirmation required"
//                editTextConfirmPassword.requestFocus()
//                return@setOnClickListener
//            }
//            if (newPassword != confirmPassword) {
//                editTextNewPassword.error = "The new passwords don't match"
//                editTextNewPassword.requestFocus()
//                return@setOnClickListener
//            }
//
//
//
//            RetrofitClient.instance.postSetPassword(
//                    "Bearer ${globalToken!!}",
//                    newPassword,
//                    currPassword
//                )
//                .enqueue(object : Callback<Unit> {
//                    override fun onFailure(call: Call<Unit>, t: Throwable) {
//                        Toast.makeText(globalContext, t.message, Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onResponse(
//                        call: Call<Unit>,
//                        response: Response<Unit>
//                    ) {
//                        Toast.makeText(
//                            globalContext,
//                            getString(R.string.success_password),
//                            Toast.LENGTH_LONG
//                        ).show()
//                        dismiss()
//
//                    }
//
//                })
//        }
        return view
    }

}