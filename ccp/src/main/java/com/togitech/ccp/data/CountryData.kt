package com.togitech.ccp.data

import java.util.*

data class CountryData(
    private var cCodes: String,
    val countryPhoneCode: String = "",
    val cNames:String = "",
    val flagResID: Int = 0
) {
    val countryCode = cCodes.lowercase(Locale.getDefault())
}
