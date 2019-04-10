package fraud.detection.domain.fraud

import fraud.detection.domain.Transaction
import fraud.detection.domain.crm.CrmService
import fraud.detection.domain.crm.Event
import fraud.detection.domain.customer.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class FraudService(
        val fraudRepository: FraudRepository,
        val customerService: CustomerService,
        val crmService: CrmService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun analyzeTransaction(transaction: Transaction) {
        log.info("Analyzing transaction {}", transaction)
        val fraud = Fraud(orderId = transaction.orderId)
        var fraudStatus = FraudStatus.NOT_SUSPECTED

        val customer = customerService.get(transaction.customerId)
        if (transaction.city != customer.city) {
            fraudStatus = FraudStatus.SUSPECTED
            notifyFraud("FRAUD", fraud)
        }

        fraudRepository.save(Fraud(orderId = transaction.orderId, status = fraudStatus))
        log.info("Analyzed transaction {}", transaction)
    }

    fun invalidateFraud(fraud: Fraud) {
        log.info("Invalidating {}", fraud)
        fraud.status = FraudStatus.NOT_SUSPECTED
        fraudRepository.save(fraud)
        notifyFraud("CHECKED", fraud)
        log.info("Invalidated {}", fraud)
    }

    private fun notifyFraud(type: String, fraud: Fraud) {
        val eventData = mapOf("fraudId" to fraud.id)
        val event = Event(type, eventData)
        crmService.notifyEvent(fraud.orderId, event)
    }

    fun getFrauds(): MutableIterable<Fraud> {
        log.info("Getting all frauds")
        return fraudRepository.findAll()
    }

    fun getFraud(id: String): Optional<Fraud> {
        log.info("Getting Fraud {}", id)
        return fraudRepository.findById(id)
    }

    fun deleteAll() {
        log.info("Deleting all frauds")
        fraudRepository.deleteAll()
    }

}