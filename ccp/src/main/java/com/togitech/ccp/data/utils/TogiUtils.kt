package com.togitech.ccp.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.ui.text.intl.Locale
import com.togitech.ccp.data.CountryData

fun getDefaultCountry(context: Context): String {

    val localeCode: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = localeCode.networkCountryIso
    val defaultLocale = Locale.current.language

    Log.d("Country Code:", countryCode)
    return countryCode.ifEmpty { defaultLocale }
}


fun getDefaultCountryCode(context: Context): String {
    val defaultCountry = getDefaultCountry(context)
    val defaultCode: CountryData = getLibCountries().first() { it.countryCode == defaultCountry }
    return defaultCode.countryPhoneCode
}

