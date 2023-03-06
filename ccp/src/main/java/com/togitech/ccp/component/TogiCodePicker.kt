package com.togitech.ccp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryCodeToEmojiFlag
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.data.utils.unitedStates
import com.togitech.ccp.utils.sortedByLocalizedName
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList

private val DEFAULT_PADDING = 10.dp

@Composable
fun TogiCodeDialog(
    defaultSelectedCountry: CountryData,
    includeOnly: ImmutableSet<String>?,
    onCountryChange: (CountryData) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = DEFAULT_PADDING,
    showCountryCode: Boolean = true,
    showFlag: Boolean = true,
    textColor: Color = MaterialTheme.colors.onSurface,
) {
    val context = LocalContext.current

    var country by remember { mutableStateOf(defaultSelectedCountry) }
    var isOpenDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .padding(padding)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                isOpenDialog = true
            },
    ) {
        CodePickerRow(
            showCountryCode = showCountryCode,
            showFlag = showFlag,
            country = country,
            textColor = textColor,
        )

        if (isOpenDialog) {
            val filteredCountryList = getLibCountries.sortedByLocalizedName(context)
                .let { countries ->
                    includeOnly?.map { it.lowercase() }?.let { includeLowercase ->
                        countries.filter { it.countryCode in includeLowercase }
                    } ?: countries
                }
            CountryDialog(
                onDismissRequest = { isOpenDialog = false },
                context = context,
                onSelect = { countryItem ->
                    onCountryChange(countryItem)
                    country = countryItem
                    isOpenDialog = false
                },
                filteredCountryList = filteredCountryList.toImmutableList(),
            )
        }
    }
}

@Composable
private fun CodePickerRow(
    showCountryCode: Boolean,
    showFlag: Boolean,
    country: CountryData,
    textColor: Color,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showCountryCode || showFlag) {
            Text(
                text = emojiCodeText(
                    showFlag = showFlag,
                    isPickCountry = country,
                    showCountryCode = showCountryCode,
                ),
                modifier = Modifier.padding(start = DEFAULT_PADDING),
                fontStyle = MaterialTheme.typography.body1.fontStyle,
                fontWeight = fontWeight,
                color = textColor,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = textColor,
            )
        }
    }
}

@Composable
private fun emojiCodeText(
    showFlag: Boolean,
    isPickCountry: CountryData,
    showCountryCode: Boolean,
) = if (showFlag) { countryCodeToEmojiFlag(isPickCountry.countryCode) } else { "" } +
    if (showCountryCode && showFlag) { "  " } else { "" } +
    if (showCountryCode) { isPickCountry.countryPhoneCode } else { "" }

@Preview
@Composable
private fun TogiCodeDialogPreview() {
    TogiCodeDialog(
        defaultSelectedCountry = unitedStates,
        includeOnly = null,
        onCountryChange = {},
    )
}
