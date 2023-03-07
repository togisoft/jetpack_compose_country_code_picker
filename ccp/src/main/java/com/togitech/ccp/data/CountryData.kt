package com.togitech.ccp.data

@Suppress("UnusedPrivateMember")
data class CountryData(
    private val cCodes: String,
    val countryPhoneCode: String,
    private val cNames: String, // bookkeeping only
) {
    val countryCode = cCodes.lowercase()
}
