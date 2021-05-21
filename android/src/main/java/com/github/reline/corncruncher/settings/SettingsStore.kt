package com.github.reline.corncruncher.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.reline.Settings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

val Context.settingsStore: DataStore<Settings> by dataStore(
	fileName = "settings",
	serializer = SettingsSerializer,
	produceMigrations = { emptyList() },
)

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

	@Provides
	fun provideSettingsStore(
		@ApplicationContext context: Context
	): DataStore<Settings> = context.settingsStore
}