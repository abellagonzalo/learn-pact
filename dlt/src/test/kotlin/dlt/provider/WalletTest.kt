package dlt.provider

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import dlt.DltServer
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

@PactConsumerTest
@PactTestFor(providerName = "wallet")
@Provider("dlt")
@PactFolder("../wallet/build/pacts")
class WalletTest {

    lateinit var closeable: Closeable

    @BeforeEach
    fun beforeEach(context: PactVerificationContext) {
        closeable = DltServer.start()
        context.target = HttpTestTarget(DltServer.host, DltServer.port)
    }

    @AfterEach
    fun afterEach() {
        closeable.close()
    }

    @Pact(consumer = "dlt", provider = "wallet")
    fun createPact(builder: PactDslWithProvider): V4Pact {
        return builder
            .uponReceiving("Transfer in dlt")

            .method("POST")
            .path("/completeTransfer")
            .body(
                PactDslJsonBody()
                    .stringMatcher("id", "[0-9a-zA-Z-]+")
            )

            .willRespondWith()
            .status(200)

            .toPact(V4Pact::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext, mockServer: MockServer) {
        DltServer.walletUrl = mockServer.getUrl()

        context.verifyInteraction()
        println("verifyInteraction finished")
        runBlocking {
            DltServer.deferred.await()
            DltServer.deferred.getCompleted()
        }
    }
}