package com.togitech.ccp.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryCodeToEmojiFlag
import com.togitech.ccp.data.utils.countryNames
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.utils.searchCountry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CountryDialog(
    onSelect: (item: CountryData) -> Unit,
    context: Context,
    dialogStatus: Boolean,
    filteredCountryList: ImmutableList<CountryData>,
    onDismissRequest: () -> Unit,
) {
    var searchValue by remember { mutableStateOf("") }
    if (!dialogStatus) searchValue = ""

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp)),
            ) {
                Scaffold { scaffold ->
                    scaffold.calculateBottomPadding()
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchTextField(
                            value = searchValue,
                            onValueChange = { searchValue = it },
                            textColor = MaterialTheme.colors.onSurface,
                            fontSize = 16.sp,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colors.onSurface,
                                    modifier = Modifier.padding(horizontal = 3.dp),
                                )
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .height(40.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn {
                            items(
                                if (searchValue.isEmpty()) {
                                    filteredCountryList
                                } else {
                                    filteredCountryList.searchCountry(
                                        searchValue,
                                        context,
                                    )
                                },
                            ) { countryItem ->
                                Row(
                                    Modifier
                                        .padding(18.dp)
                                        .fillMaxWidth()
                                        .clickable(onClick = { onSelect(countryItem) }),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = countryCodeToEmojiFlag(countryItem.countryCode) +
                                            stringResource(
                                                id = countryNames.getOrDefault(
                                                    countryItem.countryCode.lowercase(),
                                                    R.string.unknown,
                                                ),
                                            ),
                                        modifier = Modifier.padding(horizontal = 18.dp),
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.Serif,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    hint: String = stringResource(id = R.string.search),
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize,
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = textColor,
            fontSize = fontSize,
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            hint,
                            style = LocalTextStyle.current.copy(
                                color = textColor,
                                fontSize = fontSize,
                            ),
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        },
    )
}

@Preview
@Composable
fun CountryDialogPreview() {
    CountryDialog(
        onSelect = {},
        context = LocalContext.current,
        dialogStatus = true,
        filteredCountryList = getLibCountries.toImmutableList(),
        onDismissRequest = {},
    )
}
