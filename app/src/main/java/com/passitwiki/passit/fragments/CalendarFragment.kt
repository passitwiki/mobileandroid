package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.adapter.CalendarAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Event
import com.passitwiki.passit.tools.justRefresh
import kotlinx.android.synthetic.main.fragment_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fragment that displays a list of events.
 */
class CalendarFragment : Fragment() {

    companion object {
        const val KEY = "FragmentDashboard"
        const val ACCESS_TOKEN = "AccessToken"
        fun newInstance(token: String, key: String): Fragment {
            val fragment = CalendarFragment()
            val argument = Bundle()
            argument.putString(ACCESS_TOKEN, token)
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * On creating the view - inflate it and populate with a list of events.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        showCalendarData(accessToken)

        val toolbarTextView = activity!!.findViewById<TextView>(R.id.textViewToolbar)
        toolbarTextView.text = getString(R.string.exam_calendar)

        return view
    }

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    fun showCalendarData(access: String) {
        RetrofitClient.instance.getEvents(access)
            .enqueue(object : Callback<List<Event>> {
                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    Log.d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
                ) {
                    Log.d("MyTagExplicitNetworking", "CalendarFragment response ${response.body()}")
                    if (response.body() == null) {
                        justRefresh("calendarFragment")
                        showCalendarData(accessToken)
                    }
                    val calendarList = response.body()!!
                    Log.d(
                        "MyTagExplicit",
                        "CalendarFragment: onCreateView: showCalendarData: onResponse: $calendarList"
                    )
                    eventRecyclerView.apply {
                        layoutManager = LinearLayoutManager(activity!!.applicationContext)
                        adapter = CalendarAdapter(calendarList)
                    }

                }
            })

    }
}
