package com.togitech.ccp.utils

import android.content.Context
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getCountryName

fun List<CountryData>.searchCountry(key: String,context: Context): List<CountryData> {
    return filter {
        context.resources.getString(getCountryName(it.countryCode)).lowercase()
            .contains(key.lowercase()) || it.countryPhoneCode.contains(key)
    }
}