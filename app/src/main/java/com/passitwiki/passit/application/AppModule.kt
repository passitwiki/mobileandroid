package com.passitwiki.passit.application

import com.passitwiki.passit.fragment.*
import com.passitwiki.passit.networking.ResponseHandler
import com.passitwiki.passit.networking.provideApi
import com.passitwiki.passit.networking.provideOkHttpClient
import com.passitwiki.passit.networking.provideRetrofit
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import com.passitwiki.passit.viewmodel.CalendarViewModel
import com.passitwiki.passit.viewmodel.DashboardViewModel
import com.passitwiki.passit.viewmodel.LecturersViewModel
import com.passitwiki.passit.viewmodel.SubjectsViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Application module for Koin specification of all factories/singles in one place.
 */
val appModule = module {
    factory { provideOkHttpClient() }
    factory { provideApi(get()) }
    single { provideRetrofit(get()) }
    factory { ResponseHandler() }
    factory { Repository(get(), get()) }
    single { Utilities(get()) }
    fragment { CalendarFragment("Calendar") }
    viewModel { CalendarViewModel(get()) }
    fragment { (fag:Int) -> DashboardFragment("Dashboard", fag)}
    viewModel { DashboardViewModel(get()) }
    fragment { LecturersFragment("Lecturers") }
    viewModel { LecturersViewModel(get()) }
    fragment { MemesFragment("Memes") }
    fragment { (fos:String, fullName:String) -> SettingsFragment("Settings", fos, fullName) }
    fragment { SubjectsFragment("Subjects") }
    viewModel { SubjectsViewModel(get()) }
}