package uk.co.idv.app.spring.config.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.idv.context.config.identity.respository.IdentityRepositoryConfig;
import uk.co.idv.context.config.identity.respository.inmemory.InMemoryIdentityRepositoryConfig;
import uk.co.idv.context.config.lockout.repository.LockoutRepositoryConfig;
import uk.co.idv.context.config.lockout.repository.inmemory.InMemoryLockoutRepositoryConfig;

@Configuration
@Profile("stubbed")
public class SpringStubbedRepositoryConfig {

    @Bean
    @Profile("stubbed")
    public IdentityRepositoryConfig identityRepositoryConfig() {
        return new InMemoryIdentityRepositoryConfig();
    }

    @Bean
    @Profile("stubbed")
    public LockoutRepositoryConfig lockoutRepositoryConfig() {
        return new InMemoryLockoutRepositoryConfig();
    }

}
