package com.togitech.ccp.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    focusedBorderColorSearch: Color = Color.Transparent,
    unfocusedBorderColorSearch: Color = Color.Transparent,
    cursorColorSearch: Color = MaterialTheme.colors.primary,
    dialogAppBarColor: Color = MaterialTheme.colors.primary,
    dialogAppBarTextColor: Color = Color.White,

    rowPadding: Modifier = modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }
    val textFieldValue = textFieldValueState.copy(text = text)
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        shape = shape,
        color = color
    ) {
        Row(
            rowPadding,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth(),

                value = textFieldValue,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = unfocusedBorderColor,
                    cursorColor = cursorColor
                ),
                onValueChange = {
                    textFieldValueState = it
                    if (text != it.text) {
                        onValueChange(it.text)
                    }
                },
                singleLine = true,
                visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                placeholder = { Text(text = stringResource(id = getNumberHint(defaultCountry.countryCode))) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    autoCorrect = true,
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                leadingIcon = {
                    Row {
                        Column {
                            val dialog = TogiCodePicker()
                            dialog.TogiCodeDialog(
                                pickedCountry = pickedCountry,
                                defaultSelectedCountry = defaultCountry,
                                dialogAppBarColor = dialogAppBarColor,
                                showCountryCode = showCountryCode,
                                focusedBorderColorSearch = focusedBorderColorSearch,
                                unfocusedBorderColorSearch = unfocusedBorderColorSearch,
                                cursorColorSearch = cursorColorSearch,
                                dialogAppBarTextColor = dialogAppBarTextColor
                            )
                        }

                    }
                }
            )
        }

    }
}