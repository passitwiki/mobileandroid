package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.LecturerAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Lecturer
import com.passitwiki.passit.tools.globalContext
import kotlinx.android.synthetic.main.fragment_lecturers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//TODO description for both the fragment and its functions
class LecturersFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.instance.getLecturers()
            .enqueue(object : Callback<List<Lecturer>> {
                override fun onFailure(call: Call<List<Lecturer>>, t: Throwable) {
                    d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Lecturer>>,
                    response: Response<List<Lecturer>>
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
        return inflater.inflate(R.layout.fragment_lecturers, container, false)
    }

    fun showData(lecturers: List<Lecturer>) {

        lecturersRecyclerView.apply {
            layoutManager = LinearLayoutManager(globalContext)
            adapter = LecturerAdapter(lecturers)
        }
    }


}
