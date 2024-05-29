package com.base.hilt.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.base.hilt.R

@Composable
fun SplashScreen(
    onButtonClick: () -> Unit = {}
) {

    val TAG = "SplashScreen"

    Box(
        Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.ic_splash_background),
                contentScale = ContentScale.Crop
            )
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            Image(
                modifier = Modifier
                    .width(146.70401.dp)
                    .height(128.15878.dp),
                painter = painterResource(id = R.drawable.ic_appicon),
                contentDescription = "",
                contentScale = ContentScale.None
            )

            Image(
                modifier = Modifier
                    .padding(top = 22.84.dp),
                painter = painterResource(id = R.drawable.ic_brandinglogo),
                contentDescription = "image description",
                contentScale = ContentScale.None
            )
        }
    }
}

@Preview
@Composable
fun SplashPreview() {

    SplashScreen()
}