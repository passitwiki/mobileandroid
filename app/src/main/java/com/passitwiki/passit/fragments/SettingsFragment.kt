package com.passitwiki.passit.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.LoginActivity
import com.passitwiki.passit.dialogfragments.PasswordDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSettings"
        const val ACCESS_TOKEN = "AccessToken"
        const val FIELD_OF_STUDY = "FieldOfStudy"
        const val FULL_NAME = "FullName"
        const val PREFERENCES = "SharedPreferences"
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        var collapsedTop = true //true - collapsed

        rootView.relativeLayoutUserPartHeader.setOnClickListener {
            if (collapsedTop) {
                rootView.imageViewExpander.setImageResource(R.drawable.ic_arrow_up)
                rootView.relativeLayoutUserPartBody.visibility = View.VISIBLE
                collapsedTop = false
            } else {
                rootView.imageViewExpander.setImageResource(R.drawable.ic_arrow_down)
                rootView.relativeLayoutUserPartBody.visibility = View.GONE
                collapsedTop = true
            }
        }

        var collapsedBottom = true //true - collapsed
        rootView.relativeLayoutFosHeader.setOnClickListener {
            if (collapsedBottom) {
                rootView.imageViewExpander2.setImageResource(R.drawable.ic_arrow_up)
                rootView.relativeLayoutFosBody.visibility = View.VISIBLE
                collapsedBottom = false
            } else {
                rootView.imageViewExpander2.setImageResource(R.drawable.ic_arrow_down)
                rootView.relativeLayoutFosBody.visibility = View.GONE
                collapsedBottom = true
            }
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO make an adapter for FAG multi-element array - not supported yet

        arguments.let {
            val fullName = it?.getString(FULL_NAME)
            val fos = it?.getString(FIELD_OF_STUDY)
            val accessToken = it?.getString(ACCESS_TOKEN)

            view.textViewUserNameSurname.text = fullName
            view.textViewFos.text = fos


            view.textViewChangePassword.setOnClickListener {
                val passwordDialogFragment =
                    PasswordDialogFragment.newInstance(accessToken!!, "NewPassword")
                passwordDialogFragment.show(fragmentManager!!, "password")
            }
        }

        view.textViewLogout.setOnClickListener {
            val sharedPref = activity!!.getSharedPreferences("userdetails", Context.MODE_PRIVATE)

            sharedPref.edit().putString(
                "username",
                "null"
            ).apply()
            sharedPref.edit().putString(
                "password",
                "null"
            ).apply()
            sharedPref.edit().putBoolean(
                "logged_in",
                false
            ).apply()
            val intent = (Intent(activity!!.applicationContext, LoginActivity::class.java))
            startActivity(intent)
            activity!!.finish()
        }
    }
}
