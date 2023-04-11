package com.togitech.ccp.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getCountryName
import com.togitech.ccp.data.utils.getFlags
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.utils.searchCountry


@Composable
fun TogiCodeDialog(
    modifier: Modifier = Modifier,
    padding: Dp = 15.dp,
    defaultSelectedCountry: CountryData = getLibCountries.first(),
    showCountryCode: Boolean = true,
    pickedCountry: (CountryData) -> Unit,
    showFlag: Boolean = true,
    showCountryName: Boolean = false,
    textStyleDefault: TextStyle = TextStyle.Default,
    backgroundColor: Color = MaterialTheme.colors.background,

    ) {
    val context = LocalContext.current

    val countryList: List<CountryData> = getLibCountries
    var isPickCountry by remember {
        mutableStateOf(defaultSelectedCountry)
    }
    var isOpenDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier
        .padding(padding)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
        ) {
            isOpenDialog = true
        }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showFlag) {
                Image(
                    modifier = modifier.width(34.dp),
                    painter = painterResource(
                        id = getFlags(
                            isPickCountry.countryCode
                        )
                    ), contentDescription = null
                )
            }
            if (showCountryCode) {
                Text(
                    text = isPickCountry.countryPhoneCode,
                    modifier = Modifier.padding(start = 6.dp),
                    style = textStyleDefault,
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = textStyleDefault.color
                )
            }
            if (showCountryName) {
                Text(
                    text = stringResource(id = getCountryName(isPickCountry.countryCode.lowercase())),
                    modifier = Modifier.padding(start = 6.dp),
                    style = textStyleDefault,
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = textStyleDefault.color
                )
            }
        }


        if (isOpenDialog) {
            CountryDialog(
                countryList = countryList,
                onDismissRequest = { isOpenDialog = false },
                context = context,
                dialogStatus = isOpenDialog,
                onSelected = { countryItem ->
                    pickedCountry(countryItem)
                    isPickCountry = countryItem
                    isOpenDialog = false
                },
                textStyleDefault = textStyleDefault,
                backgroundColor = backgroundColor,
            )
        }
    }
}

@Composable
fun CountryDialog(
    modifier: Modifier = Modifier,
    countryList: List<CountryData>,
    onDismissRequest: () -> Unit,
    onSelected: (item: CountryData) -> Unit,
    context: Context,
    dialogStatus: Boolean,
    textStyleDefault: TextStyle = TextStyle.Default,
    backgroundColor: Color = MaterialTheme.colors.background,
) {
    var searchValue by remember { mutableStateOf("") }
    if (!dialogStatus) searchValue = ""

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                color = textStyleDefault.color,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
            ) {
                Scaffold { scaffold ->
                    scaffold.calculateBottomPadding()
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)) {
                        SearchTextField(
                            value = searchValue, onValueChange = { searchValue = it },
                            textColor = textStyleDefault.color,
                            fontSize = textStyleDefault.fontSize,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = textStyleDefault.color,
                                    modifier = Modifier.padding(horizontal = 3.dp)
                                )
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .height(40.dp),
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn {
                            items(
                                if (searchValue.isEmpty()) countryList else countryList.searchCountry(
                                    searchValue,
                                    context
                                )
                            ) { countryItem ->
                                Row(
                                    Modifier
                                        .padding(18.dp)
                                        .fillMaxWidth()
                                        .clickable(onClick = { onSelected(countryItem) }),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = modifier.width(30.dp),
                                        painter = painterResource(
                                            id = getFlags(
                                                countryItem.countryCode
                                            )
                                        ), contentDescription = null
                                    )
                                    Text(
                                        stringResource(id = getCountryName(countryItem.countryCode.lowercase())),
                                        Modifier.padding(horizontal = 18.dp),
                                        style = textStyleDefault,
                                        fontFamily = textStyleDefault.fontFamily
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
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    value: String,
    textColor: Color = Color.Black,
    onValueChange: (String) -> Unit,
    hint: String = stringResource(id = R.string.search),
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    BasicTextField(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = textColor,
            fontSize = fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        hint,
                        style = LocalTextStyle.current.copy(
                            color = textColor,
                            fontSize = fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}