package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.SubjectsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Subject
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalSharedPreferences
import kotlinx.android.synthetic.main.fragment_subjects.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO description for both the fragment and its functions
class SubjectsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitClient.instance.getSubjects()
            .enqueue(object : Callback<List<Subject>> {
                override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                    d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Subject>>,
                    response: Response<List<Subject>>
                ) {
                    d("MyTag", "onResponse: ${response.body()}")
                    val listOfSubjects: ArrayList<Subject> =
                        response.body()!! as ArrayList<Subject>
                    val returnListOfSubjects: ArrayList<Subject> = ArrayList()

                    for (i in listOfSubjects) {
                        if (i.field_of_study == globalSharedPreferences!!.getString(
                                "current_fos",
                                "null_fos"
                            )
                        ) {
                            returnListOfSubjects.add(i)
                        }

                    }
                    returnListOfSubjects.sortBy { it.semester }
                    showData(returnListOfSubjects as List<Subject>)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }

    fun showData(subjects: List<Subject>) {
        subjectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(globalContext)
            adapter = SubjectsAdapter(subjects)
        }
    }
}
