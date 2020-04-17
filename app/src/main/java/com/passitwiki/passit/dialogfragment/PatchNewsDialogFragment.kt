package com.passitwiki.passit.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.model.News
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_patch_news_dialog.view.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject


class PatchNewsDialogFragment(
    private val key: String,
    private val title: String,
    private val content: String,
    private val idNews: Int
) : DialogFragment() {
    private val repository: Repository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater
            .inflate(R.layout.fragment_patch_news_dialog, container, false)

        val titleEdit = thisView.editTextEditNewsTitle
        val contentEdit = thisView.editTextEditNewsContent

        titleEdit.setText(title, TextView.BufferType.EDITABLE)
        contentEdit.setText(content, TextView.BufferType.EDITABLE)

        thisView.buttonCrossEdit.setOnClickListener {
            dismiss()
        }


        thisView.buttonCheckEdit.setOnClickListener {
            patchThisNews(idNews, titleEdit.text.toString(), contentEdit.text.toString())
        }

        return thisView
    }

    private fun patchThisNews(id: Int, title: String, content: String) {
        var resource: Resource<News>? = null
        runBlocking {
            resource = repository.handlePatchNews(accessToken, id, title, content)
        }
        when (resource!!.status) {
//                Status.LOADING -> null //TODO loading
            Status.ERROR -> {
                if (resource!!.message == "Unauthorised. Please refresh.") {
                    runBlocking {
                        Utilities(repository).justRefresh(key)
                        patchThisNews(id, title, content)
                    }
                }
            }
            Status.SUCCESS -> {
                if (resource!!.data == null) {
                    runBlocking {
                        Utilities(repository).justRefresh(key)
                        patchThisNews(id, title, content)
                    }
                } else {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "RemoveNewsDialogFragment response ${resource.toString()}"
                    )
                    dismiss()
                }
            }
        }
    }
}
