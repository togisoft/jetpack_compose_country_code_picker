package com.togitech.ccp.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.checkPhoneNumber
import com.togitech.ccp.data.utils.countryDataMap
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.data.utils.unitedStates
import com.togitech.ccp.transformation.PhoneNumberTransformation
import kotlinx.collections.immutable.ImmutableSet

private val DEFAULT_ROUNDING = 24.dp

@Suppress("LongMethod")
@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (Pair<String, String>, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(DEFAULT_ROUNDING),
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    fallbackCountry: CountryData = unitedStates,
    showPlaceholder: Boolean = true,
    includeOnly: ImmutableSet<String>? = null,
    clearIcon: ImageVector? = Icons.Filled.Clear,
) {
    val context = LocalContext.current
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current
    var langAndCode by rememberSaveable {
        mutableStateOf(getDefaultPhoneCode(context, fallbackCountry))
    }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        value = phoneNumber,
        onValueChange = {
            phoneNumber = it
            if (text != it) {
                isNumberValid = checkPhoneNumber(
                    fullPhoneNumber = langAndCode.second + phoneNumber,
                    countryCode = langAndCode.second
                )
                onValueChange(langAndCode.second to phoneNumber, isNumberValid)
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
                langAndCode.first,
                fallbackCountry,
            ).countryCode.uppercase(),
        ),
        placeholder = {
            if (showPlaceholder) {
                PlaceholderNumberHint(langAndCode, fallbackCountry)
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
            TogiCodeDialog(
                onCountryChange = {
                    langAndCode = it.countryCode to it.countryPhoneCode
                },
                defaultSelectedCountry = countryDataMap.getOrDefault(
                    langAndCode.first,
                    fallbackCountry,
                ),
                showCountryCode = showCountryCode,
                showFlag = showCountryFlag,
                includeOnly = includeOnly,
            )
        },
        trailingIcon = {
            clearIcon?.let {
                IconButton(
                    onClick = {
                        phoneNumber = ""
                        isNumberValid = false
                        onValueChange(langAndCode.second to phoneNumber, isNumberValid)
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

@Composable
private fun PlaceholderNumberHint(
    langAndCode: Pair<String, String>,
    fallbackCountry: CountryData,
) {
    Text(
        text = stringResource(
            id = getNumberHint(
                countryDataMap.getOrDefault(
                    langAndCode.first,
                    fallbackCountry,
                ).countryCode.lowercase(),
            ),
        ),
    )
}

@Preview
@Composable
fun TogiCountryCodePickerPreview() {
    TogiCountryCodePicker(
        text = "",
        onValueChange = { _, _ -> },
        showCountryCode = true,
        showCountryFlag = true,
        showPlaceholder = true,
        includeOnly = null,
    )
}
