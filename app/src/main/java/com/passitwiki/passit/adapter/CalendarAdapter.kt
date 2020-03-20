package com.passitwiki.passit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.models.Event
import kotlinx.android.synthetic.main.item_calendar.view.*


/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param events a list object from a json array made with Gson
 */
class CalendarAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<CalendarAdapter.EventViewHolder>() {

    /**
     * This fun inflates - makes xml a workable object
     * @return a convenient object you can directly change values etc. in
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val eventRow = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_calendar, parent, false)
        return EventViewHolder(eventRow)
    }

    /**
     * @return how many objects in the list there are
     */
    override fun getItemCount() = events.size

    /**
     * Does the intended repeated work. Binds one "row" and a corresponding object.
     * @param holder ViewHolder with its workable xml properties
     * @param position the number of the object you're working with
     */
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        Log.d("MyTah", event.toString())

        val date = event.due_date.toString()
        val day = date.substring(8, 10) + ".\n" + date.substring(5, 7) + "."
        val time = date.substring(11, 16)

        holder.name.text = event.name
        holder.subject.text = event.subjectGroup.toString()
        holder.time.text = time
        holder.content.text = event.description
        holder.date.text = day
    }

    /**
     * The class that holds the corresponding View parts and xml parts,
     * so there is a true object from the xml layout.
     */
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.textViewCalendarName
        val subject: TextView = itemView.textViewCalendarSubject
        val time: TextView = itemView.textViewCalendarTime
        val content: TextView = itemView.textViewCalendarDescription
        val date: TextView = itemView.textViewDate
    }

}

