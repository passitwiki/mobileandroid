package com.passitwiki.passit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.CalendarAdapter
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.viewmodel.CalendarViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that displays a list of events.
 */
class CalendarFragment(private val key: String) : Fragment() {
    private val calendarViewModel: CalendarViewModel by viewModel()
    private val repository: Repository by inject()

    /**
     * On creating the view - inflate it and populate with a list of events.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val toolbarTextView = requireActivity().findViewById<TextView>(R.id.textViewToolbar)
        toolbarTextView.text = getString(R.string.exam_calendar)
        //TODO make the tpo text display like this for all of them
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCalendarData()
    }

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    private fun showCalendarData() {
        calendarViewModel.loadEvents().observe(viewLifecycleOwner, Observer { resource ->
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
                            showCalendarData()
                        }
                    } else {
                        Log.d(
                            "MyTagExplicit",
                            "CalendarFragment: onCreateView: showCalendarData: onResponse: ${resource.data}"
                        )
                        eventRecyclerView.apply {
                            layoutManager =
                                LinearLayoutManager(requireActivity().applicationContext)
                            adapter = CalendarAdapter(resource.data, this@CalendarFragment)
                        }
                    }
                }
            }
        })
    }
}
