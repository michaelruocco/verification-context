package uk.co.idv.context.config;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import uk.co.idv.context.adapter.json.error.handler.ContextErrorHandler;
import uk.co.idv.context.usecases.context.ContextRepository;
import uk.co.idv.context.usecases.context.ContextService;
import uk.co.idv.context.usecases.context.CreateContext;
import uk.co.idv.context.usecases.context.SequencesRequestFactory;
import uk.co.idv.context.usecases.context.FindContext;
import uk.co.idv.context.usecases.context.MdcPopulator;
import uk.co.idv.context.usecases.context.event.create.CompositeContextCreatedHandler;
import uk.co.idv.context.usecases.context.event.create.ContextCreatedHandler;
import uk.co.idv.context.usecases.context.event.create.ExpiryContextCreatedHandler;
import uk.co.idv.context.usecases.context.event.create.LoggingContextCreatedHandler;
import uk.co.idv.context.usecases.context.identity.IdentityLoader;
import uk.co.idv.context.usecases.context.lockout.ContextLockoutService;
import uk.co.idv.context.usecases.context.method.CompositeMethodBuilder;
import uk.co.idv.context.usecases.context.method.MethodsBuilder;
import uk.co.idv.context.usecases.context.protect.ContextDataProtector;
import uk.co.idv.context.usecases.context.sequence.SequenceBuilder;
import uk.co.idv.context.usecases.context.sequence.SequencesBuilder;
import uk.co.idv.context.usecases.context.sequence.stage.StageBuilder;
import uk.co.idv.context.usecases.context.sequence.stage.StagesBuilder;
import uk.co.idv.context.usecases.policy.ContextPoliciesPopulator;
import uk.co.idv.context.usecases.policy.ContextPolicyRepository;
import uk.co.idv.context.usecases.policy.ContextPolicyService;
import uk.co.idv.identity.config.IdentityConfig;
import uk.co.idv.lockout.usecases.LockoutService;
import uk.co.idv.method.config.AppMethodConfigs;
import uk.co.mruoc.randomvalue.uuid.UuidGenerator;

import java.time.Clock;
import java.util.concurrent.Executors;

@Builder
@Slf4j
public class ContextConfig {

    private final Clock clock;
    private final UuidGenerator uuidGenerator;
    private final ContextPolicyRepository policyRepository;
    private final ContextRepository contextRepository;
    private final LockoutService lockoutService;
    private final AppMethodConfigs appMethodConfigs;
    private final IdentityConfig identityConfig;

    public ContextPoliciesPopulator policiesPopulator() {
        return new ContextPoliciesPopulator(policyService());
    }

    public ContextPolicyService policyService() {
        return new ContextPolicyService(policyRepository);
    }

    public ContextErrorHandler errorHandler() {
        return new ContextErrorHandler();
    }

    public ContextService contextService() {
        return ContextService.builder()
                .createContext(createContext())
                .findContext(findContext())
                .build();
    }

    public IdentityLoader identityLoader() {
        return IdentityLoader.builder()
                .createEligibility(identityConfig.createEligibility())
                .policyService(policyService())
                .build();
    }

    public FindContext findContext() {
        return FindContext.builder()
                .clock(clock)
                .lockoutService(lockoutService())
                .repository(contextRepository)
                .mdcPopulator(mdcPopulator())
                .build();
    }

    public ContextLockoutService lockoutService() {
        return ContextLockoutService.builder()
                .lockoutService(lockoutService)
                .clock(clock)
                .build();
    }

    private CreateContext createContext() {
        return CreateContext.builder()
                .clock(clock)
                .lockoutService(lockoutService())
                .repository(contextRepository)
                .sequencesRequestFactory(sequencesRequestFactory())
                .sequencesBuilder(sequencesBuilder())
                .createdHandler(contextCreated())
                .protector(contextDataProtector())
                .build();
    }

    private ContextDataProtector contextDataProtector() {
        return ContextDataProtector.builder()
                .identityProtector(identityConfig.identityProtector())
                .channelProtector(identityConfig.channelProtector())
                .methodProtector(appMethodConfigs.methodProtector())
                .build();
    }

    private SequencesBuilder sequencesBuilder() {
        return new SequencesBuilder(new SequenceBuilder(stagesBuilder()));
    }

    private StagesBuilder stagesBuilder() {
        return new StagesBuilder(new StageBuilder(methodsBuilder()));
    }

    private MethodsBuilder methodsBuilder() {
        return new MethodsBuilder(new CompositeMethodBuilder(appMethodConfigs.methodBuilders()));
    }

    private SequencesRequestFactory sequencesRequestFactory() {
        return new SequencesRequestFactory(uuidGenerator);
    }

    private ContextCreatedHandler contextCreated() {
        return new CompositeContextCreatedHandler(
                loggingContextCreatedHandler(),
                expiryContextCreatedHandler()
        );
    }

    private ExpiryContextCreatedHandler expiryContextCreatedHandler() {
        return ExpiryContextCreatedHandler.builder()
                .clock(clock)
                .executor(Executors.newScheduledThreadPool(contextExpiryThreadPoolSize()))
                .build();
    }

    private LoggingContextCreatedHandler loggingContextCreatedHandler() {
        return new LoggingContextCreatedHandler(mdcPopulator());
    }

    private MdcPopulator mdcPopulator() {
        return new MdcPopulator();
    }

    private static int contextExpiryThreadPoolSize() {
        String key = "context.expiry.thread.pool.size";
        int size = Integer.parseInt(System.getProperty(key, "100"));
        log.info("loaded {} value {}", key, size);
        return size;
    }

}
