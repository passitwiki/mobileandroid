package com.passitwiki.passit.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
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
import com.passitwiki.passit.tools.UserSetter
import kotlinx.android.synthetic.main.activity_main.*


lateinit var activeFragment: Fragment
lateinit var dashboardFragment: Fragment


class MainActivity : AppCompatActivity() {

    private var savedStateSparseArray = SparseArray<Fragment.SavedState>()
    private var currentSelectItemId = R.id.itemDashboard
    private lateinit var refreshToken: String
    private lateinit var accessToken: String
    val frameLayoutMain = R.id.frameLayoutMain
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var lecturersFragment: Fragment
    lateinit var subjectsFragment: Fragment
    lateinit var memesFragment: Fragment
    lateinit var settingsFragment: Fragment

    lateinit var fos: String
    var fosInt = 0
    lateinit var fullName: String
    var fag = 0

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


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        refreshToken = intent.getStringExtra("refresh")!!
        accessToken = "Bearer ${intent.getStringExtra("token")}"
        UserSetter.setUserAndFos(accessToken, sharedPreferences)
        Log.d("MyTag", "heya" + sharedPreferences.getInt("current_fos_int", 0))

//        while (sharedPreferences.getInt("current_fos_int", 0) == 0) {
//            Thread(Runnable {
//                try {
//                    Thread.sleep(1)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }).start()
//        }

        fos = sharedPreferences.getString("current_fos", "Error")!!
        fosInt = sharedPreferences.getInt("current_fos_int", 0)
        fullName =
            sharedPreferences.getString("current_user_full_name", "Something went terribly wrong")!!
        fag = sharedPreferences.getInt("current_fag", 0)



        if (savedInstanceState != null) {
            savedStateSparseArray =
                savedInstanceState.getSparseParcelableArray(SAVED_STATE_CONTAINER_KEY)!!
            currentSelectItemId = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY)
        }

        setContentView(R.layout.activity_main)
        btmNav.itemIconTintList = null //Color of the bottom icons

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


//        while(fosInt!=0) {
//
//        }
        setUpFragments()

//        val progressDialog: ProgressDialog
//
//        progressDialog = ProgressDialog(this)
//        progressDialog.max = 100
//        progressDialog.setMessage("Please wait...")
//        progressDialog.setTitle("My Application")
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
//        progressDialog.show()
//        val handle: Handler = object : Handler() {
//            override fun handleMessage(msg: Message?) {
//                super.handleMessage(msg)
//                progressDialog.incrementProgressBy(1)
//            }
//        }
//        Thread(Runnable {
//            try {
//                while (progressDialog.progress <= progressDialog
//                        .max
//                ) {
//                    Thread.sleep(100)
//                    handle.sendMessage(handle.obtainMessage())
//                    if (progressDialog.progress == progressDialog
//                            .max
//                    ) {
//                        progressDialog.dismiss()
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }).start()


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
        dashboardFragment = DashboardFragment.newInstance(accessToken, "Dashboard", fag)
        lecturersFragment = LecturersFragment.newInstance(accessToken, "Lecturers")
        subjectsFragment = SubjectsFragment.newInstance(accessToken, "Subjects", fosInt)
        memesFragment = MemesFragment.newInstance(accessToken, "Memes")
        settingsFragment = SettingsFragment.newInstance(accessToken, "Settings", fos, fullName)
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


