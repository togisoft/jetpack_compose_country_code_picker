package com.togitech.ccp.component

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation

@Composable
fun TogiCountryCodePicker(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    defaultCountry: CountryData = getLibCountries().first(),
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
    var phoneNumber by rememberSaveable { mutableStateOf("") }

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
                modifier = modifier.fillMaxWidth(),

                value = phoneNumber,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = unfocusedBorderColor,
                    cursorColor = cursorColor
                ),
                onValueChange = { newPhone ->
                    phoneNumber = newPhone
                },
                singleLine = true,
                visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                placeholder = { Text(text = stringResource(id = getNumberHint(defaultCountry.countryCode))) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                leadingIcon = {
                    Row {
                        Column() {
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