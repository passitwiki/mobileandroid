package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.adapter.SubjectsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Subject
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalSharedPreferences
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_subjects.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//TODO description for both the fragment and its functions
class SubjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        val listOfSemester =
            arrayOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5")

        view.spinnerSemester!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    globalSharedPreferences!!.edit().putString("curent_semester", "yes")
                        .apply()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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
                                        ) && i.semester == position + 1
                                    ) {
                                        returnListOfSubjects.add(i)
                                    }

                                }
                                returnListOfSubjects.sortBy { it.semester }
                                showData(returnListOfSubjects as List<Subject>)
                            }
                        })
                }
            }

        val array_adapter =
            ArrayAdapter(globalContext!!, android.R.layout.simple_spinner_item, listOfSemester)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        view.spinnerSemester.adapter = array_adapter

        return view
    }

    fun showData(subjects: List<Subject>) {
        subjectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(globalContext)
            adapter = SubjectsAdapter(subjects)
        }
    }
}
