package com.passitwiki.passit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.sharedPreferences
import com.passitwiki.passit.adapter.SubjectsAdapter
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.viewmodel.SubjectsViewModel
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_subjects.view.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that displays a list of clickable subject names.
 */
class SubjectsFragment(private val key: String) : Fragment() {
    private val subjectsViewModel: SubjectsViewModel by viewModel()
    private val repository: Repository by inject()
    private val listOfSemester =
        arrayOf(
            "Semester 1",
            "Semester 2",
            "Semester 3",
            "Semester 4",
            "Semester 5",
            "Semester 6",
            "Semester 7"
        )

    /**
     * On creating the view - inflate it, when some item is selected on a top spinner,
     * it displays a list of subjects that come from a SubjectAdapter.
     * @return the inflated view with the listener
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater
            .inflate(R.layout.fragment_subjects, container, false)

        rootView.spinnerSemester!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val fosInt = sharedPreferences
                        .getInt("current_fos_int", 0)
                    Log.d(
                        "MyTagExplicit",
                        "SubjectFragment: onCreateView: " +
                                "onItemSelectedListener: get current fieldOfStudyInt $fosInt"
                    )

                    showSubjectData(position+1, fosInt)
                }
            }

        val arrayAdapter =
            ArrayAdapter(
                requireActivity().applicationContext,
                android.R.layout.simple_spinner_item,
                listOfSemester
            )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        rootView.spinnerSemester.adapter = arrayAdapter

        return rootView
    }

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    fun showSubjectData(position: Int, fosInt: Int) {
        subjectsViewModel.loadSubjects(position, fosInt)
            .observe(viewLifecycleOwner, Observer { resource ->
                when (resource.status) {
//                Status.LOADING -> null //TODO loading
                    Status.ERROR -> Toast.makeText(
                        requireActivity().applicationContext,
                        resource.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Status.SUCCESS -> {
                        if (resource.data == null) {
                            runBlocking {
                                Utilities(repository).justRefresh(key)
                                showSubjectData(position, fosInt)
                            }
                        } else {
                            Log.d(
                                "MyTagExplicitNetworking",
                                "SubjectsFragment response ${resource.data}"
                            )

                            val listOfSubjects = resource.data
                            Log.d(
                                "MyTagExplicit",
                                "SubjectFragment: onCreateView: " +
                                        "onItemSelectedListener: showSubjectData: " +
                                        "onResponse: $listOfSubjects"
                            )

                            subjectsRecyclerView.apply {
                                layoutManager = LinearLayoutManager(requireActivity().applicationContext)
                                adapter = SubjectsAdapter(
                                    listOfSubjects,
                                    requireActivity()
                                )
                            }
                        }
                    }
                }
            })
    }
}
