package com.passitwiki.passit.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.LoginActivity
import com.passitwiki.passit.activity.sharedPreferences
import com.passitwiki.passit.dialogfragment.PasswordChangeSettingsDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * Fragment that displays user's full name, has an expandable accessibility tab,
 * an expandable fieldAgeGroup tab and a logout clickable.
 */
class SettingsFragment(
    private val key: String,
    private val fieldOfStudy: String,
    private val fullName: String
) : Fragment() {

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

        rootView.textViewUserNameSurname.text = fullName
        rootView.textViewFos.text = fieldOfStudy

        rootView.textViewChangePassword.setOnClickListener {
            val passwordDialogFragment =
                PasswordChangeSettingsDialogFragment("NewPassword")
            passwordDialogFragment.show(requireFragmentManager(), "password")
        }

        rootView.textViewLogout.setOnClickListener {
            Log.d(
                "MyTagExplicit",
                "SettingsFragment: onViewCreated: set logged_in to false"
            )
            sharedPreferences.edit().putBoolean("logged_in", false).apply()
            val intent = (Intent(requireActivity().applicationContext, LoginActivity::class.java))
            startActivity(intent)
            requireActivity().finish()
        }

        return rootView
    }
}
