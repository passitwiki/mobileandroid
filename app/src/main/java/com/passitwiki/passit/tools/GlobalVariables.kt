package com.passitwiki.passit.tools

import android.content.Context
import android.content.SharedPreferences
import com.passitwiki.passit.models.User

var globalContext : Context? = null
var globalUser : User? = null
var globalToken : String? = null
var globalRefresh: String? = null
var globalSharedPreferences: SharedPreferences? = null