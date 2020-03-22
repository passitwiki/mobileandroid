package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_individual_subject.view.*

/**
 * Fragment that displys an individual, chosen subject.
 */
class IndividualSubjectFragment : Fragment() {

    companion object {
        const val KEY = "FragmentIndividualSubject"
        const val SUBJECT_NAME = "SubjectName"
        const val DESCRIPTION = "Description"
        fun newInstance(
            key: String,
            subjectName: String,
            description: String
        ): Fragment {
            val fragment = IndividualSubjectFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putString(SUBJECT_NAME, subjectName)
            argument.putString(DESCRIPTION, description)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * On creating the view - inflate it and set a few basic texts.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_individual_subject, container, false)
        arguments.let {
            val subjectName: String = it?.getString(SUBJECT_NAME)!!
            val description: String = it.getString(DESCRIPTION)!!
            view.textViewThisSubject.text = subjectName
            view.textViewSubjectDescription.text = description
        }

        return view
    }
}
