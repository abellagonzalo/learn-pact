package wallet

import kotlinx.serialization.Serializable

@Serializable
data class TransferCompleted(val id: String)