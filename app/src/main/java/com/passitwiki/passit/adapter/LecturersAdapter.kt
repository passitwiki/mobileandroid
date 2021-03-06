package com.passitwiki.passit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.activeFragment
import com.passitwiki.passit.fragment.IndividualLecturerFragment
import com.passitwiki.passit.model.Lecturer
import kotlinx.android.synthetic.main.item_lecturer.view.*

/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param lecturers a list object from a json array made with Moshi
 */
class LecturersAdapter(private val lecturers: List<Lecturer>, context: FragmentActivity) :
    RecyclerView.Adapter<LecturersAdapter.LecturerViewHolder>() {

    val activity: FragmentActivity = context

    /**
     * This fun inflates - makes xml a workable object
     * @return a convenient object you can directly change values etc. in
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturerViewHolder {
        val lecturerRow = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lecturer, parent, false)
        return LecturerViewHolder(
            lecturerRow
        )
    }

    /**
     * @return how many objects in the list there are
     */
    override fun getItemCount() = lecturers.size

    /**
     * Does the intended repeated work. Binds one "row" and a corresponding object.
     * @param holder ViewHolder with its workable xml properties
     * @param position the number of the object you're working with
     */
    override fun onBindViewHolder(holder: LecturerViewHolder, position: Int) {
        val lecturer = lecturers[position]
        val fullNameString = "${lecturer.last_name} ${lecturer.first_name}"

        holder.fullName.text = fullNameString

        holder.button.setOnClickListener {
            val individualLecturer =
                IndividualLecturerFragment(
                    "Calendar",
                    fullNameString,
                    lecturer.title,
                    lecturer.contact,
                    lecturer.consultations
                )
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.frameLayoutMain, individualLecturer, "IndividualLecturer")
                .hide(individualLecturer)
                .commit()
            activity.supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(individualLecturer)
                .commit()
            activeFragment = individualLecturer
        }

    }

    /**
     * The class that holds the corresponding View parts and xml parts,
     * so there is a true object from the xml layout.
     */
    class LecturerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullName: TextView = itemView.textViewLecturersNameFull
        val button: RelativeLayout = itemView.buttonLecturer
    }

}

