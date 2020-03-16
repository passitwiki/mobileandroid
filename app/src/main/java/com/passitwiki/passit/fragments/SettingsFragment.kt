package com.passitwiki.passit.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.LoginActivity
import com.passitwiki.passit.dialogfragments.PasswordDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSettings"
        const val ACCESS_TOKEN = "AccessToken"
        const val FIELD_OF_STUDY = "FieldOfStudy"
        const val FULL_NAME = "FullName"
        fun newInstance(
            token: String,
            key: String,
            fieldOfStudy: String,
            fullName: String
        ): Fragment {
            val fragment = SettingsFragment()
            val argument = Bundle()
            argument.putString(ACCESS_TOKEN, token)
            argument.putString(KEY, key)
            argument.putString(FIELD_OF_STUDY, fieldOfStudy)
            argument.putString(FULL_NAME, fullName)
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO make an adapter for FAG multi-element array - not supported yet

        arguments.let {
            val fullName = it?.getString(FULL_NAME)
            val fos = it?.getString(FIELD_OF_STUDY)

            textViewUserNameSurname.append(fullName)
            textViewFos.append(fos)
        }


        var collapsedTop = "true" //true - collapsed
        relativeLayoutUserPartHeader.setOnClickListener {
            if (collapsedTop.equals("true")) {
                imageViewExpander.setImageResource(R.drawable.ic_arrow_up)
                relativeLayoutUserPartBody.visibility = View.VISIBLE
                collapsedTop = "false"
            } else {
                imageViewExpander.setImageResource(R.drawable.ic_arrow_down)
                relativeLayoutUserPartBody.visibility = View.GONE
                collapsedTop = "true"
            }
        }

        var collapsedBottom = "true" //true - collapsed
        relativeLayoutFosHeader.setOnClickListener {
            if (collapsedBottom.equals("true")) {
                imageViewExpander2.setImageResource(R.drawable.ic_arrow_up)
                relativeLayoutFosBody.visibility = View.VISIBLE
                collapsedBottom = "false"
            } else {
                imageViewExpander2.setImageResource(R.drawable.ic_arrow_down)
                relativeLayoutFosBody.visibility = View.GONE
                collapsedBottom = "true"
            }
        }

        textViewChangePassword.setOnClickListener {
            val passwordDialogFragment =
                PasswordDialogFragment()
            passwordDialogFragment.show(fragmentManager!!, "password")
        }

        textViewLogout.setOnClickListener {
            val intent = (Intent(activity!!.applicationContext, LoginActivity::class.java))
            startActivity(intent)
            activity!!.finish()
        }
    }
}
