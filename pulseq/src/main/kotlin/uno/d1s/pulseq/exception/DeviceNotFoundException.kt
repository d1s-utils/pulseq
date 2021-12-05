package uno.d1s.pulseq.exception

import uno.d1s.pulseq.constant.error.ErrorConstants

class DeviceNotFoundException(override val message: String = ErrorConstants.DEVICE_NOT_FOUND) :
    RuntimeException(message)