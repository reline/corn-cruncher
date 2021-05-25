package com.github.reline.corncruncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.reline.corncruncher.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent(
    viewModel: MainViewModel = viewModel()
) {
    val tankSize by viewModel.tankSize.observeAsState()
    val desiredEthanol by viewModel.desiredEthanol.observeAsState()
    val fuelOneEthanol by viewModel.fuelOneEthanol.observeAsState()
    val fuelTwoEthanol by viewModel.fuelTwoEthanol.observeAsState()
    val resultOne by viewModel.resultOne.observeAsState()
    val resultTwo by viewModel.resultTwo.observeAsState()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        CalculatorContent(
            tankSize,
            null,
            10f,
            desiredEthanol,
            fuelOneEthanol,
            fuelTwoEthanol,
            resultOne,
            resultTwo
        ) { tankSizeGallons: Float,
            currentFuelPercentage: Float,
            currentEthanolPercentage: Float,
            desiredEthanolPercentage: Float,
            fuelOneEthanolPercentage: Float,
            fuelTwoEthanolPercentage: Float ->
            run {
                Timber.d("hi")
                viewModel.onCalculateClicked(
                    tankSizeGallons,
                    currentFuelPercentage,
                    currentEthanolPercentage,
                    desiredEthanolPercentage,
                    fuelOneEthanolPercentage,
                    fuelTwoEthanolPercentage
                )
            }
        }
    }
}

typealias CalculateClick = (
    tankSizeGallons: Float,
    currentFuelPercentage: Float,
    currentEthanolPercentage: Float,
    desiredEthanolPercentage: Float,
    fuelOneEthanolPercentage: Float,
    fuelTwoEthanolPercentage: Float,
) -> Unit

@Composable
fun CalculatorContent(
    tankSize: Float?,
    tankPercentageRemaining: Float?,
    currentEthanol: Float,
    desiredEthanol: Float?,
    fuelOneEthanol: Float?,
    fuelTwoEthanol: Float?,
    resultOne: Float?,
    resultTwo: Float?,
    onCalculateClicked: CalculateClick,
) {
    var tank by rememberSaveable { mutableStateOf(tankSize) }
    var tankPercentage by rememberSaveable { mutableStateOf(tankPercentageRemaining) }
    var current by rememberSaveable { mutableStateOf(currentEthanol) }
    var target by rememberSaveable { mutableStateOf(desiredEthanol) }
    var one by rememberSaveable { mutableStateOf(fuelOneEthanol) }
    var two by rememberSaveable { mutableStateOf(fuelTwoEthanol) }

    val valid = { tank != null && tankPercentage != null && target != null && one != null && two != null }

    val calculate = {
        if (valid()) {
            Timber.d("valid")
        } else {
            Timber.d("invalid: $tank, $tankPercentage, $target, $one, $two")
        }
        ifLet(
            tank,
            tankPercentage,
            target,
            one,
            two
        ) { (tankSize, fuelRemaining, targetEthanol, fuelOne, fuelTwo) ->
            Timber.d("heyheyhey")
            onCalculateClicked(
                tankSize,
                fuelRemaining,
                current,
                targetEthanol,
                fuelOne,
                fuelTwo,
            )
        } // todo: else?
    }

    val imeAction = if (valid()) {
        ImeAction.Done
    } else {
        ImeAction.Next
    }
    val keyboardActions = KeyboardActions(onDone = { calculate() }) // todo: onNext

    Column {
        Row {
            TextField(
                value = tank.toString(),
                onValueChange = {
                    try {
                        tank = it.toFloat()
                    } catch (e: NumberFormatException) {
                    }
                },
                label = { Text("Tank Size (gal)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
            TextField(
                value = tankPercentage?.toString() ?: "",
                onValueChange = {
                    tankPercentage = it.toFloat()
                },
                label = { Text("Fuel Remaining (%)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
        }
        Row {
            TextField(
                value = current.toString(),
                onValueChange = {
                    current = it.toFloat()
                },
                label = { Text("Current Ethanol (%)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
            TextField(
                value = target.toString(),
                onValueChange = {
                    target = it.toFloat()
                },
                label = { Text("Target Ethanol (%)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
        }
        Row {
            TextField(
                value = one.toString(),
                onValueChange = {
                    one = it.toFloat()
                },
                label = { Text("Fuel #1 (%)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
            TextField(
                value = two.toString(),
                onValueChange = {
                    two = it.toFloat()
                },
                label = { Text("Fuel #2 (%)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction,
                ),
                keyboardActions = keyboardActions,
            )
        }
        if (resultOne != null && resultTwo != null) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    "%.2f gal".format(resultOne),
                    modifier = Modifier
//                    .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    "%.2f gal".format(resultTwo),
                    modifier = Modifier
//                    .weight(1f)
                        .align(Alignment.CenterVertically),
                )
            }
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { calculate() }
        ) {
            Text("Calculate")
        }
    }
}

@Preview
@Composable
fun CalculatorPreview() {
    CalculatorContent(
        12.4f,
        20f,
        27f,
        30f,
        10f,
        52f,
        4.5f,
        5.4f,
    ) { _, _, _, _, _, _ -> }
}
