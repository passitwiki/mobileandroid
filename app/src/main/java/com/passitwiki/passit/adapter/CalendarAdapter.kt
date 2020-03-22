package com.passitwiki.passit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.activeFragment
import com.passitwiki.passit.models.Event
import kotlinx.android.synthetic.main.item_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param events a list object from a json array made with Gson
 */
class CalendarAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<CalendarAdapter.EventViewHolder>() {

    val days = arrayOf(
        activeFragment.getString(R.string.day1),
        activeFragment.getString(R.string.day2),
        activeFragment.getString(R.string.day3),
        activeFragment.getString(R.string.day4),
        activeFragment.getString(R.string.day5),
        activeFragment.getString(R.string.day6),
        activeFragment.getString(R.string.day7)
    )
    val months = arrayOf(
        activeFragment.getString(R.string.month1),
        activeFragment.getString(R.string.month2),
        activeFragment.getString(R.string.month3),
        activeFragment.getString(R.string.month4),
        activeFragment.getString(R.string.month5),
        activeFragment.getString(R.string.month6),
        activeFragment.getString(R.string.month7),
        activeFragment.getString(R.string.month8),
        activeFragment.getString(R.string.month9),
        activeFragment.getString(R.string.month10),
        activeFragment.getString(R.string.month11),
        activeFragment.getString(R.string.month12)
    )

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


        //TODO work with dates
        val dateString = event.due_date
        val calendar = Calendar.getInstance()
//        val formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss'Z'")
//        val dateObject = LocalDate.parse(dateString, formatter)
        val formatter = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'", Locale("pl", "PL"))
        calendar.time = formatter.parse(dateString)!!

        val dayInTheMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dayInTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK) - 1]

        val timeOfDay = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

        holder.name.text = event.name
        holder.subject.text = event.subjectGroup.toString()
        holder.time.text = timeOfDay
        holder.content.text = event.description
        holder.date.text = "$dayInTheWeek\n$dayInTheMonth"
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

