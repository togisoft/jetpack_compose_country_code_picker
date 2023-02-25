package com.togitech.ccp.data

data class CountryData(
    private val cCodes: String,
    val countryPhoneCode: String = "+90",
    val cNames: String = "tr",
) {
    val countryCode = cCodes.lowercase()
}
