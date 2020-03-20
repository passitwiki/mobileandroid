package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.activeFragment
import com.passitwiki.passit.activities.dashboardFragment
import com.passitwiki.passit.adapter.NewsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.dialogfragments.AddNewsDialogFragment
import com.passitwiki.passit.models.News
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    companion object {
        const val KEY = "FragmentDashboard"
        const val ACCESS_TOKEN = "AccessToken"
        const val FIELD_AGE_GROUP = "FieldAgeGroup"
        fun newInstance(token: String, key: String, fag: Int): Fragment {
            val fragment = DashboardFragment()
            val argument = Bundle()
            argument.putString(ACCESS_TOKEN, token)
            argument.putString(KEY, key)
            argument.putInt(FIELD_AGE_GROUP, fag)
            fragment.arguments = argument
            return fragment
        }
    }

    var news: MutableList<News> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        news = ArrayList()


        arguments.let {

            val accessToken = it?.getString(ACCESS_TOKEN)
            val addNewsButton = view.imageViewAddNews
            val fag = it?.getInt(FIELD_AGE_GROUP)

            addNewsButton.setOnClickListener {
                val addNewsDialogFragment =
                    AddNewsDialogFragment.newInstance(accessToken!!, "AddNews", fag!!)
                addNewsDialogFragment.show(fragmentManager!!, "addNews")
            }

            view.buttonExam.setOnClickListener {
                replaceWithCalendarFragment()
            }

            view.swipeRefreshDashboard.setOnRefreshListener {
                Log.d("SWIPE", "onRefresh called from SwipeRefreshLayout")
                dashboardFragment = DashboardFragment.newInstance(accessToken!!, "Dashboard", fag!!)
                activity!!.supportFragmentManager.beginTransaction()
                    .add(R.id.frameLayoutMain, dashboardFragment, "Dashboard").commit()
                activity!!.supportFragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(dashboardFragment)
                    .commit()
                activeFragment = dashboardFragment
            }

        }

        display()



        return view
    }


    fun replaceWithCalendarFragment() {
        arguments.let {
            val accessToken = it?.getString(ACCESS_TOKEN)
            val calendarFragment = CalendarFragment.newInstance(accessToken!!, "Calendar")
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

    fun showData(news: List<News>) {
        newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = NewsAdapter(news)
        }
    }

    fun display() {
        arguments.let {
            val accessToken = it?.getString(ACCESS_TOKEN)

            RetrofitClient.instance.getNews(accessToken!!)
                .enqueue(object : Callback<List<News>> {
                    override fun onFailure(call: Call<List<News>>, t: Throwable) {
                        d("MyTag", "onFailure: $t")
                    }

                    override fun onResponse(
                        call: Call<List<News>>,
                        response: Response<List<News>>
                    ) {
                        d("MyTag", "onResponse: ${response.body()}")
                        if (response.body() == null) {
                            Toast.makeText(
                                    activity!!.applicationContext,
                                    "Something went wrong",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        } else {
                            if (news.isEmpty() || response.body()!! != news) {
                                news.addAll(response.body()!!)
                                showData(response.body()!!)
                            }
                        }
                    }
                })
        }
    }

}
