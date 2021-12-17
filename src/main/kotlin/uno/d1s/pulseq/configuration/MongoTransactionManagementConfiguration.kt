package uno.d1s.pulseq.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager


@Configuration
class MongoTransactionManagementConfiguration {

    @Bean
    fun transactionManager(dbFactory: MongoDatabaseFactory) =
        MongoTransactionManager(dbFactory)
}