package com.passitwiki.passit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.sharedPreferences
import com.passitwiki.passit.adapter.SubjectsAdapter
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.Subject
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_subjects.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fragment that displays a list of clickable subject names.
 */
class SubjectsFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSubjects"
        fun newInstance(key: String): Fragment {
            val fragment = SubjectsFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * On creating the view - inflate it, when some item is selected on a top spinner,
     * it displays a list of subjects that come from a SubjectAdapter.
     * @return the inflated view with the listener
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_subjects, container, false)

        val listOfSemester =
            arrayOf(
                "Semester 1",
                "Semester 2",
                "Semester 3",
                "Semester 4",
                "Semester 5",
                "Semester 6",
                "Semester 7"
            )

        rootView.spinnerSemester!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val fosInt = sharedPreferences.getInt("current_fos_int", 0)
                    Log.d(
                        "MyTagExplicit",
                        "SubjectFragment: onCreateView: onItemSelectedListener: get current fieldOfStudyInt $fosInt"
                    )

                    showSubjectData(position, fosInt)

                    //WE OUT HERE CHECKING IF IT WORKS
//                    arguments.let {
//                        var fosInt = it?.getInt(FIELD_OF_STUDY_INT)
//                        Log.d("MyTag", fosInt.toString())
//                        fosInt = sharedPreferences
//                            .getInt("current_fos_int", 0)
//
//                        RetrofitClient.instance.getSubjects(position + 1, fosInt)
//                            .enqueue(object : Callback<List<Subject>> {
//                                override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
//                                    d("MyTag", "onFailure: $t")
//                                }
//
//                                override fun onResponse(
//                                    call: Call<List<Subject>>,
//                                    response: Response<List<Subject>>
//                                ) {
//                                    d("MyTag", "onResponseSub: ${response.body()}")
//                                    showData(response.body()!!)
//                                }
//                            })
//                    }
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

    /**
     * Get the data to populate the recyclerView, set the adapter
     */
    fun showSubjectData(position: Int, fosInt: Int) {
        RetrofitClient.instance.getSubjects(position + 1, fosInt)
            .enqueue(object : Callback<List<Subject>> {
                override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                    Log.d("MyTag", "onFailure: $t")
                }

                override fun onResponse(
                    call: Call<List<Subject>>,
                    response: Response<List<Subject>>
                ) {
                    Log.d("MyTagExplicitNetworking", "SubjectsFragment response ${response.body()}")

                    val listOfSubjects = response.body()!!
                    Log.d(
                        "MyTagExplicit",
                        "SubjectFragment: onCreateView: onItemSelectedListener: showSubjectData: onResponse: $listOfSubjects"
                    )

                    subjectsRecyclerView.apply {
                        layoutManager = LinearLayoutManager(activity!!.applicationContext)
                        adapter = SubjectsAdapter(listOfSubjects, requireActivity())
                    }
                }
            })
    }
}
