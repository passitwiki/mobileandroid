package com.passitwiki.passit.dialogfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.News
import com.passitwiki.passit.tools.justRefresh
import kotlinx.android.synthetic.main.fragment_patch_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatchDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "PatchDialogFragment"
        const val ID_NEWS = "IdNews"
        const val TITLE_NEWS = "TitleNews"
        const val CONTENT_NEWS = "ContentNews"
        fun newInstance(key: String, idNews: Int, title: String, content: String): DialogFragment {
            val fragment = PatchDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putString(TITLE_NEWS, title)
            argument.putString(CONTENT_NEWS, content)
            argument.putInt(ID_NEWS, idNews)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_patch_dialog, container, false)

        val id = arguments!!.getInt(ID_NEWS)
        val title = arguments!!.getString(TITLE_NEWS)!!
        val content = arguments!!.getString(CONTENT_NEWS)!!
        val titleEdit = thisView.editTextEditNewsTitle
        val contentEdit = thisView.editTextEditNewsContent

        titleEdit.setText(title, TextView.BufferType.EDITABLE)
        contentEdit.setText(content, TextView.BufferType.EDITABLE)

        thisView.buttonCrossEdit.setOnClickListener {
            dismiss()
        }


        thisView.buttonCheckEdit.setOnClickListener {
            patchThisNews(id, titleEdit.text.toString(), contentEdit.text.toString())
        }

        return thisView
    }

    fun patchThisNews(id: Int, title: String, content: String) {
        RetrofitClient.instance.patchNews(id, accessToken, title, content)
            .enqueue(object : Callback<News> {
                override fun onFailure(call: Call<News>, t: Throwable) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "RemoveDialogFragment response ${response.body()}"
                    )

                    if (response.code() == 401) {
                        justRefresh("addNewsDialogFragment")
                        patchThisNews(id, title, content)
                    }
                    if (response.code() == 404) {
                        Toast.makeText(context!!.applicationContext, "404", Toast.LENGTH_SHORT)
                            .show()
                    }
                    dismiss()
                }

            })
    }

}
