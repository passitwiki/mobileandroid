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
import com.passitwiki.passit.tools.justRefresh
import kotlinx.android.synthetic.main.fragment_add_news_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Pop-up that enables you to add new news.
 */
class AddNewsDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "AddNewsDialogFragment"
        const val FIELD_AGE_GROUP = "FieldAgeGroup"
        fun newInstance(key: String, fag: Int): DialogFragment {
            val dFragment =
                AddNewsDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putInt(FIELD_AGE_GROUP, fag)
            dFragment.arguments = argument
            return dFragment
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
     * Prepares the view and listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            arguments.let {
                val fag: Int = it!!.getInt(FIELD_AGE_GROUP)

                //TODO CHANGE THE SUBJECT GROUP ACCORDINGLY
                postInputNews(accessToken, title, content, 2, fag)
            }
        }

        return view
    }

    /**
     * Takes the input values and posts them to the API. Closes the dialog.
     */
    fun postInputNews(
        access: String,
        title: String,
        content: String,
        subjectGroup: Int,
        fag: Int
    ) {
        RetrofitClient.instance.postNews(access, title, content, subjectGroup, fag)
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
                    Log.d(
                        "MyTagExplicitNetworking",
                        "AddNewsDialogFragment response ${response.body()}"
                    )

                    if (response.body() == null) {
                        justRefresh("addNewsDialogFragment")
                        postInputNews(accessToken, title, content, subjectGroup, fag)
                    }
                    dismiss()
                }

            })
    }
}
