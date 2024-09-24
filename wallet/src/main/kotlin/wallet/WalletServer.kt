package wallet

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.Closeable

object WalletServer {

    @OptIn(DelicateCoroutinesApi::class)
    fun start(): Closeable {
        val server = embeddedServer(Netty, port = 8080) {
            routing {
                install(ContentNegotiation) {
                    json()
                }

                post("/completeTransfer") {
                    val request = call.receive<TransferCompleted>()
                    call.response.status(HttpStatusCode.OK)
                    println("Transfer has completed successfully.")
                }
            }
        }.start(wait = false)

        return Closeable {
            server.stop()
        }
    }
}