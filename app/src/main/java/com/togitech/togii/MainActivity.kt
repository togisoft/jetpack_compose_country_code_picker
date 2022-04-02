package com.togitech.togii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.checkPhoneNumber
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
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

            SelectCountryWithCountryCode()
        }
    }

    @Composable
    fun SelectCountryWithCountryCode() {
        val getDefaultLangCode = getDefaultLangCode()
        val getDefaultPhoneCode = getDefaultPhoneCode()
        var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode) }
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode) }
        var verifyText by remember { mutableStateOf("") }
        var isValidPhone by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = verifyText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            TogiCountryCodePicker(
                pickedCountry = {
                    phoneCode = it.countryPhoneCode
                    defaultLang = it.countryCode
                },
                defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.primary,
                dialogAppBarTextColor = Color.Black,
                dialogAppBarColor = Color.White,
                error = isValidPhone,
                text = phoneNumber.value,
                onValueChange = { phoneNumber.value = it }
            )

            val fullPhoneNumber = "$phoneCode${phoneNumber.value}"
            val checkPhoneNumber = checkPhoneNumber(
                phone = phoneNumber.value,
                fullPhoneNumber = fullPhoneNumber,
                countryCode = defaultLang
            )
            Button(
                onClick = {
                    verifyText = if (checkPhoneNumber) {
                        isValidPhone = true
                        "Phone Number Correct"
                    } else {
                        isValidPhone = false
                        "Phone Number is Wrong"

                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp)
            ) {
                Text(text = "Phone Verify")
            }
        }
    }
}
