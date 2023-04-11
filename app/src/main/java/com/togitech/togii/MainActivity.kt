package com.togitech.togii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.platform.LocalTextInputService
import com.togitech.ccp.component.*
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
                    topBar = { TopAppBar(title = { Text(text = "Togisoft") }) }) { top ->
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val phoneNumber = rememberSaveable { mutableStateOf("") }
        val fullPhoneNumber = rememberSaveable { mutableStateOf("") }
        val onlyPhoneNumber = rememberSaveable { mutableStateOf("") }
        val errorStatus = rememberSaveable { mutableStateOf(false) }
        val liveErrorStatus = rememberSaveable { mutableStateOf(false) }
        val keyboardController = LocalTextInputService.current

        TogiCountryCodePicker(
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            unfocusedBorderColor = MaterialTheme.colors.primary,
            bottomStyle = false,
            shape = RoundedCornerShape(24.dp),
            textStyleDefault = TextStyle(
                fontWeight = FontWeight.Normal,
                color = Color.Yellow,
                fontSize = MaterialTheme.typography.body1.fontSize,
            ),
            textStyleHint = TextStyle(
                fontWeight = FontWeight.Thin,
                color = Color.Gray,
                fontSize = MaterialTheme.typography.body1.fontSize,
            ),
            textStyleError = TextStyle(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.error,
                fontSize = MaterialTheme.typography.body1.fontSize,
            ),
            countryCodeStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                fontSize = MaterialTheme.typography.body1.fontSize,
            ),
            countryCodeDialogBackgroundColor = Color.DarkGray,
            changeStatustoErrorIfError = errorStatus.value,
            turnLiveErrorStatusOn = true,
            liveErrorStatus = {
                liveErrorStatus.value = it
                if (!it) {
                    errorStatus.value = false
                    keyboardController?.hideSoftwareKeyboard()
                }
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (!isPhoneNumber()) {
                fullPhoneNumber.value = getFullPhoneNumber()
                onlyPhoneNumber.value = getOnlyPhoneNumber()

            } else {
                fullPhoneNumber.value = "Error"
                onlyPhoneNumber.value = "Error"
                errorStatus.value = true
            }
        }) {
            Text(text = "Check")
        }

        Text(
            text = "Full Phone Number: ${fullPhoneNumber.value}",
            color = if (getErrorStatus() && errorStatus.value) Color.Red else Color.Green
        )

        Text(
            text = "Only Phone Number: ${onlyPhoneNumber.value}",
            color = if (getErrorStatus() && errorStatus.value) Color.Red else Color.Green
        )
    }
}
