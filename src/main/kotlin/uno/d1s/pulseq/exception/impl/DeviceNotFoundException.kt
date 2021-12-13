package uno.d1s.pulseq.exception.impl

import org.springframework.http.HttpStatus
import uno.d1s.pulseq.constant.error.ErrorConstants
import uno.d1s.pulseq.exception.AbstractHttpStatusException

class DeviceNotFoundException(override val message: String = ErrorConstants.DEVICE_NOT_FOUND) :
    AbstractHttpStatusException(HttpStatus.NOT_FOUND, message)