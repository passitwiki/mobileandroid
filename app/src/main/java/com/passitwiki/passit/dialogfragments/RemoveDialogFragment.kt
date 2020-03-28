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
import kotlinx.android.synthetic.main.fragment_remove_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoveDialogFragment : DialogFragment() {

    companion object {
        const val KEY = "RemoveDialogFragment"
        const val ID_NEWS = "IdNews"
        fun newInstance(key: String, idNews: Int): DialogFragment {
            val fragment = RemoveDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putInt(ID_NEWS, idNews)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_remove_dialog, container, false)

        thisView.buttonCrossRemove.setOnClickListener {
            dismiss()
        }

        thisView.buttonCheckRemove.setOnClickListener {
            val id = arguments!!.getInt(ID_NEWS)
            deleteThisNews(id)
        }

        return thisView
    }

    fun deleteThisNews(id: Int) {
        RetrofitClient.instance.deleteNews(id, accessToken)
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
                        "RemoveDialogFragment response ${response.body()}"
                    )

                    if (response.code() == 401) {
                        justRefresh("addNewsDialogFragment")
                        deleteThisNews(id)
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