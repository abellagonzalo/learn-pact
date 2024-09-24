package wallet

import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class WalletServerTest {

    @Test
    fun first() {
        val dltGateway = DltGatewayImpl("localhost:8081")
        runBlocking {
            val request = TransferRequest("alice", "bob", 100)
            dltGateway.transfer(request)
        }
    }
}