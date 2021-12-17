package uno.d1s.pulseq.strategy.source

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import uno.d1s.pulseq.testUtils.VALID_STUB

@SpringBootTest
@ContextConfiguration(classes = [SourceFindingStrategyTest::class])
internal class SourceFindingStrategyTest {

    @Test
    fun `should return ById source finding strategy`() {
        Assertions.assertEquals(SourceFindingStrategy.ById(VALID_STUB), byId(VALID_STUB))
    }

    @Test
    fun `should return ByName source finding strategy`() {
        Assertions.assertEquals(SourceFindingStrategy.ByName(VALID_STUB), byName(VALID_STUB))
    }

    @Test
    fun `should return ByAll source finding strategy`() {
        Assertions.assertEquals(SourceFindingStrategy.ByAll(VALID_STUB), byAll(VALID_STUB))
    }

    @Test
    fun `should return valid source finding strategy on getting it by strategy type`() {
        Assertions.assertEquals(
            SourceFindingStrategy.ById(VALID_STUB), byStrategyType(VALID_STUB, SourceFindingStrategyType.BY_ID)
        )

        Assertions.assertEquals(
            SourceFindingStrategy.ByName(VALID_STUB), byStrategyType(VALID_STUB, SourceFindingStrategyType.BY_NAME)
        )

        Assertions.assertEquals(
            SourceFindingStrategy.ByAll(VALID_STUB), byStrategyType(VALID_STUB, SourceFindingStrategyType.BY_ALL)
        )
    }
}