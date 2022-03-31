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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultCountry
import com.togitech.ccp.data.utils.getDefaultCountryCode
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
        var selectedCountry by rememberSaveable { mutableStateOf(getDefaultCountryCode(this)) }
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        var defaultCountry by rememberSaveable { mutableStateOf(getDefaultCountry(this))}

        TogiCountryCodePicker(
            pickedCountry = {
                selectedCountry = it.countryPhoneCode
                defaultCountry = it.countryCode

            },
            defaultCountry = getLibCountries().single { it.countryCode == defaultCountry },
            dialogAppBarTextColor = Color.Black,
            dialogAppBarColor = Color.White,
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it }
        )

    }

    @Composable
    fun SelectCountryWithoutCountryCode() {
        var selectedCountry by rememberSaveable { mutableStateOf(getDefaultCountryCode(this)) }
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        var defaultCountry by rememberSaveable { mutableStateOf(getDefaultCountry(this))}

        TogiCountryCodePicker(
            pickedCountry = {
                selectedCountry = it.countryPhoneCode
                defaultCountry = it.countryCode
            },
            defaultCountry = getLibCountries().single { it.countryCode == defaultCountry},
            showCountryCode = false,
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it }
        )

        Text(text = "Number with * : $selectedCountry${phoneNumber.value}")
    }
}
