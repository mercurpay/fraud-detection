package fraud.detection.infra

import fraud.detection.domain.fraud.FraudService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/frauds")
class FraudController(val fraudService: FraudService) {

    @GetMapping
    fun getFrauds() = fraudService.getFrauds()

    @GetMapping("/{id}/invalidation")
    fun invalidate(@PathVariable id: String): ResponseEntity<*> {
        var status = HttpStatus.OK
        val fraud = fraudService.getFraud(id)

        if (!fraud.isPresent) {
            status = HttpStatus.NOT_FOUND
        } else {
            fraudService.invalidateFraud(fraud.get())
        }

        return ResponseEntity.status(status).build<String>()
    }

}