package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.NewsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.News
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalToken
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO description for both the fragment and its functions
class DashboardFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        imageViewButtonSettings.setOnClickListener {
//            val userDialogFragment = SettingsFragment()
//            userDialogFragment.show(parentFragmentManager,"SettingsFragment")
//        }

        val bearerToken = "Bearer $globalToken"

        //TODO news adding logic

        RetrofitClient.instance.getNews(bearerToken)
            .enqueue(object : Callback<List<News>> {
                override fun onFailure(call: Call<List<News>>, t: Throwable) {
                    d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<News>>,
                    response: Response<List<News>>
                ) {
                    d("MyTag", "onResponse: ${response.body()}")
                    showData(response.body()!!)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

//        view.imageViewButtonSettings.setOnClickListener {
//            val userDialogFragment = SettingsFragment()
//            userDialogFragment.show(parentFragmentManager, "SettingsFragment")
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.frameLayoutMain, dashboardFragment)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit()
//        }


        return view
    }


    fun showData(users: List<News>) {
        newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(globalContext)
            adapter = NewsAdapter(users)
        }
    }

}
