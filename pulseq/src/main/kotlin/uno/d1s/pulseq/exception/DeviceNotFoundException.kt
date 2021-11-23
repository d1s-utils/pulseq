package uno.d1s.pulseq.exception

class DeviceNotFoundException(override val message: String) : RuntimeException(message)