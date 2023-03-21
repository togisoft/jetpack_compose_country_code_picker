package com.togitech.ccp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.togitech.ccp.component.TogiCountryCodePicker
import org.junit.Rule
import org.junit.Test

class CountryCodePickTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun test() {
        paparazzi.snapshot {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val phoneNumber = rememberSaveable { mutableStateOf("") }
                val fullPhoneNumber = rememberSaveable { mutableStateOf("") }
                var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }

                TogiCountryCodePicker(
                    text = "text",
                    onValueChange = { (code, phone), isValid ->
                        phoneNumber.value = phone
                        fullPhoneNumber.value = code + phone
                        isNumberValid = isValid
                    },
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Full Phone Number: ${fullPhoneNumber.value}",
                    color = if (!isNumberValid) Color.Red else Color.Green,
                )

                Text(
                    text = "Only Phone Number: ${phoneNumber.value}",
                    color = if (!isNumberValid) Color.Red else Color.Green,
                )
            }
        }
    }
}
