package com.base.hilt.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.base.hilt.BuildConfig
import com.base.hilt.R
import com.base.hilt.model.SettingsMenuDataItem
import com.base.hilt.ui.composable.TopBarC
import com.base.hilt.ui.theme.SecondaryLabelColor
import com.base.hilt.ui.theme.SeparatorLightOpaque
import com.base.hilt.ui.theme.BackgroundLightTertiary

@Composable
fun Settings() {

    val TAG = "Settings"

    val mViewModel: SettingsViewModel = hiltViewModel()
    val mSettingMenuList = mViewModel.settingsMenuList
    val settingsMenuListState: LazyListState = rememberLazyListState()

    Scaffold(
        backgroundColor = BackgroundLightTertiary,
        topBar = {
            TopBarC()
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                color = SeparatorLightOpaque.copy(alpha = 0.36f),
                thickness = 1.dp
            )

            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(139.dp)
                        .padding(top = 16.dp).background(color = BackgroundLightTertiary),
                    painter = painterResource(id = R.drawable.ic_card_pro_offer),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.padding(vertical = 14.dp))

                Card(modifier = Modifier, shape = RoundedCornerShape(8.dp)) {

                    LazyColumn(
                        modifier = Modifier.height(290.dp),
                        state = settingsMenuListState
                    ) {
                        itemsIndexed(mSettingMenuList) { position, model ->

                            if (position in 0..5) {
                                SettingsMenu(position = 0, model = model)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                Card(modifier = Modifier, shape = RoundedCornerShape(8.dp)) {

                    LazyColumn(
                        modifier = Modifier.height(146.dp),
                        state = settingsMenuListState
                    ) {
                        itemsIndexed(mSettingMenuList) { position, model ->

                            if (position in 6..8) {
                                SettingsMenu(position = 0, model = model)
                            }
                        }
                    }
                }

                Image(
                    modifier = Modifier
                        .padding(top = 84.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_brandingicon),
                    contentDescription = "image description",
                    contentScale = ContentScale.None
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    text = BuildConfig.VERSION_NAME,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
//                        fontFamily = FontFamily(Font(R.font.sf pro rounded)),
                        fontWeight = FontWeight(400),
                        color = SecondaryLabelColor,
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun SettingsMenu(
    position: Int = 0,
    model: SettingsMenuDataItem = SettingsMenuDataItem(
        menuIcon = R.drawable.ic_restore_purchase,
        menuName = "Restore Purchase"
    )
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {

        }) {

        Row(Modifier.fillMaxWidth()) {

            IconButton(modifier = Modifier, onClick = {

            }) {
                Image(
                    painterResource(model.menuIcon),
                    contentDescription = "",
                )
            }

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = model.menuName,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
//                fontFamily = FontFamily(Font(R.font.sf pro rounded)),
                    fontWeight = FontWeight(400),
                    color = SecondaryLabelColor,
                    letterSpacing = 0.06.sp,
                )
            )

        }

        IconButton(modifier = Modifier.align(Alignment.BottomEnd), onClick = {

        }) {
            Image(
                painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
            )
        }

        if(model.menuName != "Themes" && model.menuName != "Privacy Policy")
        {
            Divider(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp),
                color = SeparatorLightOpaque.copy(alpha = 0.36f),
                thickness = 1.dp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {

    Settings()
}