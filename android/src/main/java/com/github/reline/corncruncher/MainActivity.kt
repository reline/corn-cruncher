package com.github.reline.corncruncher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.reline.corncruncher.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        CalculatorContent(
            tankSize,
            desiredEthanol,
            fuelOneEthanol,
            fuelTwoEthanol,
            resultOne,
            resultTwo
        ) { tank, target, one, two ->
            // fixme
            viewModel.onCalculateClicked(tank, 0f, 10f, target, one, two)
        }
    }
}

@Composable
fun CalculatorContent(
    tankSize: Float?,
    desiredEthanol: Float?,
    fuelOneEthanol: Float?,
    fuelTwoEthanol: Float?,
    resultOne: Float?,
    resultTwo: Float?,
    onCalculateClicked: (Float, Float, Float, Float) -> Unit = { _, _, _, _ -> }
) {
    var tank by rememberSaveable { mutableStateOf(tankSize) }
    var target by rememberSaveable { mutableStateOf(desiredEthanol) }
    var one by rememberSaveable { mutableStateOf(fuelOneEthanol) }
    var two by rememberSaveable { mutableStateOf(fuelTwoEthanol) }

    Column {
        Row {
            TextField(
                value = tank.toString(),
                onValueChange = {
                    tank = it.toFloat()
                },
                label = { Text("Tank Size (gal)") },
                modifier = Modifier.weight(1f),
            )
            TextField(
                value = target.toString(),
                onValueChange = {
                    target = it.toFloat()
                },
                label = { Text("Target Ethanol (%)") },
                modifier = Modifier.weight(1f),
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
            )
            TextField(
                value = two.toString(),
                onValueChange = {
                    two = it.toFloat()
                },
                label = { Text("Fuel #2 (%)") },
                modifier = Modifier.weight(1f),
            )
        }
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
        Button(
            onClick = { onCalculateClicked(tank!!, target!!, one!!, two!!) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
        30f,
        10f,
        52f,
        4.5f,
        5.0f,
    )
}
