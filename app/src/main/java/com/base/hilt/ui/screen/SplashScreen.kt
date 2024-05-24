package com.base.hilt.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    onButtonClick: () -> Unit = {}
) {

    val TAG = "SplashScreen"

    Column(Modifier.fillMaxSize()) {

        Text(text = TAG)
        Button(onClick = {
            onButtonClick()
        }) {

            Text(text = "Click here")
        }
    }
}