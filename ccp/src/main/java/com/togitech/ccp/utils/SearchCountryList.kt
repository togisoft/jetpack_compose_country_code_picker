package com.togitech.ccp.utils

import android.content.Context
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.countryNames

fun List<CountryData>.searchCountry(key: String, context: Context): List<CountryData> =
    this.mapNotNull {
        countryNames[it.countryCode]?.let { countryName ->
            val localizedCountryName = context.resources.getString(countryName).lowercase()
            if (localizedCountryName.contains(key.lowercase())) {
                it to localizedCountryName
            } else {
                null
            }
        }
    }
        .partition { it.second.startsWith(key.lowercase()) }
        .let { (startWith, contains) ->
            startWith.map { it.first } + contains.map { it.first }
        }

fun List<CountryData>.sortedByLocalizedName(context: Context): List<CountryData> =
    this.sortedBy {
        context.resources.getString(
            countryNames.getOrDefault(it.countryCode, R.string.unknown),
        )
    }
