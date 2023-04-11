package com.togitech.ccp.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.togitech.ccp.R
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation


private var fullNumberState: String by mutableStateOf("")
private var checkNumberState: Boolean by mutableStateOf(false)
private var phoneNumberState: String by mutableStateOf("")
private var countryCodeState: String by mutableStateOf("")


@Composable
fun TogiCountryCodePicker(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    bottomStyle: Boolean = false,
    textStyleDefault: TextStyle = TextStyle.Default,
    textStyleHint: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.onBackground,
        fontSize = MaterialTheme.typography.body1.fontSize,
    ),
    textStyleError: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.error,
        fontSize = MaterialTheme.typography.caption.fontSize,
    ),
    countryCodeStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onBackground,
        fontSize = MaterialTheme.typography.body1.fontSize,
    ),
    countryCodeDialogBackgroundColor: Color,
    changeStatustoErrorIfError: Boolean = true,
   turnLiveErrorStatusOn: Boolean = false,
    liveErrorStatus: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current
    var phoneCode by rememberSaveable {
        mutableStateOf(
            getDefaultPhoneCode(
                context
            )
        )
    }
    var defaultLang by rememberSaveable {
        mutableStateOf(
            getDefaultLangCode(context)
        )
    }

    countryCodeState = defaultLang
    fullNumberState = phoneCode + textFieldValue
    phoneNumberState = textFieldValue


    Surface(color = color) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            if (bottomStyle) {
                TogiCodeDialog(
                    pickedCountry = {
                        phoneCode = it.countryPhoneCode
                        defaultLang = it.countryCode
                    },
                    defaultSelectedCountry = getLibCountries.single { it.countryCode == defaultLang },
                    showCountryCode = showCountryCode,
                    showFlag = showCountryFlag,
                    showCountryName = true,
                    backgroundColor = countryCodeDialogBackgroundColor,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(modifier = modifier.fillMaxWidth(),
                    shape = shape,
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        if (text != it) {
                            onValueChange(it)
                        }
                        fullNumberState = phoneCode + textFieldValue
                        phoneNumberState = textFieldValue

                        if (turnLiveErrorStatusOn) {
                            liveErrorStatus(isPhoneNumber())
                        }

                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (getErrorStatus() && changeStatustoErrorIfError) Color.Red else focusedBorderColor,
                        unfocusedBorderColor = if (getErrorStatus() && changeStatustoErrorIfError) Color.Red else unfocusedBorderColor,
                        cursorColor = cursorColor,
                    ),
                    textStyle = textStyleDefault,
                    visualTransformation = PhoneNumberTransformation(getLibCountries.single { it.countryCode == defaultLang }.countryCode.uppercase()),
                    placeholder = { Text(text = stringResource(id = getNumberHint(getLibCountries.single { it.countryCode == defaultLang }.countryCode.lowercase())), style = textStyleHint) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hideSoftwareKeyboard()
                    }),
                    leadingIcon = {
                        if (!bottomStyle)
                            Row {
                                Column {
                                    TogiCodeDialog(
                                        pickedCountry = {
                                            phoneCode = it.countryPhoneCode
                                            defaultLang = it.countryCode
                                        },
                                        defaultSelectedCountry = getLibCountries.single { it.countryCode == defaultLang },
                                        showCountryCode = showCountryCode,
                                        showFlag = showCountryFlag,
                                        textStyleDefault = countryCodeStyle,
                                        backgroundColor = countryCodeDialogBackgroundColor,
                                    )
                                }
                            }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            textFieldValue = ""
                            onValueChange("")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear",
                                tint = if (getErrorStatus() && changeStatustoErrorIfError) textStyleError.color else textStyleDefault.color,
                            )
                        }
                    })
            }
            if (getErrorStatus() && changeStatustoErrorIfError) Text(
                text = stringResource(id = R.string.invalid_number),
                style = textStyleError,
                modifier = Modifier.padding(top = 0.8.dp)
            )
        }
    }
}

fun getFullPhoneNumber(): String {
    return fullNumberState
}

fun getOnlyPhoneNumber(): String {
    return phoneNumberState
}

fun getErrorStatus(): Boolean {
    return checkNumberState
}

fun isPhoneNumber(): Boolean {
    val check = com.togitech.ccp.data.utils.checkPhoneNumber(
        phone = phoneNumberState, fullPhoneNumber = fullNumberState, countryCode = countryCodeState
    )
    checkNumberState = check
    return check
}