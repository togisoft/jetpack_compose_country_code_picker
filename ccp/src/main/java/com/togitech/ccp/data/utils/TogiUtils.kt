package com.togitech.ccp.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.ui.text.intl.Locale
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.togitech.ccp.data.CountryData

private const val MIN_PHONE_LENGTH = 6
private const val EMOJI_UNICODE = 0x1F1A5

@Suppress("SwallowedException")
private fun getDefaultLangCode(context: Context): String {
    val countryCode: String? = try {
        context.telephonyManager?.networkCountryIso
    } catch (ex: java.lang.AssertionError) {
        null
    }
    val defaultLocale = Locale.current.language
    return countryCode.takeIf { !it.isNullOrBlank() } ?: defaultLocale
}

fun getDefaultPhoneCode(context: Context, fallbackCountryData: CountryData): Pair<String, String> {
    val defaultCountry = getDefaultLangCode(context)
    val defaultCode: CountryData? = getLibCountries.firstOrNull { it.countryCode == defaultCountry }
    return defaultCountry to (
        defaultCode?.countryPhoneCode.takeIf {
            !it.isNullOrBlank()
        } ?: fallbackCountryData.countryPhoneCode
        )
}

fun checkPhoneNumber(fullPhoneNumber: String, countryCode: String): Boolean {
    val number: Phonenumber.PhoneNumber?
    if (fullPhoneNumber.length > MIN_PHONE_LENGTH) {
        return try {
            number = PhoneNumberUtil.getInstance().parse(
                fullPhoneNumber,
                Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name,
            )
            !PhoneNumberUtil.getInstance().isValidNumberForRegion(number, countryCode.uppercase())
        } catch (ex: Exception) {
            true
        }
    }
    return true
}

fun countryCodeToEmojiFlag(countryCode: String): String {
    return countryCode
        .uppercase()
        .map { char ->
            Character.codePointAt("$char", 0) + EMOJI_UNICODE
        }
        .joinToString("") {
            String(Character.toChars(it))
        }
}

private val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
