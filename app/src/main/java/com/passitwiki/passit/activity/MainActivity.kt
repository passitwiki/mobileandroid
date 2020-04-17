package com.passitwiki.passit.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.passitwiki.passit.R
import com.passitwiki.passit.fragment.*
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.utilities.inTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.core.parameter.parametersOf

var accessToken = ""
lateinit var activeFragment: Fragment

class MainActivity : AppCompatActivity() {
    private var fieldAgeGroup = 0
    private lateinit var fieldOfStudy:String
    private lateinit var fullName:String

    private val repository: Repository by inject()
//    private val dashboardFragment: DashboardFragment by inject { parametersOf(fieldAgeGroup) }
//    private val subjectsFragment: SubjectsFragment by inject()
//    private val lecturersFragment: LecturersFragment by inject()
//    private val memesFragment: MemesFragment by inject()
//    private val settingsFragment: SettingsFragment by inject { parametersOf(fieldOfStudy, fullName)}
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var subjectsFragment: SubjectsFragment
    private lateinit var lecturersFragment: LecturersFragment
    private lateinit var memesFragment: MemesFragment
    private lateinit var settingsFragment: SettingsFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setupKoinFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var refreshToken = ""
        //Get the tokens first
        runBlocking {
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
        }
        //Then, set a user with the access token
        runBlocking {
            Utilities(repository).setUserAndFos()
        }
        //Get Field of study and full name of the user
        fieldOfStudy = sharedPreferences.getString("current_fos", "Error")!!
        Log.d("MyTagExplicit", "13*. MainActivity:onCreate:checking got fos: $fieldOfStudy")
        val fosInt = sharedPreferences.getInt("current_fos_int", 0)
        Log.d("MyTagExplicit", "14*. MainActivity:onCreate:checking got fosInt: $fosInt")
        fullName = sharedPreferences.getString(
            "current_user_full_name",
            "Something went terribly wrong"
        )!!
        Log.d("MyTagExplicit", "15*. MainActivity:onCreate:checking got fullName: $fullName")
        fieldAgeGroup = sharedPreferences.getInt("current_fag", 0)
        Log.d("MyTagExplicit", "16*. MainActivity:onCreate:checking got fag: $fieldAgeGroup")

        //Color of the bottom icons - otherwise it won't work
        btmNav.itemIconTintList = null

        //Setting up fragments and setting navigation between them
        runBlocking {
            setUpFragments(fieldAgeGroup, fieldOfStudy, fullName)
            Log.d("MyTagExplicit", "17*. Successfully created dash")
        }

        imageViewButtonSettings.setOnClickListener {
            swapFragments(R.id.imageViewButtonSettings, "Settings")
        }

        btmNav.setOnNavigationItemSelectedListener(mBtmNavItemSelectedListener)

    }


    override fun onBackPressed() {
        when (activeFragment.tag) {
            "IndividualLecturer" -> swapFragments(R.id.itemLecturers, "Lecturers")
            "IndividualSubject" -> swapFragments(R.id.itemSubjects, "Subjects")
            "Calendar" -> {
                btmNav.selectedItemId = R.id.itemDashboard
                swapFragments(R.id.itemDashboard, "Dashboard")
            }
            "Dashboard" -> {
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
            else -> swapFragments(R.id.itemDashboard, "Dashboard")
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

    private fun setUpFragments(fag: Int, fos: String, fullName: String) {
        runBlocking {
            dashboardFragment = DashboardFragment("Dashboard", fag)
        }
        lecturersFragment = LecturersFragment("Lecturers")
        subjectsFragment = SubjectsFragment("Subjects")
        memesFragment = MemesFragment("Memes")
        settingsFragment = SettingsFragment("Settings", fos, fullName)
        activeFragment = dashboardFragment
        progressMain.visibility = View.GONE

        supportFragmentManager.inTransaction { add(R.id.frameLayoutMain,dashboardFragment,"Dashboard") }
        supportFragmentManager.inTransaction { add(R.id.frameLayoutMain,lecturersFragment,"Lecturers").hide(lecturersFragment) }
        supportFragmentManager.inTransaction { add(R.id.frameLayoutMain,subjectsFragment,"Subjects").hide(subjectsFragment) }
        supportFragmentManager.inTransaction { add(R.id.frameLayoutMain,settingsFragment,"Settings").hide(settingsFragment) }
        supportFragmentManager.inTransaction { add(R.id.frameLayoutMain,memesFragment,"Memes").hide(memesFragment) }

//        supportFragmentManager.beginTransaction().add(frameLayoutMain, dashboardFragment,"Dashboard").commit()
//        this.supportFragmentManager.beginTransaction().add(frameLayoutMain, dashboardFragment, "Dashboard").commit()
//        supportFragmentManager.beginTransaction()
//            .add(frameLayoutMain, lecturersFragment, "Lecturers").hide(lecturersFragment).commit()
//        supportFragmentManager.beginTransaction().add(frameLayoutMain, subjectsFragment, "Subjects")
//            .hide(subjectsFragment).commit()
//        supportFragmentManager.beginTransaction().add(frameLayoutMain, settingsFragment, "Settings")
//            .hide(settingsFragment).commit()
//        supportFragmentManager.beginTransaction().add(frameLayoutMain, memesFragment, "Memes")
//            .hide(memesFragment).commit()
//        supportFragmentManager.beginTransaction().add(frameLayoutMain, dashboardFragment, "Dash")
    }
}


