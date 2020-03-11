package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R

/**
 * A simple [Fragment] subclass.
 * Use the [IndividualSubjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IndividualSubjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_individual_subject, container, false)


        return view
    }
}
