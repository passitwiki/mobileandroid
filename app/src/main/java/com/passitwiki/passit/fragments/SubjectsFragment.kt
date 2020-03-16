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
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_subjects.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubjectsFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSubjects"
        const val ACCESS_TOKEN = "AccessToken"
        const val FIELD_OF_STUDY = "FieldOfStudy"
        fun newInstance(token: String, key: String, fos: String): Fragment {
            val fragment = SubjectsFragment()
            val argument = Bundle()
            argument.putString(ACCESS_TOKEN, token)
            argument.putString(KEY, key)
            argument.putString(FIELD_OF_STUDY, fos)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_subjects, container, false)

        val listOfSemester =
            arrayOf("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5")

        rootView.spinnerSemester!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    RetrofitClient.instance.getSubjectsBySemester(position + 1)
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
                                    arguments.let {
                                        val fos = it?.getString(FIELD_OF_STUDY)
                                        if (i.field_of_study == fos) {
                                            returnListOfSubjects.add(i)
                                        }
                                    }
                                }
                                showData(returnListOfSubjects as List<Subject>)
                            }
                        })
                }
            }

        val arrayAdapter =
            ArrayAdapter(
                activity!!.applicationContext,
                android.R.layout.simple_spinner_item,
                listOfSemester
            )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        rootView.spinnerSemester.adapter = arrayAdapter

        return rootView
    }

    fun showData(subjects: List<Subject>) {
        subjectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity!!.applicationContext)
            adapter = SubjectsAdapter(subjects)
        }
    }
}
