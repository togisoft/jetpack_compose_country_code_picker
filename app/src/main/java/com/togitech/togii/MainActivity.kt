package com.togitech.togii

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.togii.ui.theme.TogiiTheme
import kotlinx.collections.immutable.persistentSetOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TogiiTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(
                    color = MaterialTheme.colors.primary,
                    false,
                )
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.primary,
                    false,
                )
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(text = "Togisoft") }) },
                ) { top ->
                    top.calculateTopPadding()
                    Surface(modifier = Modifier.fillMaxSize()) {
                        CountryCodePick()
                    }
                }
            }
        }
    }
}

@Composable
fun CountryCodePick() {
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

        Spacer(modifier = Modifier.height(100.dp))

        TogiCountryCodePicker(
            text = phoneNumber.value,
            onValueChange = { (code, phone), isValid ->
                Log.d("CCP", "onValueChange: $code $phone -> $isValid")
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
        Spacer(modifier = Modifier.weight(1f))
    }
}
