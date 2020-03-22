package com.passitwiki.passit.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_remove_dialog.view.*


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

//        thisView.buttonCheckRemove.setOnClickListener {
////            val id
//        }

        return thisView
    }

}
