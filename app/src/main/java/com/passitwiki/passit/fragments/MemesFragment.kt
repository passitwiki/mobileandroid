package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_memes.view.*

/**
 * The fragment that holds memes. HAHA FUNNEH MEME.
 */
class MemesFragment : Fragment() {

    companion object {
        const val KEY = "FragmentMemes"
        fun newInstance(key: String): Fragment {
            val fragment = MemesFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * For now, there is a glide placeholder with a gif.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)
        val imgView = view.ImageViewMemesFrag
//        val url =
//            "http://ewintergames.pl:8080/api/files/7-0?sign=MTU4MjI0MzIwMDAwMHwxLTV8Ny0wfGhzQUxOcTZVN0E4T1NvcEROcGxiWEd5MWI5Tk56VWg1cVUzcmRVcDBtcG89&updated=1580337352482"
//        Glide
//            .with(this)
//            .load(url)
//            .into(imgView)
        Glide
            .with(this)
            .load(R.drawable.koscielnik)
            .into(imgView)
        return view
    }
}

