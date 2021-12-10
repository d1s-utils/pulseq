package uno.d1s.pulseq.strategy.device

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.testUtils.VALID_STUB

@SpringBootTest
@ContextConfiguration(classes = [DeviceFindingStrategyTest::class])
internal class DeviceFindingStrategyTest {

    @Test
    fun `should return ById device finding strategy`() {
        Assertions.assertEquals(DeviceFindingStrategy.ById(VALID_STUB), byId(VALID_STUB))
    }

    @Test
    fun `should return ByName device finding strategy`() {
        Assertions.assertEquals(DeviceFindingStrategy.ByName(VALID_STUB), byName(VALID_STUB))
    }

    @Test
    fun `should return ByAll device finding strategy`() {
        Assertions.assertEquals(DeviceFindingStrategy.ByAll(VALID_STUB), byAll(VALID_STUB))
    }

    @Test
    fun `should return valid device finding strategy on getting it by strategy type`() {
        Assertions.assertEquals(
            DeviceFindingStrategy.ById(VALID_STUB), byStrategyType(VALID_STUB, DeviceFindingStrategyType.BY_ID)
        )

        Assertions.assertEquals(
            DeviceFindingStrategy.ByName(VALID_STUB), byStrategyType(VALID_STUB, DeviceFindingStrategyType.BY_NAME)
        )

        Assertions.assertEquals(
            DeviceFindingStrategy.ByAll(VALID_STUB), byStrategyType(VALID_STUB, DeviceFindingStrategyType.BY_ALL)
        )
    }
}