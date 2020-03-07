package com.passitwiki.passit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.models.Subject
import kotlinx.android.synthetic.main.item_subject.view.*


/**
 * An adapter class that adapts repeatedly a recieved JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param subjects a list object from a json array made with Gson
 */
class SubjectsAdapter(private val subjects: List<Subject>) :
    RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>() {

    /**
     * This fun inflates - makes xml a workable object
     * @return a convenient object you can directly change values etc. in
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectsViewHolder {
        val subjectsRow = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return SubjectsViewHolder(subjectsRow)
    }

    /**
     * @return how many objects in the list there are
     */
    override fun getItemCount() = subjects.size

    /**
     * Does the intended repeated work. Binds one "row" and a corresponding object.
     * @param holder ViewHolder with its workable xml properties
     * @param position the number of the object you're working with
     */
    override fun onBindViewHolder(holder: SubjectsViewHolder, position: Int) {
        val subject = subjects[position]
        holder.name.text = subject.name

//        val descText =
//            if (subject.general_desctiption != "null") "Description: ${subject.general_desctiption}" else "No desription yet."
//        val semAndFieldText = "FoS: ${subject.field_of_study}; Sem: ${subject.semester}"
//        holder.description.text = descText
//        holder.semesterAndField.text = semAndFieldText
    }

    /**
     * The class that holds the corresponding View parts and xml parts,
     * so there is a true object from the xml layout.
     */
    class SubjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.textViewSubjectsName
//        val description = itemView.textViewSubjectsDescription
//        val semesterAndField = itemView.textViewSubjectsSemesterAndField
    }
}
