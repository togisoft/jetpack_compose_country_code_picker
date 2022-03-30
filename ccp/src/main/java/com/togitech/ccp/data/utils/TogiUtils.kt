package com.togitech.ccp.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.ui.text.intl.Locale

fun getDefaultCountry(context: Context): String {
    val localeCode: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = localeCode.networkCountryIso
    val defaultLocale = Locale.current.language

    Log.d("Country Code:", countryCode)
    return countryCode.ifEmpty { defaultLocale }
}