package com.passitwiki.passit.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activities.LoginActivity
import com.passitwiki.passit.activities.sharedPreferences
import com.passitwiki.passit.dialogfragments.PasswordDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * Fragment that displays user's full name, has an expandable accessibility tab,
 * an expandable fieldAgeGroup tab and a logout clickable.
 */
class SettingsFragment : Fragment() {

    companion object {
        const val KEY = "FragmentSettings"
        const val FIELD_OF_STUDY = "FieldOfStudy"
        const val FULL_NAME = "FullName"
        fun newInstance(
            key: String,
            fieldOfStudy: String,
            fullName: String
        ): Fragment {
            val fragment = SettingsFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putString(FIELD_OF_STUDY, fieldOfStudy)
            argument.putString(FULL_NAME, fullName)
            fragment.arguments = argument
            return fragment
        }
    }

    /**
     * when created - makes the two views expandable
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    /**
     * when the view is already created - set the variable parts and logout logic
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO make an adapter for FAG multi-element array - not supported yet

        arguments.let {
            val fullName = it?.getString(FULL_NAME)
            val fos = it?.getString(FIELD_OF_STUDY)

            view.textViewUserNameSurname.text = fullName
            view.textViewFos.text = fos

            view.textViewChangePassword.setOnClickListener {
                val passwordDialogFragment =
                    PasswordDialogFragment.newInstance("NewPassword")
                passwordDialogFragment.show(fragmentManager!!, "password")
            }

//            view.switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
//                if (isChecked) {
//
//                }
//            }

        }

        view.textViewLogout.setOnClickListener {
            Log.d(
                "MyTagExplicit",
                "SettingsFragment: onViewCreated: set logged_in to false"
            )
            sharedPreferences.edit().putBoolean("logged_in", false).apply()
            val intent = (Intent(activity!!.applicationContext, LoginActivity::class.java))
            startActivity(intent)
            activity!!.finish()
        }
    }
}
