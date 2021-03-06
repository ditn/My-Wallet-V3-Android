package com.blockchain.morph

import com.blockchain.morph.exchange.service.QuoteService
import com.blockchain.morph.exchange.service.QuoteServiceFactory
import com.blockchain.morph.homebrew.QuoteWebSocket
import com.blockchain.morph.homebrew.authenticate
import com.blockchain.nabu.Authenticator
import com.blockchain.network.websocket.Options
import com.blockchain.network.websocket.autoRetry
import com.blockchain.network.websocket.bufferUntilAuthenticated
import com.blockchain.network.websocket.debugLog
import com.blockchain.network.websocket.newBlockchainWebSocket
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient

internal class QuoteWebSocketServiceFactory(
    private val nabuWebSocketOptions: Options,
    private val auth: Authenticator,
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) : QuoteServiceFactory {

    override fun createQuoteService(): QuoteService {
        val socket = okHttpClient.newBlockchainWebSocket(nabuWebSocketOptions)
            .debugLog("Quotes")
            .autoRetry()
            .authenticate(auth)
            .bufferUntilAuthenticated(limit = 10)

        return QuoteWebSocket(socket, moshi)
    }
}
