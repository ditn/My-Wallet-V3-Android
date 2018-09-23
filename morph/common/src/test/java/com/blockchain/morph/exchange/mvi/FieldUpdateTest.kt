package com.blockchain.morph.exchange.mvi

import com.blockchain.testutils.cad
import com.blockchain.testutils.usd
import info.blockchain.balance.CryptoCurrency
import info.blockchain.balance.CryptoValue
import io.reactivex.subjects.PublishSubject
import org.amshove.kluent.`should equal`
import org.junit.Test

class FieldUpdateTest {

    @Test
    fun `initial state`() {
        val subject = PublishSubject.create<ExchangeIntent>()
        ExchangeDialog(subject, initial("USD", CryptoCurrency.BTC to CryptoCurrency.ETHER))
            .viewModel
            .test()
            .assertValue {
                it.from `should equal` value(
                    upToDate(CryptoValue.ZeroBtc),
                    upToDate(zeroFiat("USD"))
                )
                it.to `should equal` value(
                    upToDate(CryptoValue.ZeroEth),
                    upToDate(zeroFiat("USD"))
                )
                true
            }
    }

    @Test
    fun `update base crypto`() {
        given(
            initial("USD", CryptoCurrency.BTC to CryptoCurrency.ETHER)
        )
            .on(
                FieldUpdateIntent(
                    FieldUpdateIntent.Field.FROM_CRYPTO,
                    "123.45"
                )
            ) {
                assertValue {
                    it.from `should equal` value(
                        userEntered(CryptoValue.fromMajor(CryptoCurrency.BTC, 123.45.toBigDecimal())),
                        outOfDate(zeroFiat("USD"))
                    )
                    it.to `should equal` value(
                        outOfDate(CryptoValue.ZeroEth),
                        outOfDate(zeroFiat("USD"))
                    )
                    true
                }
            }
    }

    @Test
    fun `update counter crypto`() {
        given(
            initial("GBP", CryptoCurrency.BTC to CryptoCurrency.ETHER)
        )
            .on(
                FieldUpdateIntent(
                    FieldUpdateIntent.Field.TO_CRYPTO,
                    "99.12"
                )
            ) {
                assertValue {
                    it.from `should equal` value(
                        outOfDate(CryptoValue.ZeroBtc),
                        outOfDate(zeroFiat("GBP"))
                    )
                    it.to `should equal` value(
                        userEntered(CryptoValue.fromMajor(CryptoCurrency.ETHER, 99.12.toBigDecimal())),
                        outOfDate(zeroFiat("GBP"))
                    )
                    true
                }
            }
    }

    @Test
    fun `update base fiat`() {
        given(
            initial("USD", CryptoCurrency.BTC to CryptoCurrency.ETHER)
        )
            .on(
                FieldUpdateIntent(
                    FieldUpdateIntent.Field.FROM_FIAT,
                    "123.45"
                )
            ) {
                assertValue {
                    it.from `should equal` value(
                        outOfDate(CryptoValue.ZeroBtc),
                        userEntered(123.45.usd())
                    )
                    it.to `should equal` value(
                        outOfDate(CryptoValue.ZeroEth),
                        outOfDate(zeroFiat("USD"))
                    )
                    true
                }
            }
    }

    @Test
    fun `update counter fiat`() {
        given(
            initial("CAD", CryptoCurrency.BTC to CryptoCurrency.ETHER)
        )
            .on(
                FieldUpdateIntent(
                    FieldUpdateIntent.Field.TO_FIAT,
                    "45.67"
                )
            ) {
                assertValue {
                    it.from `should equal` value(
                        outOfDate(CryptoValue.ZeroBtc),
                        outOfDate(zeroFiat("CAD"))
                    )
                    it.to `should equal` value(
                        outOfDate(CryptoValue.ZeroEth),
                        userEntered(45.67.cad())
                    )
                    true
                }
            }
    }
}
