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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryCodeToEmojiFlag
import com.togitech.ccp.data.utils.countryNames
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.data.utils.unitedStates
import com.togitech.ccp.utils.sortedByLocalizedName
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TogiCodeDialog(
    defaultSelectedCountry: CountryData,
    includeOnly: ImmutableSet<String>?,
    onCountryChange: (CountryData) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 15.dp,
    showCountryCode: Boolean = true,
    showFlag: Boolean = true,
    showCountryName: Boolean = false,
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
            isPickCountry = country,
            textColor = textColor,
            showCountryName = showCountryName,
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
    isPickCountry: CountryData,
    textColor: Color,
    showCountryName: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showCountryCode || showFlag) {
            Text(
                text = EmojiCodeText(showFlag, isPickCountry, showCountryCode),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp),
                fontSize = 18.sp,
                color = textColor,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = textColor,
            )
        }
        if (showCountryName) {
            Text(
                text = stringResource(
                    id = countryNames.getOrDefault(
                        isPickCountry.countryCode.lowercase(),
                        R.string.unknown,
                    ),
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
    }
}

@Composable
private fun EmojiCodeText(
    showFlag: Boolean,
    isPickCountry: CountryData,
    showCountryCode: Boolean,
) = if (showFlag) { countryCodeToEmojiFlag(isPickCountry.countryCode) } else { "" } +
    if (showCountryCode && showFlag) { "  " } else { "" } +
    if (showCountryCode) { isPickCountry.countryPhoneCode } else { "" }

@Preview
@Composable
fun TogiCodeDialogPreview() {
    TogiCodeDialog(
        defaultSelectedCountry = unitedStates,
        includeOnly = null,
        onCountryChange = {},
    )
}
