package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalToken
import com.passitwiki.passit.tools.globalUser
import kotlinx.android.synthetic.main.fragment_add_news_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewsDialogFragment : DialogFragment() {

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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_news_dialog, container, false)

        val titleEditText = view.editTextNewsTitle
        val contentEditText = view.editTextNewsContent
        val check = view.buttonCheck
        val cross = view.buttonCross

        cross.setOnClickListener {
            dismiss()
        }

        check.setOnClickListener {

            val title = titleEditText.text.toString().trim()
            val content = contentEditText.text.toString().trim()

            if (title.isEmpty()) {
                view.editTextNewsTitle.error = "Title required"
                view.editTextNewsTitle.requestFocus()
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                view.editTextNewsContent.error = "Content required"
                view.editTextNewsContent.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.postNews(
                    "Bearer $globalToken",
                    title,
                    content,
                    2,
                    globalUser!!.profile.field_age_groups[0].id
                )
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Toast.makeText(globalContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {
                        dismiss()
                    }

                })
        }

        return view
    }

}
