package com.passitwiki.passit.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.passitwiki.passit.R
import com.passitwiki.passit.dialogfragments.PatchDialogFragment
import com.passitwiki.passit.dialogfragments.RemoveDialogFragment
import com.passitwiki.passit.dialogfragments.WebViewDialogFragment
import com.passitwiki.passit.fragments.DashboardFragment
import com.passitwiki.passit.models.News
import kotlinx.android.synthetic.main.item_news.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * An adapter class that adapts repeatedly a received JsonArray to a pattern.
 * Takes in a response from the api and
 * creates as many items as getItemCount has.
 * @param news a list object from a json array made with Gson
 */
class NewsAdapter(private val news: List<News>, private val dashFragment: DashboardFragment) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), Filterable {

    var newsFilterArrayList = ArrayList<News>()

    init {
        newsFilterArrayList = news as ArrayList<News>
    }

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
    override fun getItemCount() = newsFilterArrayList.size

    /**
     * Does the intended repeated work. Binds one "row" and a corresponding object.
     * @param holder ViewHolder with its workable xml properties
     * @param position the number of the object you're working with
     */
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//        val decreasingOrderPosition = itemCount - position - 1
        val pieceOfNews = newsFilterArrayList[position]
        val creation = "@" + pieceOfNews.created_by

        holder.title.text = pieceOfNews.title
        holder.content.text = pieceOfNews.content
        holder.creationDate.text = pieceOfNews.created_at.substring(0, 10)
        holder.createdBy.text = creation
        if (pieceOfNews.attachment != null) {
            holder.attachment.text = pieceOfNews.attachment.toString().substring(46)
            holder.attachment.setOnClickListener {
                val webViewDialogFragment =
                    WebViewDialogFragment.newInstance(
                        "RemoveNews",
                        pieceOfNews.attachment.toString()
                    )
                webViewDialogFragment.show(dashFragment.fragmentManager!!, "removeNews")
            }
        } else {
            holder.attachment.visibility = View.GONE
        }

        val textContent = holder.content
        textContent.maxLines = 2
        textContent.ellipsize = TextUtils.TruncateAt.END

        val remove = holder.textViewRemove
        remove.setOnClickListener {
            val pm = PopupMenu(dashFragment.activity, remove)
            pm.menuInflater.inflate(R.menu.news_options, pm.menu)
            pm.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.newsEdit -> {
                        val patchDialogFragment =
                            PatchDialogFragment.newInstance(
                                "PatchNews",
                                pieceOfNews.id,
                                pieceOfNews.title,
                                pieceOfNews.content
                            )
                        patchDialogFragment.show(dashFragment.fragmentManager!!, "patchNews")
                    }
                    R.id.newsRemove -> {
                        val removeDialogFragment =
                            RemoveDialogFragment.newInstance("RemoveNews", pieceOfNews.id)
                        removeDialogFragment.show(dashFragment.fragmentManager!!, "removeNews")
                    }
                }
                true
            }
            pm.show()
        }

//            val removeDialogFragment =
//                RemoveDialogFragment.newInstance("AddNews", pieceOfNews.id)
//            removeDialogFragment.show(dashFragment.fragmentManager!!, "addNews")
//        }

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
        val textViewRemove: TextView = itemView.textViewRemove
        val attachment: TextView = itemView.textViewAttachment
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    newsFilterArrayList = news as ArrayList<News>
                } else {
                    val resultList = ArrayList<News>()
                    for (itemNews in news) {
                        if (itemNews.title.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                            || itemNews.content.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(itemNews)
                        }
                    }
                    newsFilterArrayList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = newsFilterArrayList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                newsFilterArrayList = results?.values as ArrayList<News>
                notifyDataSetChanged()
            }
        }
    }

}

