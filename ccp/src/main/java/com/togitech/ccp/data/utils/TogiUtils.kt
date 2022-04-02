package com.togitech.ccp.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.togitech.ccp.data.CountryData

@Composable
fun getDefaultLangCode(): String {
    val localeCode: TelephonyManager =
        LocalContext.current.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = localeCode.networkCountryIso
    val defaultLocale = Locale.current.language
    return countryCode.ifEmpty { defaultLocale }
}

@Composable
fun getDefaultPhoneCode(): String {
    val defaultCountry = getDefaultLangCode()
    val defaultCode: CountryData = getLibCountries().first() { it.countryCode == defaultCountry }
    return defaultCode.countryPhoneCode
}

fun checkPhoneNumber(phone: String, fullPhoneNumber: String, countryCode: String): Boolean {
    val number: Phonenumber.PhoneNumber?
    if (phone.length > 6) {
        return try {
            number = PhoneNumberUtil.getInstance().parse(
                fullPhoneNumber,
                Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name
            )
            PhoneNumberUtil.getInstance().isValidNumberForRegion(number, countryCode.uppercase())
        } catch (ex: Exception) {
            false
        }
    }
    return false
}
