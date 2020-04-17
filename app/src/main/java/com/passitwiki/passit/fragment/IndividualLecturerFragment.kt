package com.passitwiki.passit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_individual_lecturer.view.*

/**
 * Fragment that displays an individual, chosen lecturer.
 */
class IndividualLecturerFragment(
    private val key: String,
    private val fullName: String,
    private val degree: String,
    private val contact: String,
    private val consultations: String
) : Fragment() {

    /**
     * On creating the view - inflate it and set a few basic texts.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater
            .inflate(R.layout.fragment_individual_lecturer, container, false)
        view.textViewThisLecturer.text = fullName
        view.textViewLecturersDegree.text = degree
        view.textViewLecturersConsultations.text = contact
        view.textViewLecturersContact.text = consultations
        return view
    }

}