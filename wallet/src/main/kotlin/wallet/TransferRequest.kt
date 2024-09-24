package wallet

import kotlinx.serialization.Serializable

@Serializable
data class TransferRequest(val from: String, val to: String, val amount: Int)