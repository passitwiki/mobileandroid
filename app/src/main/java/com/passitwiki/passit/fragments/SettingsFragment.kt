package com.passitwiki.passit.fragments


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.LoginActivity
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalSharedPreferences
import com.passitwiki.passit.tools.globalUser
import kotlinx.android.synthetic.main.fragment_settings.view.*


//TODO description for both the fragment and its functions
class SettingsFragment : Fragment() {

    //TODO user info display, make the adapter etc

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
        val fosTextView = view.textViewFos
        val switchTheme = view.switchTheme
        val textChangePasswordButton = view.textViewChangePassword


        nameTextView.append(globalUser?.first_name + " " + globalUser?.last_name)

        fosTextView.append(globalSharedPreferences!!.getString("current_fos", "no_fos_present")!!)
        //TODO make an adapter for FAG multi-element array - not supported yet

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

        textChangePasswordButton.setOnClickListener {
            val passwordDialogFragment = PasswordDialogFragment()
            passwordDialogFragment.show(fragmentManager!!, "password")
        }


        //TODO darkmode onSwitch toggle
        switchTheme.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener
        { _, isChecked ->
            toggleTheme(
                isChecked
            )
        })


        view.textViewLogout.setOnClickListener {
            val intent = (Intent(globalContext, LoginActivity::class.java))
            startActivity(intent)
            activity!!.finish()
        }

        return view
    }

    private fun toggleTheme(darkTheme: Boolean) {
        val editor: SharedPreferences.Editor =
            globalSharedPreferences!!.edit()
        editor.putBoolean("current_theme_light", false)
        editor.apply()

        activity!!.setTheme(R.style.DarkTheme)
//        val intent = (Intent(globalContext, MainActivity::class.java))
//        finish()
//        startActivity(intent)
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
