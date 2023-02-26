package com.togitech.ccp.utils

import android.content.Context
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryNames

fun List<CountryData>.searchCountry(key: String, context: Context): List<CountryData> =
    this.filter {
        countryNames[it.countryCode]?.let { countryName ->
            context.resources.getString(countryName).lowercase().contains(key.lowercase())
        } ?: false
    }

fun List<CountryData>.sortedByLocalizedName(context: Context): List<CountryData> =
    this.sortedBy {
        context.resources.getString(
            countryNames.getOrDefault(it.countryCode, R.string.unkown),
        )
    }
