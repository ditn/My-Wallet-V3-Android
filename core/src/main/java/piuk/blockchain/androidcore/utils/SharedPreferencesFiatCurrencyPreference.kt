package piuk.blockchain.androidcore.utils

import com.blockchain.preferences.FiatCurrencyPreference

internal class SharedPreferencesFiatCurrencyPreference(
    private val prefsUtil: PrefsUtil
) : FiatCurrencyPreference {

    override val fiatCurrencyPreference: String
        get() = prefsUtil.getValue(PrefsUtil.KEY_SELECTED_FIAT, PrefsUtil.DEFAULT_CURRENCY)
}
