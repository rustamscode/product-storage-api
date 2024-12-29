package rustamscode.productstorageapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class TransactionalMethodTimeMeasureAspect extends TransactionSynchronizationAdapter {
    ThreadLocal<Instant> startTime = new ThreadLocal<>();

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void registerTransactionSynchronization() {
        TransactionSynchronizationManager.registerSynchronization(this);
        startTime.set(Instant.now());
    }

    public void afterCommit() {
        log.info("Method execution took {} ms", Duration.between(startTime.get(), Instant.now()).toMillis());
    }
}
