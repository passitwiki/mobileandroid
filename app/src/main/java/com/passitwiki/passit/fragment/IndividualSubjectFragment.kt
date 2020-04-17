package com.passitwiki.passit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_individual_subject.view.*

/**
 * Fragment that displays an individual, chosen subject.
 */
class IndividualSubjectFragment(
    private val key: String,
    private val subjectName: String,
    private val description: String
) : Fragment() {


    /**
     * On creating the view - inflate it and set a few basic texts.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_individual_subject, container, false)

        view.textViewThisSubject.text = subjectName
        view.textViewSubjectDescription.text = description

        return view
    }
}
