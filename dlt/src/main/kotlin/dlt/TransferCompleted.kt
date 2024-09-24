package dlt

import kotlinx.serialization.Serializable

@Serializable
data class TransferCompleted(val id: String)