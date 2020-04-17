package com.passitwiki.passit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.fragment.CalendarFragment
import com.passitwiki.passit.model.Event
import kotlinx.android.synthetic.main.item_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param events a list object from a json array made with Moshi
 */
class CalendarAdapter(
    private val events: List<Event>,
    calendarFragment: CalendarFragment
) : RecyclerView.Adapter<CalendarAdapter.EventViewHolder>() {

    //This looks dumb, but its purpose is its ability to be locally translated.
    private val days = arrayOf(
        calendarFragment.getString(R.string.day1),
        calendarFragment.getString(R.string.day2),
        calendarFragment.getString(R.string.day3),
        calendarFragment.getString(R.string.day4),
        calendarFragment.getString(R.string.day5),
        calendarFragment.getString(R.string.day6),
        calendarFragment.getString(R.string.day7)
    )
    private val months = arrayOf(
        calendarFragment.getString(R.string.month1),
        calendarFragment.getString(R.string.month2),
        calendarFragment.getString(R.string.month3),
        calendarFragment.getString(R.string.month4),
        calendarFragment.getString(R.string.month5),
        calendarFragment.getString(R.string.month6),
        calendarFragment.getString(R.string.month7),
        calendarFragment.getString(R.string.month8),
        calendarFragment.getString(R.string.month9),
        calendarFragment.getString(R.string.month10),
        calendarFragment.getString(R.string.month11),
        calendarFragment.getString(R.string.month12)
    )
    private var lastMonth = 0

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
        val dateString = event.due_date
        val calendar = Calendar.getInstance()
        val actualCurrentDate = calendar
        val formatter = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z'", Locale("pl", "PL"))
        calendar.time = formatter.parse(dateString)!!

        val monthEvent = calendar.get(Calendar.MONTH)

        if (monthEvent > lastMonth) {
            holder.monthTextView.visibility = View.VISIBLE
            holder.monthTextView.text = months[monthEvent]
            lastMonth = monthEvent
        }


        val dayInTheMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dayInTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK) - 1]

        val minutes: String =
            if (calendar.get(Calendar.MINUTE) < 10) "0${calendar.get(Calendar.MINUTE)}" else calendar.get(
                Calendar.MINUTE
            ).toString()

        val timeOfDay = "${calendar.get(Calendar.HOUR_OF_DAY)}:$minutes"

        holder.name.text = event.name
        holder.subject.text = "SAG: ${event.subject_group.toString()}"
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

        //        val monthRelative: RelativeLayout = itemView.relativeLayoutMonth
        val monthTextView: TextView = itemView.textViewMonth
    }

}

