package fraud.detection.domain.crm

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CrmService(
        val restTemplate: RestTemplate,
        @Value("\${crm-service.host}") val host: String,
        @Value("\${crm-service.port}") val port: Int
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun notifyEvent(orderId: String, event: Event) {
        val url = "http://$host:$port/api/orders/$orderId/events"
        log.info("Calling endpoint {}", url)
        val request = HttpEntity(event)
        restTemplate.postForEntity(url, request, String.javaClass)
    }

}