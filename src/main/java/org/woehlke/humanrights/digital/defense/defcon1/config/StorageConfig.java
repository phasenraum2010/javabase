package org.woehlke.humanrights.digital.defense.defcon1.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by tw on 25.06.18.
 */
@Configuration
@EnableNeo4jRepositories(
    basePackages = "org.woehlke.humanrights.digital.defense.defcon1.storage.neo4j.repository",
    transactionManagerRef = "neo4jTransactionManager"
)
@EnableRedisRepositories(
    basePackages = "org.woehlke.humanrights.digital.defense.defcon1.storage.redis.repository"
)
@EnableTransactionManagement
@EnableConfigurationProperties({
    JpaProperties.class,
    Neo4jProperties.class,
    DataSourceProperties.class
})
public class StorageConfig {

    @Bean
    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory){
        return new Neo4jTransactionManager(sessionFactory);
    }

    @Bean
    public JedisConnectionFactory jedisConnFactory() {
        JedisConnectionFactory fac = new JedisConnectionFactory(new RedisStandaloneConfiguration());
        return fac;
    }

    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        //redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisConnectionFactory jedisConnFactory) {
       return (RedisConnectionFactory) jedisConnFactory;
    }

    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisTemplate redisTemplate){
        return new RedisOperationsSessionRepository(redisTemplate);
    }

}
