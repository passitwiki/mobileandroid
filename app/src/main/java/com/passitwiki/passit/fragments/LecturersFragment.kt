package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.LecturerAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Lecturer
import kotlinx.android.synthetic.main.fragment_lecturers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fragment that displays a list of clickable lecturer names.
 */
class LecturersFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSLecturers"
        fun newInstance(key: String): Fragment {
            val fragment = LecturersFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * On creating the view - inflate it and populate with a list of lecturers.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lecturers, container, false)

        showLecturerData()

        return view
    }

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    fun showLecturerData() {
        RetrofitClient.instance.getLecturers()
            .enqueue(object : Callback<List<Lecturer>> {
                override fun onFailure(call: Call<List<Lecturer>>, t: Throwable) {
                    Log.d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Lecturer>>,
                    response: Response<List<Lecturer>>
                ) {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "LecturersFragment response ${response.body()}"
                    )

                    val lecturersList = response.body()!!
                    Log.d(
                        "MyTagExplicit",
                        "LecturersFragment: onCreateView: showLecturerData: onResponse: $lecturersList"
                    )
                    lecturersRecyclerView.apply {
                        layoutManager = LinearLayoutManager(activity!!.applicationContext)
                        adapter = LecturerAdapter(lecturersList, activity!!)
                    }
                }
            })
    }
}
