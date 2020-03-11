package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R


/**
 * A simple [Fragment] subclass.
 * Use the [IndividualLecturerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IndividualLecturerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_individual_lecturer, container, false)

        return view
    }

}