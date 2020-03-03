package com.passitwiki.passit.activities

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.fragments.*
import com.passitwiki.passit.models.User
import com.passitwiki.passit.tools.globalContext
import com.passitwiki.passit.tools.globalToken
import com.passitwiki.passit.tools.globalUser
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var dashboardFragment: DashboardFragment
    lateinit var lecturersFragment: LecturersFragment
    lateinit var memesFragment: MemesFragment
    lateinit var subjectsFragment: SubjectsFragment
    lateinit var settingsFragment: SettingsFragment
    var currentFragment: String = "dash"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(findViewById(R.id.toolbarTop))

        btmNav.itemIconTintList = null

        globalContext = applicationContext

        globalToken = intent.getStringExtra("token")
        val token = globalToken
        val bearerToken = "Bearer $globalToken"

        RetrofitClient.instance.getUserInfo(bearerToken)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    d("MyTag", "Error: ${t}")

                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    d("MyTag", "onResponse: ${response.body()}")

                    //TODO BIG IMPORTANT this shitty thing

                    globalUser = User(
                        response.body()!!.id,
                        response.body()!!.username,
                        response.body()!!.profile
                    )

//                    Toast.makeText(applicationContext, globalUser!!.username, Toast.LENGTH_LONG).show()

                    changeToDashboard(token!!)

                    imageViewButtonSettings.setOnClickListener {
                        changeToUser()
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

//                    Toast.makeText(applicationContext, globalUser!!.username, Toast.LENGTH_LONG).show()
                }
            })

//        getUserData(token)
//
//        Toast.makeText(this, globalUser!!.username, Toast.LENGTH_LONG).show()
//
//        changeToDashboard(token!!)
//
//        imageViewButtonSettings.setOnClickListener {
//            changeToUser()
//        }
//
//        btmNav.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.itemDashboard -> {
//                    changeToDashboard(token)
//                }
//                R.id.itemLecturers -> {
//                    changeToLecturers()
//                }
//                R.id.itemSubjects -> {
//                    changeToSubjects()
//                }
//                R.id.itemMemes -> {
//                    changeToMemes()
//                }
//            }
//            true
//        }

    }

    fun changeToDashboard(token: String) {
        currentFragment = "dash"
        this.textViewToolbar.text= getString(R.string.dashboard)
        this.imageViewButtonSettings.setVisibility(View.VISIBLE)
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
        this.textViewToolbar.text= getString(R.string.lecturers)
        this.imageViewButtonSettings.setVisibility(View.GONE)
        lecturersFragment = LecturersFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, lecturersFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToSubjects() {
        currentFragment = "subj"
        this.textViewToolbar.text= getString(R.string.subjects)
        this.imageViewButtonSettings.setVisibility(View.GONE)
        subjectsFragment = SubjectsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, subjectsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToMemes() {
        currentFragment = "meme"
        this.textViewToolbar.text= getString(R.string.memes)
        this.imageViewButtonSettings.setVisibility(View.GONE)
        memesFragment = MemesFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, memesFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun changeToUser() {
        currentFragment = "sett"
        this.textViewToolbar.text= getString(R.string.settings)
        this.imageViewButtonSettings.setVisibility(View.GONE)
        settingsFragment = SettingsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayoutMain, settingsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if (!currentFragment.equals("dash")) {
            val token = intent.getStringExtra("token")
            changeToDashboard(token!!)
        } else {
            super.onBackPressed()
        }
    }


}


