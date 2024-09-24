package dlt

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.Closeable
import java.util.UUID

object DltServer {

    val host = "localhost"
    val port = 8080
    lateinit var walletUrl: String

    lateinit var deferred: Deferred<Unit>

    @OptIn(DelicateCoroutinesApi::class)
    fun start(): Closeable {
        val server = embeddedServer(Netty, port = port) {
            routing {
                install(ContentNegotiation) {
                    json()
                }

                post("/transfer") {
                    val request = call.receive<TransferRequest>()

                    call.response.status(HttpStatusCode.OK)

                    deferred = GlobalScope.async {
                        delay(1000)

                        if (request.amount < 1)
                            throw Exception("Amount cannot be 0 or negative.")

                        val walletGateway = WalletGatewayImpl(walletUrl)
                        walletGateway.completeTransfer(TransferCompleted(UUID.randomUUID().toString()))
                    }
                }
            }
        }.start(wait = false)

        return Closeable {
            server.stop()
        }
    }
}