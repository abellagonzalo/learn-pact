import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTest
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import org.apache.hc.client5.http.fluent.Request
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import kotlin.test.Test

@PactConsumerTest
@PactTestFor(providerName = "ArticlesProvider")
class ConsumerTest {

    @Pact(provider = "ArticlesProvider", consumer = "test_consumer")
    fun createPact(builer: PactDslWithProvider): V4Pact {
        return builer
//            .given("test state")

            .uponReceiving("ExampleJavaConsumerPactTest test interaction")
            .path("/articles.json")
            .method("GET")

            .willRespondWith()
            .status(200)
            .body("{\"responsetest\": true}")

            .toPact(V4Pact::class.java)
    }

    @Test
    fun first(mockServer: MockServer) {
        val httpResponse = Request.get(mockServer.getUrl() + "/articles.json")
            .execute()
            .returnResponse()

        assertThat(httpResponse.code, equalTo(200))
    }
}