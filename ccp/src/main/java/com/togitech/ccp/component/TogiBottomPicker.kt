package com.togitech.ccp.component

import TogiCodePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation

@Composable
fun TogiBottomCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    showCountryName: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
) {
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current

    Surface(
        color = color
    ) {
        Column(
            modifier = rowPadding
        ) {
            val dialog = TogiCodePicker()
            dialog.TogiCodeDialog(
                pickedCountry = pickedCountry,
                defaultSelectedCountry = defaultCountry,
                showCountryCode = showCountryCode,
                showFlag = showCountryFlag,
                showCountryName = showCountryName
            )
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth(),

                value = textFieldValue,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (!error) Color.Red else focusedBorderColor,
                    unfocusedBorderColor = if (!error) Color.Red else unfocusedBorderColor,
                    cursorColor = cursorColor
                ),
                onValueChange = {
                    textFieldValue = it
                    if (text != it) {
                        onValueChange(it)
                    }
                },
                singleLine = true,
                visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                placeholder = { Text(text = stringResource(id = getNumberHint(defaultCountry.countryCode))) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    autoCorrect = true,
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hideSoftwareKeyboard() }),

                trailingIcon = {
                    IconButton(onClick = {
                        textFieldValue = ""
                        onValueChange("")
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear",
                            tint = if(!error) Color.Red else MaterialTheme.colors.onSurface)
                    }
                }
            )
            if (!error)
                Text(
                    text = stringResource(id = R.string.invalid_number),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 0.8.dp)
                )
        }
    }
}