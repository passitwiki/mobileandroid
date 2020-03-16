package com.passitwiki.passit.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.passitwiki.passit.R
import com.passitwiki.passit.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.itemDashboard
    private lateinit var refreshToken: String
    private lateinit var accessToken: String
    val frameLayoutMain = R.id.frameLayoutMain
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var activeFragment: Fragment
    lateinit var dashboardFragment: Fragment
    lateinit var lecturersFragment: Fragment
    lateinit var subjectsFragment: Fragment
    lateinit var memesFragment: Fragment
    lateinit var settingsFragment: Fragment

    companion object {
        const val SAVED_STATE_CONTAINER_KEY = "ContainerKey"
        const val SAVED_STATE_CURRENT_TAB_KEY = "CurrentTabKey"
    }

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
        super.onCreate(savedInstanceState)
        refreshToken = intent.getStringExtra("refresh")!!
        accessToken = "Bearer ${intent.getStringExtra("token")}"
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        setUpFragments()

        if (savedInstanceState != null) {
            savedStateSparseArray =
                savedInstanceState.getSparseParcelableArray(SAVED_STATE_CONTAINER_KEY)!!
            currentSelectItemId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
        }

        setContentView(R.layout.activity_main)
        btmNav.itemIconTintList = null //Color of the bottom icons


        imageViewButtonSettings.setOnClickListener {
            swapFragments(R.id.imageViewButtonSettings, "Settings")
        }
        btmNav.setOnNavigationItemSelectedListener(mBtmNavItemSelectedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, savedStateSparseArray)
        outState.putInt(SAVED_STATE_CURRENT_TAB_KEY, currentSelectItemId)
    }

    override fun onBackPressed() {
        if (currentSelectItemId != R.id.itemDashboard) {
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

    private fun savedFragmentState(actionId: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayoutMain)
        if (currentFragment != null) {
            savedStateSparseArray.put(
                currentSelectItemId,
                supportFragmentManager.saveFragmentInstanceState(currentFragment)
            )
        }
        currentSelectItemId = actionId
    }

    private fun swapFragments(@IdRes actionId: Int, key: String) {
        if (supportFragmentManager.findFragmentByTag(key) != null) {
            savedFragmentState(actionId)
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
                    Log.d("dupa", "dupa")
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

    private fun setUpFragments() {
        val fos = sharedPreferences.getString("current_fos", "Error404: fos not found")!!
        val fullName =
            sharedPreferences.getString("current_user_full_name", "Something went terribly wrong")!!
        dashboardFragment = DashboardFragment.newInstance(accessToken, "Dashboard")
        activeFragment = dashboardFragment
        lecturersFragment = LecturersFragment.newInstance(accessToken, "Lecturers")
        subjectsFragment = SubjectsFragment.newInstance(accessToken, "Subjects", fos)
        memesFragment = MemesFragment.newInstance(accessToken, "Memes")
        settingsFragment = SettingsFragment.newInstance(accessToken, "Settings", fos, fullName)

        supportFragmentManager.beginTransaction()
            .add(frameLayoutMain, dashboardFragment, "Dashboard").commit()
        supportFragmentManager.beginTransaction()
            .add(frameLayoutMain, lecturersFragment, "Lecturers").hide(lecturersFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, subjectsFragment, "Subjects")
            .hide(subjectsFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, memesFragment, "Memes")
            .hide(memesFragment).commit()
        supportFragmentManager.beginTransaction().add(frameLayoutMain, settingsFragment, "Settings")
            .hide(settingsFragment).commit()
    }

//    private fun createDashboard(key: String, actionId: Int) {
//        val fragment = DashboardFragment.newInstance(accessToken, key)
////        mine
//        this.textViewToolbar.text = getString(R.string.dashboard)
//        this.imageViewButtonSettings.visibility = (View.VISIBLE)
//        fragment.setInitialSavedState(savedStateSparseArray[actionId])
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutMain, fragment, key)
//            .commit()
//    }

//    private fun createSubjects(key: String, actionId: Int) {
//        val fragment = SubjectsFragment.newInstance(accessToken, key, "fos")
//        Log.d("TEST", "creating a subfragment")//DONE12
//
////        mine
//        this.textViewToolbar.text = getString(R.string.subjects)
//        this.imageViewButtonSettings.visibility = (View.GONE)
////
//        fragment.setInitialSavedState(savedStateSparseArray[actionId])
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutMain, fragment, key)
//            .commit()
//    }
//
//    private fun createLecturers(key: String, actionId: Int) {
//        val fragment = LecturersFragment.newInstance(accessToken, key)
//        Log.d("TEST", "creating a lecfragment")//DONE12
//
////        mine
//        this.textViewToolbar.text = getString(R.string.lecturers)
//        this.imageViewButtonSettings.visibility = (View.GONE)
////
//        fragment.setInitialSavedState(savedStateSparseArray[actionId])
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutMain, fragment, key)
//            .commit()
//    }
//
//    private fun createMemes(key: String, actionId: Int) {
//        val fragment = MemesFragment.newInstance(accessToken, key)
//        Log.d("TEST", "creating a memfragment")//DONE12
//
////        mine
//        this.textViewToolbar.text = getString(R.string.memes)
//        this.imageViewButtonSettings.visibility = (View.GONE)
////
//        fragment.setInitialSavedState(savedStateSparseArray[actionId])
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutMain, fragment, key)
//            .commit()
//    }
//
//    private fun createSettings(key: String, actionId: Int) {
//        val fragment = SettingsFragment.newInstance(accessToken, key, "nic", "nic")
////        mine
//        this.textViewToolbar.text = getString(R.string.settings)
//        this.imageViewButtonSettings.visibility = (View.GONE)
////
//        fragment.setInitialSavedState(savedStateSparseArray[actionId])
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayoutMain, fragment, key)
//            .commit()
//    }
}


