package com.passitwiki.passit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.passitwiki.passit.R
import kotlinx.android.synthetic.main.fragment_memes.*
import kotlinx.android.synthetic.main.fragment_memes.view.*

//TODO description for both the fragment and its functions
//TODO make this fragment usable???
class MemesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_memes, container, false)
        // You make any layout interaction inside this fun. Fun.
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

