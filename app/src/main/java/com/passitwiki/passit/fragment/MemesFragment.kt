package com.passitwiki.passit.fragment

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
class MemesFragment(private val key: String) : Fragment() {

    /**
     * For now, there is a glide placeholder with a gif.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_memes, container, false)
        val imgView = view.ImageViewMemesFrag
        Glide
            .with(this)
            .load(R.drawable.koscielnik)
            .into(imgView)
        return view
    }
}

