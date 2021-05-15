package com.github.reline.corncruncher.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.reline.corncruncher.databinding.MainFragmentBinding
import com.github.reline.corncruncher.hideKeyboard
import com.github.reline.corncruncher.settings.SettingsRepository
import com.github.reline.corncruncher.settings.settingsStore

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

	private lateinit var viewModel: MainViewModel

	private lateinit var binding: MainFragmentBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(
			this,
			MainViewModel.Factory(SettingsRepository(requireContext().settingsStore))
		).get(MainViewModel::class.java)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View {
		binding = MainFragmentBinding.inflate(inflater, container, false)
		return binding.root
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.calculateButton.setOnClickListener {
			val tankSize = binding.tankSize.text.toString().toFloat()
			val fuelLevel = binding.fuelLevel.text.toString().toFloat()
			val currentBlend = binding.currentBlend.text.toString().toFloat()
			val desiredBlend = binding.desiredBlend.text.toString().toFloat()
			val fuelOneBlend = binding.fuelOneBlend.text.toString().toFloat()
			val fuelTwoBlend = binding.fuelTwoBlend.text.toString().toFloat()
			viewModel.onCalculateClicked(tankSize, fuelLevel, currentBlend, desiredBlend, fuelOneBlend, fuelTwoBlend)
			activity?.hideKeyboard()
		}

		viewModel.tankSize.observe(this) {
			binding.tankSize.setText(it.toString())
		}

		viewModel.desiredEthanol.observe(this) {
			binding.desiredBlend.setText(it.toString())
		}

		viewModel.fuelOneEthanol.observe(this) {
			binding.fuelOneBlend.setText(it.toString())
		}

		viewModel.fuelTwoEthanol.observe(this) {
			binding.fuelTwoBlend.setText(it.toString())
		}

		viewModel.resultOne.observe(this) {
			binding.fuelOneResult.text = "%.2f".format(it)
		}

		viewModel.resultTwo.observe(this) {
			binding.fuelTwoResult.text = "%.2f".format(it)
		}
	}
}
