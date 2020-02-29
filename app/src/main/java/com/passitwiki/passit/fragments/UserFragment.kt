package com.passitwiki.passit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalUser
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*


//TODO description for both the fragment and its functions
class UserFragment : Fragment() {

    //TODO user info display, make the adapter etc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        view.textViewUserNameSurname.append(globalUser?.username)


        view.textViewLogout.setOnClickListener {

        }

        return view
    }


}
