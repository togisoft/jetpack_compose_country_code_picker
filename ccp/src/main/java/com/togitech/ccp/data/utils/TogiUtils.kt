package com.togitech.ccp.data.utils

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.ui.text.intl.Locale
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.togitech.ccp.data.CountryData

private const val MIN_PHONE_LENGTH = 6
private const val EMOJI_UNICODE = 0x1F1A5

private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }

@Suppress("SwallowedException")
private fun getDefaultLangCode(context: Context): String =
    try {
        context.telephonyManager?.networkCountryIso
    } catch (ex: java.lang.AssertionError) {
        null
    }.let { countryCode ->
        countryCode.takeIf { !it.isNullOrBlank() } ?: Locale.current.language
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

fun isPhoneNumberValid(fullPhoneNumber: String): Boolean =
    if (fullPhoneNumber.length > MIN_PHONE_LENGTH) {
        try {
            phoneUtil.isValidNumber(phoneUtil.parse(fullPhoneNumber, null))
        } catch (ex: NumberParseException) {
            false
        }
    } else {
        false
    }

fun countryCodeToEmojiFlag(countryCode: String): String =
    countryCode
        .uppercase()
        .map { char ->
            Character.codePointAt("$char", 0) + EMOJI_UNICODE
        }
        .joinToString("") {
            String(Character.toChars(it))
        }

private val Context.telephonyManager: TelephonyManager?
    get() = getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
