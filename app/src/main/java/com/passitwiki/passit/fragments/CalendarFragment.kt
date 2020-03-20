package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.CalendarAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Event
import kotlinx.android.synthetic.main.fragment_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        arguments.let {
            val accessToken = it?.getString(ACCESS_TOKEN)

            RetrofitClient.instance.getEvents(accessToken!!)
                .enqueue(object : Callback<List<Event>> {
                    override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                        d("MyTag", "onFailure: $t")
                    }

                    override fun onResponse(
                        call: Call<List<Event>>,
                        response: Response<List<Event>>
                    ) {
                        d("MyTag", "onResponse: ${response.body()}")
                        showData(response.body()!!)

                    }
                })

            val test = activity!!.findViewById<TextView>(R.id.textViewToolbar)
            test.text = "Exams"
        }
        return view
    }

    fun showData(events: List<Event>) {
        eventRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = CalendarAdapter(events)
        }
    }
}
