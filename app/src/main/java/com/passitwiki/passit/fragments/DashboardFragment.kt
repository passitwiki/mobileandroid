package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.activities.activeFragment
import com.passitwiki.passit.activities.dashboardFragment
import com.passitwiki.passit.adapter.NewsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.dialogfragments.AddNewsDialogFragment
import com.passitwiki.passit.models.News
import com.passitwiki.passit.tools.justRefresh
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fragment that displays a list of news, allows refresh and can open a calendar.
 */
class DashboardFragment : Fragment() {

    companion object {
        const val KEY = "FragmentDashboard"
        const val FIELD_AGE_GROUP = "FieldAgeGroup"
        fun newInstance(key: String, fieldAgeGroupInt: Int): Fragment {
            val fragment = DashboardFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putInt(FIELD_AGE_GROUP, fieldAgeGroupInt)
            fragment.arguments = argument
            return fragment
        }
    }

    var news: MutableList<News> = ArrayList()



    /**
     * On creating the view - inflate it, make a list that holds news, prepare the 2 buttons,
     * set a swipeListener, and populate the recycler with news.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        news = ArrayList()
        val fieldAgeGroupInt = arguments!!.getInt(FIELD_AGE_GROUP)

        val addNewsButton = view.imageViewAddNews
        addNewsButton.setOnClickListener {
            val addNewsDialogFragment =
                AddNewsDialogFragment.newInstance("AddNews", fieldAgeGroupInt)
            addNewsDialogFragment.show(fragmentManager!!, "addNews")
        }

        view.imageViewSearchNews.setOnClickListener {
            if (view.searchViewButton.visibility == View.GONE) {
                view.searchViewButton.visibility = View.VISIBLE
            } else {
                view.searchViewButton.visibility = View.GONE
            }
        }

        view.buttonExam.setOnClickListener {
            replaceWithCalendarFragment()
        }

        val dashboardAdapter = NewsAdapter(news, this@DashboardFragment)

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
            Log.d("MyTagExplicit", "onRefresh called from SwipeRefreshLayout")
            dashboardFragment = newInstance("Dashboard", fieldAgeGroupInt)
            activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutMain, dashboardFragment, "Dashboard").commit()
            activity!!.supportFragmentManager.beginTransaction()
                .remove(activeFragment)
                .show(dashboardFragment)
                .commit()
            activeFragment = dashboardFragment
        }
        Log.d(
            "MyTa",
            "DashboardFragment: onCreateView: checking accessToken before init of showNewsData: $accessToken"
        )
        showNewsData(accessToken, dashboardAdapter)

        return view
    }

    /**
     * Create a calendar fragment, hide this one and show that one.
     */
    fun replaceWithCalendarFragment() {
        arguments.let {
            val calendarFragment = CalendarFragment.newInstance(accessToken, "Calendar")
            activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutMain, calendarFragment, "Calendar").hide(calendarFragment)
                .commit()
            activity!!.supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(calendarFragment)
                .commit()
            activeFragment = calendarFragment

        }
    }


    /**
     * Get the data to populate the recyclerView with news.
     */
    fun showNewsData(access: String, adapterOfNews: NewsAdapter) {
        RetrofitClient.instance.getNews(access)
            .enqueue(object : Callback<List<News>> {
                override fun onFailure(call: Call<List<News>>, t: Throwable) {
                    Log.d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<News>>,
                    response: Response<List<News>>
                ) {
                    Log.d(
                        "MyTaNetworking",
                        "DashboardFragment response ${response.body()}"
                    )
                    Log.d(
                        "MyTagExplicitNetworking",
                        "DashboardFragment accessToken it was initialised with: $access"
                    )

                    val newsList = response.body()
                    if (newsList == null) {
                        justRefresh("dashboardFragment")
                        showNewsData(accessToken, adapterOfNews)
                    } else {
                        Log.d(
                            "MyTa",
                            "DashboardFragment: onCreateView: showNewsData: onResponse: $newsList"
                        )

                        if (news.isEmpty() || newsList != news) {
                            news.addAll(newsList)
                            newsRecyclerView.apply {
                                layoutManager = LinearLayoutManager(activity!!.applicationContext)
                                adapter = adapterOfNews
                            }
                        }
                    }

                }
            })
    }

}
