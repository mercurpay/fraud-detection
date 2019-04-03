package fraud.detection.domain.customer

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import tech.claudioed.customer.grpc.CustomerFindRequest
import tech.claudioed.customer.grpc.CustomerServiceGrpc
import java.util.concurrent.TimeUnit

@Service
class CustomerService(
        @Value("\${customer-service.host}") host: String,
        @Value("\${customer-service.port}") port: Int
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val managedChannel: ManagedChannel

    init {
        this.managedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(java.lang.Boolean.TRUE)
                .build()
    }

    operator fun get(id: String): Customer {
        log.info("Getting Customer=[{}]", id)

        val stub = CustomerServiceGrpc.newBlockingStub(managedChannel).withDeadlineAfter(1, TimeUnit.SECONDS)
        val request = CustomerFindRequest.newBuilder().build()
        val response = stub.findCustomer(request)

        return Customer(response.id, response.city)
    }

}