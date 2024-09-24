package wallet.consumer

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import kotlinx.coroutines.runBlocking
import wallet.DltGatewayImpl
import wallet.TransferRequest
import kotlin.test.Test

@PactConsumerTest
@PactTestFor(providerName = "dlt")
class TransferTest {

    @Pact(provider = "dlt", consumer = "wallet")
    fun createPact(builder: PactDslWithProvider): V4Pact {
        return builder
            .uponReceiving("Transfer interaction")

            .method("POST")
            .path("/transfer")
            .body(
                """
                    {
                        "from": "alice",
                        "to": "bob",
                        "amount": 100
                    }
                """.trimIndent()
            )

            .willRespondWith()
            .status(200)

            .toPact(V4Pact::class.java)
    }

    @Test
    fun first(mockServer: MockServer) {
        val dltGateway = DltGatewayImpl(mockServer.getUrl())
        runBlocking {
            dltGateway.transfer(
                TransferRequest("alice", "bob", 100)
            )
        }
    }
}