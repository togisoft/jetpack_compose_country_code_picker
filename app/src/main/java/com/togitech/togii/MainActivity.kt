package com.togitech.togii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultCountry
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.togii.ui.theme.TogiiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TogiiTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(
                    color = MaterialTheme.colors.primary,
                    false
                )
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.primary,
                    false
                )
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(text = "Togisoft") }) }) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SelectCountryBody()
                    }
                }
            }
        }
    }

    @Composable
    fun SelectCountryBody() {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "With Country Code")
            SelectCountryWithCountryCode()
            Text(text = "Without Country Code")
            SelectCountryWithoutCountryCode()
        }
    }

    @Composable
    fun SelectCountryWithCountryCode() {
        var selectedItem by rememberSaveable { mutableStateOf(getDefaultCountry(this)) }
        TogiCountryCodePicker(
            pickedCountry = {
                selectedItem = it.countryCode
            },
            defaultCountry = getLibCountries().single { it.countryCode == selectedItem },
            dialogAppBarTextColor = Color.Black,
            dialogAppBarColor = Color.White
        )
    }

    @Composable
    fun SelectCountryWithoutCountryCode() {
        var selectedItem by rememberSaveable { mutableStateOf(getDefaultCountry(this)) }
        TogiCountryCodePicker(
            pickedCountry = {
                selectedItem = it.countryCode
            },
            defaultCountry = getLibCountries().single { it.countryCode == selectedItem },
            showCountryCode = false
        )

    }
}
