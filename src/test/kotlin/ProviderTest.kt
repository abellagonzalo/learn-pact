import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

@Provider("ArticlesProvider")
@PactFolder("build/pacts")
class ProviderTest {

    @Serializable
    data class ResponseTest(val responsetest: Boolean)

    private lateinit var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>

    @BeforeEach
    fun beforeEach(context: PactVerificationContext) {
        server = embeddedServer(Netty, port = 8080) {
            routing {
                install(ContentNegotiation) {
                    json()
                }
                get("/articles.json") {
                    call.respond(ResponseTest(true))
                }
            }
        }.start(wait = false)

        context.target = HttpTestTarget("localhost", 8080)
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @AfterEach
    fun afterEach() {
        server.stop()
    }
}