package com.togitech.ccp.utils

import android.content.Context
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryNames

fun List<CountryData>.searchCountry(key: String,context: Context): MutableList<CountryData> {
    this.filter {
        countryNames.getOrNull(countryCode)?.let { countryName ->
            context.resources.getString(countryName).lowercase().contains(key.lowercase())
        } ?: false
        val countryName = context.resources.getString(countryNames(it.countryCode))
    }
    val tempList = mutableListOf<CountryData>()
    this.forEach {
        if (context.resources.getString(getCountryName(it.countryCode)).lowercase().contains(key.lowercase())) {
            tempList.add(it)
        }
    }
    return tempList
}
