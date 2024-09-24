package dlt

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay

interface WalletGateway {
    suspend fun completeTransfer(payload: TransferCompleted)
}

class WalletGatewayImpl(host: String) : WalletGateway {

    private val url = "$host/completeTransfer"

    override suspend fun completeTransfer(payload: TransferCompleted) {
        delay(1000)

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        client.use {
            it.post(url) {
                contentType(Json)
                setBody(payload)
            }

            println("Finished sending request: $payload")
        }
    }
}