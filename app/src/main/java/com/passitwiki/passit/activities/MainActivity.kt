package com.passitwiki.passit.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.passitwiki.passit.R
import com.passitwiki.passit.fragments.*
import com.passitwiki.passit.tools.UserSetter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


lateinit var activeFragment: Fragment

//TODO figure out why did i put this here
lateinit var dashboardFragment: Fragment


class MainActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    private var currentSelectItemId = R.id.itemDashboard
    private lateinit var refreshToken: String
    private lateinit var accessToken: String
    val frameLayoutMain = R.id.frameLayoutMain

    lateinit var lecturersFragment: Fragment
    lateinit var subjectsFragment: Fragment
    lateinit var memesFragment: Fragment
    lateinit var settingsFragment: Fragment

    private val mBtmNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemDashboard -> {
                    swapFragments(item.itemId, "Dashboard")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemLecturers -> {
                    swapFragments(item.itemId, "Lecturers")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemSubjects -> {
                    swapFragments(item.itemId, "Subjects")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.itemMemes -> {
                    swapFragments(item.itemId, "Memes")
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        launch {
            refreshToken = intent.getStringExtra("refresh")!!
            Log.d(
                "MyTagExplicit",
                "4*. MainActivity: onCreate: get refreshToken: $refreshToken"
            )
            accessToken = intent.getStringExtra("access")!!
            Log.d(
                "MyTagExplicit",
                "5*. MainActivity: onCreate: get accessToken: $accessToken"
            )
            delay(100)
            UserSetter.setUserAndFos(accessToken)
            delay(200)
            Log.d("MyTagExplicit", "12*. heya" + sharedPreferences.getInt("current_fos_int", 0))

            val fos = sharedPreferences.getString("current_fos", "Error")!!
            Log.d("MyTagExplicit", "13*. MainActivity:onCreate:checking got fos: $fos")
            val fosInt = sharedPreferences.getInt("current_fos_int", 0)
            Log.d("MyTagExplicit", "14*. MainActivity:onCreate:checking got fosInt: $fosInt")
            val fullName =
                sharedPreferences.getString(
                    "current_user_full_name",
                    "Something went terribly wrong"
                )!!
            Log.d("MyTagExplicit", "15*. MainActivity:onCreate:checking got fullName: $fullName")
            val fag = sharedPreferences.getInt("current_fag", 0)
            Log.d("MyTagExplicit", "16*. MainActivity:onCreate:checking got fag: $fag")

            setContentView(R.layout.activity_main)
            btmNav.itemIconTintList = null //Color of the bottom icons

            setUpFragments(fag, fos, fullName)


            imageViewButtonSettings.setOnClickListener {
                swapFragments(R.id.imageViewButtonSettings, "Settings")
            }

            btmNav.setOnNavigationItemSelectedListener(mBtmNavItemSelectedListener)

        }
    }


    override fun onBackPressed() {
        if (activeFragment == supportFragmentManager.findFragmentByTag("IndividualLecturer")) {
            swapFragments(R.id.itemLecturers, "Lecturers")
        } else if (activeFragment == supportFragmentManager.findFragmentByTag("IndividualSubject")) {
            swapFragments(R.id.itemSubjects, "Subjects")
        } else if (currentSelectItemId != R.id.itemDashboard
            || activeFragment == supportFragmentManager.findFragmentByTag("Calendar")
        ) {
            btmNav.selectedItemId = R.id.itemDashboard
            swapFragments(R.id.itemDashboard, "Dashboard")
        } else {
            supportFragmentManager.fragments.forEach { fragment ->
                if (fragment != null && fragment.isVisible) {
                    with(fragment.childFragmentManager) {
                        if (backStackEntryCount > 0) {
                            popBackStack()
                            return
                        }
                    }
                }
            }
            super.onBackPressed()
        }
    }


    private fun swapFragments(@IdRes actionId: Int, key: String) {
        if (supportFragmentManager.findFragmentByTag(key) != null) {

            when (actionId) {
                R.id.itemDashboard -> {
                    textViewToolbar.text = getString(R.string.dashboard)
                    imageViewButtonSettings.visibility = (View.VISIBLE)
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(dashboardFragment)
                        .commit()
                    activeFragment = dashboardFragment
                }
                R.id.itemLecturers -> {
                    textViewToolbar.text = getString(R.string.lecturers)
                    imageViewButtonSettings.visibility = (View.GONE)
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(lecturersFragment)
                        .commit()
                    activeFragment = lecturersFragment
                }
                R.id.itemSubjects -> {
                    textViewToolbar.text = getString(R.string.subjects)
                    imageViewButtonSettings.visibility = (View.GONE)
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(subjectsFragment)
                        .commit()
                    activeFragment = subjectsFragment
                }
                R.id.itemMemes -> {
                    textViewToolbar.text = getString(R.string.memes)
                    imageViewButtonSettings.visibility = (View.GONE)
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(memesFragment)
                        .commit()
                    activeFragment = memesFragment
                }
                R.id.imageViewButtonSettings -> {
                    textViewToolbar.text = getString(R.string.settings)
                    imageViewButtonSettings.visibility = (View.GONE)
                    supportFragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(settingsFragment)
                        .commit()
                    activeFragment = settingsFragment
                }
            }
        }
    }

    suspend fun setUpFragments(fag: Int, fos: String, fullName: String) {
        dashboardFragment = DashboardFragment.newInstance("Dashboard", fag)
        delay(100)
        lecturersFragment = LecturersFragment.newInstance("Lecturers")
        subjectsFragment = SubjectsFragment.newInstance("Subjects")
        memesFragment = MemesFragment.newInstance("Memes")
        settingsFragment = SettingsFragment.newInstance("Settings", fos, fullName)
        activeFragment = dashboardFragment

        supportFragmentManager.beginTransaction()
            .add(frameLayoutMain, dashboardFragment, "Dashboard").commit()
        supportFragmentManager.beginTransaction()
            .add(frameLayoutMain, lecturersFragment, "Lecturers").hide(lecturersFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, subjectsFragment, "Subjects")
            .hide(subjectsFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, settingsFragment, "Settings")
            .hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, memesFragment, "Memes")
            .hide(memesFragment).commit()

    }
}


