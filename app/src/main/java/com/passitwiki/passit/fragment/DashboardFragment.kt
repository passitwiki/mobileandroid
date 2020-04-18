package com.passitwiki.passit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.activity.activeFragment
import com.passitwiki.passit.adapter.DashboardNewsAdapter
import com.passitwiki.passit.dialogfragment.AddNewsDialogFragment
import com.passitwiki.passit.model.News
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.utilities.refreshDashboardFragment
import com.passitwiki.passit.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Fragment that displays a list of news, allows refresh and can open a calendar.
 */
class DashboardFragment(private val key: String, private val fieldAgeGroupInt: Int) : Fragment() {
    private val dashboardViewModel: DashboardViewModel by viewModel()
    private val repository: Repository by inject()
    private val news: MutableList<News> = ArrayList()
//    private val dashboardFragment: DashboardFragment by inject()
//    private val calendarFragment: CalendarFragment by inject()

    /**
     * On creating the view - inflate it, make a list that holds news, prepare the 2 buttons,
     * set a swipeListener, and populate the recycler with news.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_dashboard, container, false)
        val addNewsButton = view.imageViewAddNews
        addNewsButton.setOnClickListener {
            val addNewsDialogFragment =
                AddNewsDialogFragment("AddNews", fieldAgeGroupInt)
            addNewsDialogFragment.show(requireFragmentManager(), "addNews")
            //TODO try with childfragmentmanager
        }

        view.imageViewSearchNews.setOnClickListener {
            when {
                view.relativeLayoutForBothSearchAndFilter.visibility == View.GONE -> {
                    view.relativeLayoutForBothSearchAndFilter.visibility = View.VISIBLE
                    view.relativeLayoutForSearchView.visibility = View.VISIBLE
                    view.spinnerFilter.visibility = View.GONE
                }
                view.spinnerFilter.visibility == View.VISIBLE -> {
                    view.spinnerFilter.visibility = View.GONE
                    view.relativeLayoutForSearchView.visibility = View.VISIBLE
                }
                else -> {
                    view.relativeLayoutForSearchView.visibility = View.GONE
                    view.relativeLayoutForBothSearchAndFilter.visibility = View.GONE
                }
            }
        }
        view.imageViewFilter.setOnClickListener {
            when {
                view.relativeLayoutForBothSearchAndFilter.visibility == View.GONE -> {
                    view.relativeLayoutForBothSearchAndFilter.visibility = View.VISIBLE
                    view.relativeLayoutForSearchView.visibility = View.GONE
                    view.spinnerFilter.visibility = View.VISIBLE
                }
                view.relativeLayoutForSearchView.visibility == View.VISIBLE -> {
                    view.spinnerFilter.visibility = View.VISIBLE
                    view.relativeLayoutForSearchView.visibility = View.GONE
                }
                else -> {
                    view.spinnerFilter.visibility = View.GONE
                    view.relativeLayoutForBothSearchAndFilter.visibility = View.GONE
                }
            }
        }



        view.buttonExam.setOnClickListener {
            replaceWithCalendarFragment()
        }

        val dashboardAdapter = DashboardNewsAdapter(news, this@DashboardFragment)

//        var listOfSubjectGroup = ArrayList<SubjectGroup>()
//        var displayListOfSubjectGroup = ArrayList<String>()
//        runBlocking {
//            listOfSubjectGroup = getAllTheSubjects()
//        }
//        listOfSubjectGroup.forEach{
//            displayListOfSubjectGroup.add(it.subject_name)
//        }
//        val spinnerAdapter = ArrayAdapter(
//            requireActivity().applicationContext,
//            android.R.layout.simple_spinner_item,
//            displayListOfSubjectGroup
//        )
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        view.spinnerSubjectGroupAdd.adapter = spinnerAdapter
//
//        view.spinnerSubjectGroupAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                chosenSubject = listOfSubjectGroup[position]
//            }
//        }


        view.searchViewNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dashboardAdapter.filter.filter(newText)
                return false
            }

        })

        view.swipeRefreshDashboard.setOnRefreshListener {
            refreshDashboardFragment(key, fieldAgeGroupInt)
        }
        Log.d(
            "MyTa",
            "DashboardFragment: onCreateView: " +
                    "checking accessToken before init of showNewsData: $accessToken"
        )
        showNewsData(dashboardAdapter)

        return view
    }

    /**
     * Create a calendar fragment, hide this one and show that one.
     */
    private fun replaceWithCalendarFragment() {
        val calendarFragment = CalendarFragment("Calendar")
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.frameLayoutMain, calendarFragment, "Calendar")
            .hide(calendarFragment).commit()
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .show(calendarFragment)
            .commit()
        activeFragment = calendarFragment


    }


    /**
     * Get the data to populate the recyclerView with news.
     */
    private fun showNewsData(adapterOfDashboardNews: DashboardNewsAdapter) {
        dashboardViewModel.loadNews().observe(viewLifecycleOwner, Observer { resource ->
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
                            showNewsData(adapterOfDashboardNews)
                        }
                    } else {
                        val newsList = resource.data
                        Log.d(
                            "MyTa",
                            "DashboardFragment: onCreateView: " +
                                    "showNewsData: onResponse: $newsList"
                        )

                        if (news.isEmpty() || newsList != news) {
                            news.addAll(newsList)
                            newsRecyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(requireActivity().applicationContext)
                                adapter = adapterOfDashboardNews
                            }
                        }
                    }
                }
            }
        })
    }
}

