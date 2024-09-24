package wallet


import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay


interface DltGateway {
    suspend fun transfer(request: TransferRequest)
}

class DltGatewayImpl(host: String) : DltGateway {

    private val url = "$host/transfer"

    override suspend fun transfer(request: TransferRequest) {
        delay(1000)

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        client.use {
            it.post(url) {
                contentType(Json)
                setBody(request)
            }

            println("Finished sending request: $request")
        }
    }

}