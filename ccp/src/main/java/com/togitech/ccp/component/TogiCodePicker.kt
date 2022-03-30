package com.togitech.ccp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getCountryName
import com.togitech.ccp.data.utils.getFlags
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.utils.searchCountry

class TogiCodePicker {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun TogiCodeDialog(
        modifier: Modifier = Modifier,
        padding: Dp = 15.dp,
        defaultSelectedCountry: CountryData = getLibCountries().first(),
        showCountryCode: Boolean = true,
        pickedCountry: (CountryData) -> Unit,
        dialogAppBarColor: Color = MaterialTheme.colors.primary,
        dialogAppBarTextColor: Color = Color.White,
        focusedBorderColorSearch: Color = MaterialTheme.colors.primary,
        unfocusedBorderColorSearch: Color = MaterialTheme.colors.onSecondary,
        cursorColorSearch: Color = MaterialTheme.colors.primary,
    ) {
        val countryList: List<CountryData> = getLibCountries()
        var isPickCountry by remember { mutableStateOf(defaultSelectedCountry) }
        var isOpenDialog by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }
        var isSearch by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Card(
            modifier = modifier
                .padding(3.dp)
                .clickable { isOpenDialog = true }
        ) {
            Column(modifier = Modifier.padding(padding)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = modifier.width(30.dp),
                        painter = painterResource(
                            id = getFlags(
                                isPickCountry.countryCode
                            )
                        ), contentDescription = null
                    )
                    if (showCountryCode) {
                        Text(
                            text = isPickCountry.countryPhoneCode,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            }

            //Select Country Dialog
            if (isOpenDialog) {
                Dialog(
                    onDismissRequest = { isOpenDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false),
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(id = R.string.select_country),
                                        textAlign = TextAlign.Center,
                                        modifier = modifier.fillMaxWidth()
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        isOpenDialog = false
                                        isSearch = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                },
                                backgroundColor = dialogAppBarColor,
                                contentColor = dialogAppBarTextColor,
                                actions = {
                                    IconButton(onClick = {
                                        isSearch = !isSearch
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Search,
                                            contentDescription = "Search"
                                        )
                                    }
                                }
                            )
                        }
                    ) {
                        Surface(modifier = modifier.fillMaxSize()) {
                            Card(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                elevation = 4.dp,
                            ) {
                                Column {
                                    if (isSearch) {
                                        searchValue = DialogSearchView(
                                            focusedBorderColor = focusedBorderColorSearch,
                                            unfocusedBorderColor = unfocusedBorderColorSearch,
                                            cursorColor = cursorColorSearch,
                                        )
                                    }

                                    LazyColumn {
                                        items(
                                            (if (searchValue.isEmpty()) {
                                                countryList
                                            } else {
                                                countryList.searchCountry(
                                                    searchValue,
                                                    context = context
                                                )
                                            })
                                        ) { countryItem ->
                                            Row(
                                                Modifier
                                                    .padding(
                                                        horizontal = 18.dp,
                                                        vertical = 18.dp
                                                    )
                                                    .clickable {
                                                        pickedCountry(countryItem)
                                                        isPickCountry = countryItem
                                                        isOpenDialog = false
                                                    }) {
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
                                                    Modifier.padding(horizontal = 18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun DialogSearchView(
        focusedBorderColor: Color = MaterialTheme.colors.primary,
        unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
        cursorColor: Color = MaterialTheme.colors.primary,
    ): String {
        var searchVal by remember { mutableStateOf("") }
        Row {
            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                value = searchVal, onValueChange = { searchVal = it },
                fontSize = 14.sp,
                hint = stringResource(id = R.string.search),
                textAlign = TextAlign.Start,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                cursorColor = cursorColor

            )
        }
        return searchVal
    }


    @Composable
    private fun SearchTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        hint: String = "",
        fontSize: TextUnit = 16.sp,
        textAlign: TextAlign = TextAlign.Center,
        focusedBorderColor: Color = MaterialTheme.colors.primary,
        unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
        cursorColor: Color = MaterialTheme.colors.primary,
    ) {
        Box(
            modifier = modifier
                .background(
                    color = Color.White.copy(alpha = 0.1f)
                )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = textAlign,
                    fontSize = fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black.copy(0.2f)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = focusedBorderColor,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = unfocusedBorderColor,
                    cursorColor = cursorColor,
                )
            )
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray,
                    modifier = Modifier.then(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 52.dp)
                    )
                )
            }
        }
    }
}