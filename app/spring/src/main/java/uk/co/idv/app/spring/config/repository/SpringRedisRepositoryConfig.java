
package uk.co.idv.app.spring.config.repository;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.idv.context.adapter.repository.RedissonMapFactory;
import uk.co.idv.context.config.repository.ContextRepositoryConfig;
import uk.co.idv.context.config.repository.inmemory.InMemoryContextRepositoryConfig;
import uk.co.idv.lockout.config.repository.LockoutPolicyRepositoryConfig;
import uk.co.idv.lockout.config.repository.redis.RedisLockoutPolicyRepositoryConfig;
import uk.co.mruoc.json.JsonConverter;

import static uk.co.idv.app.spring.config.repository.EnvironmentLoader.loadEnvironment;

@Configuration
@Profile("!stubbed")
public class SpringRedisRepositoryConfig {

    @Bean
    public LockoutPolicyRepositoryConfig lockoutPolicyRepositoryConfig(RedissonMapFactory mapFactory,
                                                                       JsonConverter jsonConverter) {
        return RedisLockoutPolicyRepositoryConfig.builder()
                .policies(mapFactory.buildPolicyMap("lockout-policy"))
                .jsonConverter(jsonConverter)
                .build();
    }

    //TODO replace with context policy repository config using redis and then
    //add DynamoDB repostiory config for created contexts
    @Bean
    public ContextRepositoryConfig contextPolicyRepositoryConfig(RedissonMapFactory mapFactory,
                                                                 JsonConverter jsonConverter) {
        return new InMemoryContextRepositoryConfig();
    }

    @Bean
    public RedissonMapFactory mapFactory() {
        return RedissonMapFactory.builder()
                .client(redissionClient())
                .environment(loadEnvironment())
                .build();
    }

    private static RedissonClient redissionClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(System.getProperty("redis.endpoint.uri", "redis://redis:6379"));
        return Redisson.create(config);
    }

}
