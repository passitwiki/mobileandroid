package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_individual_lecturer.view.*

class IndividualLecturerFragment : Fragment() {

    companion object {
        const val KEY = "FragmentIndividualLecturer"
        const val FULL_NAME = "FullName"
        const val DEGREE = "Degree"
        const val CONTACT = "Contact"
        const val CONSULTATIONS = "Consultations"
        fun newInstance(
            key: String,
            fullname: String,
            degree: String,
            contact: String,
            consultations: String
        ): Fragment {
            val fragment = IndividualLecturerFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putString(FULL_NAME, fullname)
            argument.putString(DEGREE, degree)
            argument.putString(CONTACT, contact)
            argument.putString(CONSULTATIONS, consultations)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_individual_lecturer, container, false)
        arguments.let {
            val fullname: String = it?.getString(FULL_NAME)!!
            val degree: String = it.getString(DEGREE)!!
            val contact: String = it.getString(CONTACT)!!
            val consultations: String = it.getString(CONSULTATIONS)!!
            view.textViewThisLecturer.text = fullname
            view.textViewLecturersDegree.text = degree
            view.textViewLecturersConsultations.text = contact
            view.textViewLecturersContact.text = consultations
        }



        return view
    }

}