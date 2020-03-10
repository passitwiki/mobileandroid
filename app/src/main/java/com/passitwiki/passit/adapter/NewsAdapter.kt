package com.passitwiki.passit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.models.News
import kotlinx.android.synthetic.main.item_news.view.*


/**
 * An adapter class that adapts repeatedly a recieved JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param news a list object from a json array made with Gson
 */
class NewsAdapter(private val news: List<News>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    /**
     * This fun inflates - makes xml a workable object
     * @return a convenient object you can directly change values etc. in
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsRow = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(newsRow)
    }

    /**
     * @return how many objects in the list there are
     */
    override fun getItemCount() = news.size

    /**
     * Does the intended repeated work. Binds one "row" and a corresponding object.
     * @param holder ViewHolder with its workable xml properties
     * @param position the number of the object you're working with
     */
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val decreasingOrderPosition = itemCount - position - 1
        val pieceOfNews = news[decreasingOrderPosition]

        holder.title.text = pieceOfNews.title
        holder.content.text = pieceOfNews.content
        holder.creationDate.text = pieceOfNews.created_at.substring(0, 10)

        val textContent = holder.content
        val ellipsis = holder.ellipsis
        textContent.maxLines = 2
        //TODO FIX THIS SHIT TO DISPLAY STUFF
        if (textContent.lineCount > 2) {
            ellipsis.visibility = View.VISIBLE
        }
        textContent.setOnClickListener {
            if (textContent.maxLines == 2 || textContent.lineCount < 2) {
                textContent.maxLines = Int.MAX_VALUE
                ellipsis.visibility = View.GONE
            } else {
                textContent.maxLines = 2
                ellipsis.visibility = View.VISIBLE
            }
        }
    }

    /**
     * The class that holds the corresponding View parts and xml parts,
     * so there is a true object from the xml layout.
     */
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.textViewNewsTitle
        val content = itemView.textViewNewsContent
        val creationDate = itemView.textViewDatePosted
        val ellipsis = itemView.textViewEllipsis
    }

}

