package com.togitech.ccp.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
fun TogiRoundedPicker(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unFocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    dialogAppBarColor: Color = MaterialTheme.colors.primary,
    dialogAppBarTextColor: Color = Color.White,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 3.dp)
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    val keyboardController = LocalTextInputService.current


    Surface(color = color) {
        Column(modifier = rowPadding) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValueState = it
                        if (value != it.text) {
                            onValueChange(it.text)
                        }
                    },
                    modifier = modifier,
                    shape = shape,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (!error) Color.Red else focusedBorderColor,
                        unfocusedBorderColor = if (!error) Color.Red else unFocusedBorderColor,
                        cursorColor = cursorColor
                    ),
                    singleLine = true,
                    placeholder = { Text(text = stringResource(id = getNumberHint(defaultCountry.countryCode))) },
                    visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hideSoftwareKeyboard()
                    }),
                    trailingIcon = {
                        if (!error)
                            Icon(
                                imageVector = Icons.Filled.Warning, contentDescription = "Error",
                                tint = MaterialTheme.colors.error
                            )
                    },
                    leadingIcon = {
                        Row {
                            Column {
                                val dialog = TogiCodePicker()
                                dialog.TogiCodeDialog(
                                    pickedCountry = pickedCountry,
                                    defaultSelectedCountry = defaultCountry,
                                    dialogAppBarColor = dialogAppBarColor,
                                    dialogAppBarTextColor = dialogAppBarTextColor,
                                    showCountryCode = showCountryCode
                                )
                            }
                        }
                    }
                )
            }
            if (!error)
                Text(
                    text = stringResource(id = R.string.invalid_number),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.8.dp)
                )
        }
    }
}