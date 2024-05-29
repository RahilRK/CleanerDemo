package com.base.hilt.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.base.hilt.R
import com.base.hilt.ui.theme.PrimaryColor400
import com.base.hilt.ui.theme.SecondaryLabelColor
import com.base.hilt.ui.theme.SeparatorLightOpaque

@Composable
fun TopBarC(
    onBackPress: () -> Unit = {},
    title: String = stringResource(id = R.string.settings)
) {
    TopAppBar(
        modifier = Modifier,
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                    tint = PrimaryColor400
                )
            }
        },
        title = {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                text = title,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
//                    fontFamily = FontFamily(Font(R.font.sf pro rounded)),
                    fontWeight = FontWeight(700),
                    color = SecondaryLabelColor,
                    letterSpacing = 0.08.sp,
                )
            )

        },
        actions = {

            IconButton(onClick = {

            }, modifier = Modifier.alpha(0f)) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    contentDescription = "",
                    tint = PrimaryColor400
                )
            }

        }
    )
}

@Preview
@Composable
fun TopBarPreview() {

    TopBarC()
}