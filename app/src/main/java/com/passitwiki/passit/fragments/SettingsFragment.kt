package com.passitwiki.passit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.tools.globalUser
import kotlinx.android.synthetic.main.fragment_settings.view.*


//TODO description for both the fragment and its functions
class SettingsFragment : Fragment() {

    //TODO user info display, make the adapter etc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val nameTextView = view.textViewUserNameSurname
        val expanderTop = view.relativeLayoutUserPartHeader
        val topBody = view.relativeLayoutUserPartBody
        val expanderBottom = view.relativeLayoutFosHeader
        val bottomBody = view.relativeLayoutFosBody
        val imageTop = view.imageViewExpander
        val imageBottom = view.imageViewExpander2

        nameTextView.append(globalUser?.username)

        var collapsedTop = "true" //true - collapsed
        expanderTop.setOnClickListener {
            if (collapsedTop.equals("true")) {
                imageTop.setImageResource(R.drawable.ic_arrow_up)
                topBody.visibility = View.VISIBLE
                collapsedTop = "false"
            } else {
                imageTop.setImageResource(R.drawable.ic_arrow_down)
                topBody.visibility = View.GONE
                collapsedTop = "true"
            }
        }

        var collapsedBottom = "true" //true - collapsed
        expanderBottom.setOnClickListener {
            if (collapsedBottom.equals("true")) {
                imageBottom.setImageResource(R.drawable.ic_arrow_up)
                bottomBody.visibility = View.VISIBLE
//                bottomBody.textViewFos.text = "${globalUser!!.profile.field_age_groups.fosArray.get(0).studentsStartYear}haha"
                collapsedBottom = "false"
            } else {
                imageBottom.setImageResource(R.drawable.ic_arrow_down)
                bottomBody.visibility = View.GONE
                collapsedBottom = "true"
            }
        }


        //TODO darkmode onSwitch toggle

        view.textViewLogout.setOnClickListener {
            //TODO good logout logic
        }

        return view
    }

//    private fun collapseBehavior(
//        state: String,
//        header: RelativeLayout,
//        body: RelativeLayout,
//        var arrow: ImageView
//    ) {
//        header.setOnClickListener {
//            if (state.equals("true")) {
//                arrow.setImageResource(R.drawable.ic_arrow_up)
//                body.visibility = View.VISIBLE
//                state = "false"
//            } else {
//                arrow.setImageResource(R.drawable.ic_arrow_down)
//                body.visibility = View.GONE
//                state = "true"
//            }
//
//        }
//    }
}
