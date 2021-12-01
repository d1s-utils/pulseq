package uno.d1s.pulseq.client.testUtils

import uno.d1s.pulseq.client.event.KeyboardActivityDetectedEvent

internal val testKeyboardActivityDetectedEvent
    get() = KeyboardActivityDetectedEvent("", ' ')