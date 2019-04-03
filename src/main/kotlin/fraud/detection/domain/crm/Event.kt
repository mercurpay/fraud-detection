package fraud.detection.domain.crm

data class Event(val type: String, val data: Map<String, Any> = mutableMapOf())