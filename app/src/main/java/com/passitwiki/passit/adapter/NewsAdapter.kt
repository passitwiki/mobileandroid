package com.passitwiki.passit.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.models.News
import kotlinx.android.synthetic.main.item_news.view.*


/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
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
//        val decreasingOrderPosition = itemCount - position - 1
        val pieceOfNews = news[position]
        val creation = "@" + pieceOfNews.created_by

        holder.title.text = pieceOfNews.title
        holder.content.text = pieceOfNews.content
        holder.creationDate.text = pieceOfNews.created_at.substring(0, 10)
        holder.createdBy.text = creation

        val textContent = holder.content
        textContent.maxLines = 2
        textContent.ellipsize = TextUtils.TruncateAt.END

        textContent.setOnClickListener {
            if (textContent.maxLines == 2 || textContent.lineCount < 2) {
                textContent.maxLines = Int.MAX_VALUE
                textContent.ellipsize = null
            } else {
                textContent.maxLines = 2
                textContent.ellipsize = TextUtils.TruncateAt.END
            }
        }
    }

    /**
     * The class that holds the corresponding View parts and xml parts,
     * so there is a true object from the xml layout.
     */
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.textViewNewsTitle
        val content: TextView = itemView.textViewNewsContent
        val creationDate: TextView = itemView.textViewDatePosted
        val createdBy: TextView = itemView.textViewUserPosted
    }

}

