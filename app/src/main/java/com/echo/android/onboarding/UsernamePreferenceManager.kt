package com.echo.android.onboarding

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages username preference for the Echo app.
 * Stores and retrieves the user's chosen nickname.
 */
object UsernamePreferenceManager {
    private const val PREFS_NAME = "echo_username_prefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_HAS_SET_USERNAME = "has_set_username"

    private lateinit var prefs: SharedPreferences
    
    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: StateFlow<String> = _usernameFlow.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _usernameFlow.value = getUsername()
    }

    fun hasSetUsername(): Boolean {
        return prefs.getBoolean(KEY_HAS_SET_USERNAME, false)
    }

    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }

    fun setUsername(name: String) {
        prefs.edit()
            .putString(KEY_USERNAME, name)
            .putBoolean(KEY_HAS_SET_USERNAME, true)
            .apply()
        _usernameFlow.value = name
    }
}
