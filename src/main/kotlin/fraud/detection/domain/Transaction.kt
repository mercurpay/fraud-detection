package fraud.detection.domain

import java.math.BigDecimal

data class Transaction(
        val id: String,
        val value: BigDecimal,
        val requesterId: String,
        val customerId: String,
        val type: String,
        val paymentId: String,
        val orderId: String
)