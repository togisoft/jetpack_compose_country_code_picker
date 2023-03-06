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
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryDataMap
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.data.utils.isPhoneNumberValid
import com.togitech.ccp.data.utils.unitedStates
import com.togitech.ccp.transformation.PhoneNumberTransformation
import kotlinx.collections.immutable.ImmutableSet

private val DEFAULT_TEXT_FIELD_SHAPE = RoundedCornerShape(24.dp)

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("LongMethod")
@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (Pair<String, String>, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DEFAULT_TEXT_FIELD_SHAPE,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    fallbackCountry: CountryData = unitedStates,
    showPlaceholder: Boolean = true,
    includeOnly: ImmutableSet<String>? = null,
    clearIcon: ImageVector? = Icons.Filled.Clear,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalTextInputService.current
    var langAndCode by rememberSaveable {
        mutableStateOf(getDefaultPhoneCode(context, fallbackCountry))
    }
    var isNumberValid: Boolean by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .autofill(
                autofillTypes = listOf(AutofillType.PhoneNumberNational),
                onFill = { phoneNumber = it },
            )
            .focusRequester(focusRequester = focusRequester),
        shape = shape,
        value = phoneNumber,
        onValueChange = {
            phoneNumber = it
            if (text != it) {
                isNumberValid = isPhoneNumberValid(
                    fullPhoneNumber = langAndCode.second + phoneNumber,
                )
                onValueChange(langAndCode.second to phoneNumber, isNumberValid)
            }
        },
        singleLine = true,
        isError = !isNumberValid,
        colors = colors,
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
            keyboardType = KeyboardType.Phone,
            autoCorrect = true,
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hideSoftwareKeyboard()
        }),
        leadingIcon = {
            TogiCodeDialog(
                onCountryChange = {
                    langAndCode = it.countryCode to it.countryPhoneCode
                    isNumberValid = isPhoneNumberValid(
                        fullPhoneNumber = langAndCode.second + phoneNumber,
                    )
                    onValueChange(langAndCode.second to phoneNumber, isNumberValid)
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
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

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this.onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
    }.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }
}

@Preview
@Composable
private fun TogiCountryCodePickerPreview() {
    TogiCountryCodePicker(
        text = "",
        onValueChange = { _, _ -> },
        showCountryCode = true,
        showCountryFlag = true,
        showPlaceholder = true,
        includeOnly = null,
    )
}
