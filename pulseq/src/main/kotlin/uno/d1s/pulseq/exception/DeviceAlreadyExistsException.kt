package uno.d1s.pulseq.exception

import uno.d1s.pulseq.constant.error.ErrorConstants

class DeviceAlreadyExistsException(message: String = ErrorConstants.DEVICE_ALREADY_EXISTS) : RuntimeException(message)