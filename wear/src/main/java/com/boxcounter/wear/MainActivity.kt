package com.boxcounter.wear

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.boxcounter.wear.data.DataBaseProvider
import com.boxcounter.wear.repository.ShiftRepo
import com.boxcounter.wear.viewModel.BoxCounterViewModel
import com.boxcounter.wear.viewModel.BoxCounterViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: BoxCounterViewModel by viewModels{
        val database = DataBaseProvider.getDataBase(this)
        val dao = database.shiftDao()
        val repository = ShiftRepo(dao)
        BoxCounterViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_DeviceDefault)

        setContent {
            MaterialTheme {
                val shift = viewModel.currentShift
                val currentQuantity = shift?.quantity ?:0
                BoxCounterScreen(
                    currentCount = currentQuantity,
                    onIncrement = { viewModel.increment() },
                    onDecrement = { viewModel.decrement() }
                )
            }
        }
    }
}

@Composable
fun BoxCounterScreen(
    currentCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
    ) {
        val counterColor = when (currentCount) {
            in 0..49 -> Color.White
            in 50..99 -> Color.Yellow
            else -> Color.Green
        }

        Scaffold(
            timeText = { TimeText() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp)
                    .background(Color(0xFF0F172A)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Cajas", style = MaterialTheme.typography.caption2)

                Text(
                    text = "$currentCount",
                    fontSize = 44.sp,
                    color = counterColor,
                    style = MaterialTheme.typography.display1)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDecrement,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red
                        )) {
                        Text(
                            "-",
                            style = MaterialTheme.typography.title2,
                            color = Color.White)
                    }
                    Button(
                        onClick = onIncrement,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Green
                        )) {
                        Text(
                            "+",
                            style = MaterialTheme.typography.title2,
                            color = Color.White)
                    }
                }
            }
        }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun BoxCounterPreview() {
    MaterialTheme {
        BoxCounterScreen(
            currentCount = 57,
            onIncrement = {},
            onDecrement = {}
        )
    }
}