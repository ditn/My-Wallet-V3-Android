package com.blockchain.transactions

import info.blockchain.balance.AccountReference
import info.blockchain.balance.CryptoValue
import io.reactivex.Single

interface TransactionSender {

    /**
     * Send funds with default fees
     */
    fun sendFunds(
        sendDetails: SendDetails
    ): Single<SendFundsResult>

    fun dryRunSendFunds(
        sendDetails: SendDetails
    ): Single<SendFundsResult>
}

/**
 * Send funds, if it fails, it throws
 */
fun TransactionSender.sendFundsOrThrow(
    sendDetails: SendDetails
): Single<SendFundsResult> =
    sendFunds(sendDetails)
        .doOnSuccess {
            if (!it.success) {
                throw SendException(sendDetails)
            }
        }

class SendException(val details: SendDetails) : RuntimeException()

data class SendDetails(
    val from: AccountReference,
    val value: CryptoValue,
    val toAddress: String
)

data class SendFundsResult(
    val sendDetails: SendDetails,
    /**
     * Currency Specific error code, refer to the implementation
     */
    val errorCode: Int,
    val confirmationDetails: SendConfirmationDetails?,
    val hash: String?,
    val errorValue: CryptoValue? = null
) {
    val success = errorCode == 0 && hash != null
}

interface SendFundsResultLocalizer {

    fun localize(sendFundsResult: SendFundsResult): String
}

data class SendConfirmationDetails(
    val from: AccountReference,
    val to: String,
    val amount: CryptoValue,
    val fees: CryptoValue
)