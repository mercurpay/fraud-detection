package fraud.detection.domain.customer

data class Customer(
        val id: String,
        val name: String,
        val lastName: String,
        val document: String,
        val address: String,
        val city: String,
        val country: String,
        val email: String
)

data class NewCustomerRequest(
        val name: String,
        val lastName: String,
        val document: String,
        val address: String,
        val city: String,
        val country: String,
        val email: String
)