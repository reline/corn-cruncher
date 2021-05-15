package com.github.reline.corncruncher.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.reline.Settings

val Context.settingsStore: DataStore<Settings> by dataStore(
	fileName = "settings",
	serializer = SettingsSerializer,
	produceMigrations = { emptyList() },
)
