package com.passitwiki.passit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.LecturersAdapter
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.viewmodel.LecturersViewModel
import kotlinx.android.synthetic.main.fragment_lecturers.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that displays a list of clickable lecturer names.
 */
class LecturersFragment(val key: String) : Fragment() {
    private val lecturersViewModel: LecturersViewModel by viewModel()
    private val repository: Repository by inject()

    /**
     * On creating the view - inflate it and populate with a list of lecturers.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lecturers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLecturerData()
    }

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    private fun showLecturerData() {
        lecturersViewModel.loadLecturers().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
//                Status.LOADING -> null //TODO loading
                Status.ERROR -> Toast.makeText(
                    activity!!.applicationContext,
                    resource.message,
                    Toast.LENGTH_SHORT
                ).show()
                Status.SUCCESS -> {
                    if (resource.data == null) {
                        runBlocking {
                            Utilities(repository).justRefresh(key)
                            showLecturerData()
                        }
                    } else {
                        val lecturersList = resource.data
                        Log.d(
                            "MyTagExplicit",
                            "LecturersFragment: onCreateView: " +
                                    "showLecturerData: onResponse: $lecturersList"
                        )
                        lecturersRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity!!.applicationContext)
                            adapter = LecturersAdapter(lecturersList, activity!!)
                        }
                    }
                }
            }
        })
    }
}
