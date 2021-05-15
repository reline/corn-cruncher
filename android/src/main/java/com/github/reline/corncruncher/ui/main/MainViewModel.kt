package com.github.reline.corncruncher.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.reline.corncruncher.Calculator
import com.github.reline.corncruncher.settings.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val repository: SettingsRepository) : ViewModel() {

	val tankSize = MutableLiveData(10f)
	val desiredEthanol = MutableLiveData(54f)
	val fuelOneEthanol = MutableLiveData(15f)
	val fuelTwoEthanol = MutableLiveData(85f)
	val resultOne = MutableLiveData<Float>()
	val resultTwo = MutableLiveData<Float>()

	init {
		viewModelScope.launch {
			repository.tankSize.firstOrNull()?.let { tankSize.value = it }
			repository.desiredEthanol.firstOrNull()?.let { desiredEthanol.value = it }
			repository.actualEthanolOne.firstOrNull()?.let { fuelOneEthanol.value = it }
			repository.actualEthanolTwo.firstOrNull()?.let { fuelTwoEthanol.value = it }
		}
	}

	fun onCalculateClicked(
		tankSizeGallons: Float,
		currentFuelPercentage: Float,
		currentEthanolPercentage: Float,
		desiredEthanolPercentage: Float,
		fuelOneEthanolPercentage: Float,
		fuelTwoEthanolPercentage: Float,
	) = viewModelScope.launch {
		val result = try {
			Calculator.determineFuelRequired(
				tankSizeGallons,
				currentFuelPercentage,
				currentEthanolPercentage,
				fuelOneEthanolPercentage,
				fuelTwoEthanolPercentage,
				desiredEthanolPercentage
			)
		} catch (e: IllegalArgumentException) {
			Timber.e(e)
			return@launch
		}

		resultOne.value = result.first.gallons
		resultTwo.value = result.second.gallons

		repository.saveTankSize(tankSizeGallons)
		repository.saveDesiredEthanol(desiredEthanolPercentage)
		repository.saveActualEthanolOne(fuelOneEthanolPercentage)
		repository.saveActualEthanolTwo(fuelTwoEthanolPercentage)
	}

	class Factory(
		private val repository: SettingsRepository,
	) : ViewModelProvider.Factory {
		override fun <T : ViewModel?> create(modelClass: Class<T>): T {
			@Suppress("UNCHECKED_CAST")
			return MainViewModel(repository) as T
		}
	}
}
