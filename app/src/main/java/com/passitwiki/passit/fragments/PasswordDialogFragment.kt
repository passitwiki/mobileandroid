package com.passitwiki.passit.fragments

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
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalToken
import kotlinx.android.synthetic.main.fragment_passworddialog.*
import kotlinx.android.synthetic.main.fragment_passworddialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PasswordDialogFragment : DialogFragment() {


    //TODO making the view display btm nav bar
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = activity!!.layoutInflater
            .inflate(R.layout.fragment_passworddialog, LinearLayout(activity), false)


        val builder = Dialog(activity!!)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setContentView(view)
        return builder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_passworddialog, container, false)

        view.arrowBack.setOnClickListener {
            dismiss()
        }
        view.buttonCross.setOnClickListener {
            dismiss()
        }

        view.buttonCheck.setOnClickListener {
            val currPassword = view.editTextCurrentPassword.text.toString().trim()
            val newPassword = view.editTextNewPassword.text.toString().trim()
            val confirmPassword = view.editTextConfirmPassword.text.toString().trim()

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
            if (!newPassword.equals(confirmPassword)) {
                editTextNewPassword.error = "The new passwords don't match"
                editTextNewPassword.requestFocus()
                return@setOnClickListener
            }



            RetrofitClient.instance.postSetPassword(
                    "Bearer ${globalToken!!}",
                    newPassword,
                    currPassword
                )
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(globalContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {
                        Toast.makeText(
                            globalContext,
                            getString(R.string.success_password),
                            Toast.LENGTH_LONG
                        ).show()
                        dismiss()

                    }

                })
        }
        return view
    }

}