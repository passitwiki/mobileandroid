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
import kotlinx.android.synthetic.main.fragment_remove_news_dialog.view.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject


class RemoveNewsDialogFragment(private val key: String, private val idNews: Int) :
    DialogFragment() {
    private val repository: Repository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_remove_news_dialog, container, false)

        thisView.buttonCrossRemove.setOnClickListener {
            dismiss()
        }

        thisView.buttonCheckRemove.setOnClickListener {
            deleteThisNews(idNews)
        }

        return thisView
    }

    private fun deleteThisNews(id: Int) {
        var resource: Resource<Unit>? = null
        runBlocking {
            resource = repository.handleDeleteNews(id, accessToken)
        }
        when (resource!!.status) {
//                Status.LOADING -> null //TODO loading
            Status.ERROR -> {
                if (resource!!.message == "Unauthorised. Please refresh.") {
                    runBlocking {
                        Utilities(repository).justRefresh(key)
                        deleteThisNews(id)
                    }
                } else {
                    Toast.makeText(
                        activity!!.applicationContext,
                        resource!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Status.SUCCESS -> {
                Log.d(
                    "MyTagExplicitNetworking",
                    "RemoveNewsDialogFragment response ${resource.toString()}"
                )
                dismiss()
            }
        }
    }
}
