package fraud.detection.domain.customer

import io.grpc.ManagedChannelBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tech.claudioed.customer.grpc.CustomerCreateRequest
import tech.claudioed.customer.grpc.CustomerFindRequest
import tech.claudioed.customer.grpc.CustomerServiceGrpc
import java.util.concurrent.TimeUnit

@Service
class CustomerService(
        @Value("\${customer-service.host}") val host: String,
        @Value("\${customer-service.port}") val port: Int,
        @Value("\${grpc.deadline}") val grpcDeadline: Long
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val managedChannel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext(java.lang.Boolean.TRUE)
            .build()

    fun get(id: String): Customer {
        log.info("Getting Customer=[{}]", id)

        val stub = CustomerServiceGrpc
                .newBlockingStub(managedChannel).withDeadlineAfter(1, TimeUnit.SECONDS)
        val request = CustomerFindRequest.newBuilder().setId(id).build()
        val response = stub.findCustomer(request)

        return Customer(response.id, response.name, response.lastName, response.document,
                response.address, response.city, response.country, response.email)
    }

    fun createCustomer(customer: NewCustomerRequest): Customer {
        log.info("Saving {}", customer)

        val stub = CustomerServiceGrpc
                .newBlockingStub(managedChannel).withDeadlineAfter(grpcDeadline, TimeUnit.SECONDS)

        val request = CustomerCreateRequest.newBuilder()
                .setName(customer.name)
                .setLastName(customer.lastName)
                .setDocument(customer.document)
                .setAddress(customer.address)
                .setCity(customer.city)
                .setCountry(customer.country)
                .setEmail(customer.country)
                .build();

        val response = stub.createCustomer(request)
        return Customer(response.id, response.name, response.lastName, response.document,
                response.address, response.city, response.country, response.email)
    }

}