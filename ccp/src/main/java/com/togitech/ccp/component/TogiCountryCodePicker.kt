package com.togitech.ccp.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.checkPhoneNumber
import com.togitech.ccp.data.utils.countryDataMap
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.data.utils.unitedStates
import com.togitech.ccp.transformation.PhoneNumberTransformation
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (Pair<String, String>, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    fallbackCountry: CountryData = unitedStates,
    showPlaceholder: Boolean = true,
    includeOnly: ImmutableSet<String>? = null,
    clearIcon: ImageVector? = null,
) {
    val context = LocalContext.current
    var phoneNumberState by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current
    var phoneCode by rememberSaveable {
        mutableStateOf(getDefaultPhoneCode(context, fallbackCountry))
    }
    var defaultLang by rememberSaveable {
        mutableStateOf(getDefaultLangCode(context))
    }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }
    var countryCodeState: String by rememberSaveable { mutableStateOf(defaultLang) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        value = phoneNumberState,
        onValueChange = {
            phoneNumberState = it
            if (text != it) {
                isNumberValid = checkPhoneNumber(phoneCode + phoneNumberState, countryCodeState)
                onValueChange(phoneCode to phoneNumberState, isNumberValid)
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (!isNumberValid) Color.Red else focusedBorderColor,
            unfocusedBorderColor = if (!isNumberValid) Color.Red else unfocusedBorderColor,
            cursorColor = cursorColor,
        ),
        visualTransformation = PhoneNumberTransformation(
            countryDataMap.getOrDefault(
                defaultLang,
                fallbackCountry,
            ).countryCode.uppercase(),
        ),
        placeholder = {
            if (showPlaceholder) {
                Text(
                    text = stringResource(
                        id = getNumberHint(
                            countryDataMap.getOrDefault(
                                defaultLang,
                                fallbackCountry,
                            ).countryCode.lowercase(),
                        ),
                    ),
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            autoCorrect = true,
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hideSoftwareKeyboard()
        }),
        leadingIcon = {
            Row {
                Column {
                    TogiCodeDialog(
                        onCountryChange = {
                            phoneCode = it.countryPhoneCode
                            defaultLang = it.countryCode
                        },
                        defaultSelectedCountry = countryDataMap.getOrDefault(
                            defaultLang,
                            fallbackCountry,
                        ),
                        showCountryCode = showCountryCode,
                        showFlag = showCountryFlag,
                        includeOnly = includeOnly,
                    )
                }
            }
        },
        trailingIcon = {
            clearIcon?.let {
                IconButton(
                    onClick = {
                        phoneNumberState = ""
                        isNumberValid = false
                        onValueChange(phoneCode to phoneNumberState, isNumberValid)
                    },
                ) {
                    Icon(
                        imageVector = it,
                        contentDescription = "Clear",
                        tint = if (!isNumberValid) Color.Red else MaterialTheme.colors.onSurface,
                    )
                }
            }
        },
    )
}
