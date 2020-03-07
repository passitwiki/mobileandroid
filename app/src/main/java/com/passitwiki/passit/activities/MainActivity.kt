package com.passitwiki.passit.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.fragments.*
import com.passitwiki.passit.models.User
import com.passitwiki.passit.tools.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var lecturersFragment: LecturersFragment
    private lateinit var memesFragment: MemesFragment
    private lateinit var subjectsFragment: SubjectsFragment
    private lateinit var settingsFragment: SettingsFragment
    private var currentFragment: String = "dash"
    lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btmNav.itemIconTintList = null

        globalContext = applicationContext

        globalRefresh = intent.getStringExtra("refresh")

        globalToken = intent.getStringExtra("token")
        val token = globalToken
        val bearerToken = "Bearer $globalToken"



        RetrofitClient.instance.getUserInfo(bearerToken)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    d("MyTag", "Error: $t")

                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    d("MyTag", "onResponse: ${response.body()}")
                    globalUser = User(
                        response.body()!!.id,
                        response.body()!!.username,
                        response.body()!!.profile,
                        response.body()!!.first_name,
                        response.body()!!.last_name
                    )

                    sharedPref = getPreferences(Context.MODE_PRIVATE)
                    FieldOfStudyChecker.updateFieldOfStudy(
                        globalUser!!.profile.field_age_groups[0].field_of_study
                    )

                    d(
                        "MyTagAyy",
                        globalUser!!.profile.field_age_groups[0].field_of_study.toString()
                    )


                    changeToDashboard(token!!)

                    imageViewButtonSettings.setOnClickListener {
                        changeToSettings()
                    }

                    btmNav.setOnNavigationItemSelectedListener { item ->
                        when (item.itemId) {
                            R.id.itemDashboard -> {
                                changeToDashboard(token)
                            }
                            R.id.itemLecturers -> {
                                changeToLecturers()
                            }
                            R.id.itemSubjects -> {
                                changeToSubjects()
                            }
                            R.id.itemMemes -> {
                                changeToMemes()
                            }
                        }
                        true
                    }
                }
            })

    }

    fun changeToDashboard(token: String) {
        currentFragment = "dash"
        this.textViewToolbar.text = getString(R.string.dashboard)
        this.imageViewButtonSettings.visibility = (View.VISIBLE)
        dashboardFragment = DashboardFragment()
        val args = Bundle()
        args.putString("token", token)
        dashboardFragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, dashboardFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToLecturers() {
        currentFragment = "lect"
        this.textViewToolbar.text = getString(R.string.lecturers)
        this.imageViewButtonSettings.visibility = (View.GONE)
        lecturersFragment = LecturersFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, lecturersFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToSubjects() {
        currentFragment = "subj"
        this.textViewToolbar.text = getString(R.string.subjects)
        this.imageViewButtonSettings.visibility = (View.GONE)
        subjectsFragment = SubjectsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, subjectsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToMemes() {
        currentFragment = "meme"
        this.textViewToolbar.text = getString(R.string.memes)
        this.imageViewButtonSettings.visibility = (View.GONE)
        memesFragment = MemesFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, memesFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToSettings() {
        currentFragment = "sett"
        this.textViewToolbar.text = getString(R.string.settings)
        this.imageViewButtonSettings.visibility = (View.GONE)
        settingsFragment = SettingsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, settingsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if (currentFragment != "dash") {
            val token = intent.getStringExtra("token")
            changeToDashboard(token!!)
        } else {
            super.onBackPressed()
        }
    }

}


