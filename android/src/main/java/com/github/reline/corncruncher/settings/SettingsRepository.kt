package com.github.reline.corncruncher.settings

import androidx.datastore.core.DataStore
import com.github.reline.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class SettingsRepository(private val settingsStore: DataStore<Settings>) {

	val tankSize: Flow<Float>
		get() = settingsStore.data.map { it.tankSizeGallons }

	val desiredEthanol: Flow<Float>
		get() = settingsStore.data.map { it.desiredEthanolPercentage }

	val actualEthanolOne: Flow<Float>
		get() = settingsStore.data.map { it.fuelOneEthanolPercentage }

	val actualEthanolTwo: Flow<Float>
		get() = settingsStore.data.map { it.fuelTwoEthanolPercentage }

	suspend fun saveTankSize(float: Float) {
		settingsStore.updateData { settings ->
			settings.toBuilder()
				.setTankSizeGallons(float)
				.build()
		}
	}

	suspend fun saveDesiredEthanol(float: Float) {
		settingsStore.updateData { settings ->
			settings.toBuilder()
				.setDesiredEthanolPercentage(float)
				.build()
		}
	}

	suspend fun saveActualEthanolOne(float: Float) {
		settingsStore.updateData { settings ->
			settings.toBuilder()
				.setFuelOneEthanolPercentage(float)
				.build()
		}
	}

	suspend fun saveActualEthanolTwo(float: Float) {
		settingsStore.updateData { settings ->
			settings.toBuilder()
				.setFuelTwoEthanolPercentage(float)
				.build()
		}
	}
}
