package rustamscode.productstorageapi.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class MethodTimeMeasureAspect {

    @Around(value = "@annotation(rustamscode.productstorageapi.advice.annotation.MeasureTime)")
    public Object measureMethodTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final Instant start = Instant.now();
        Object returnValue = joinPoint.proceed();
        final Instant finish = Instant.now();

        log.info("Method execution took {} ms", Duration.between(start, finish).toMillis());
        return returnValue;
    }
}
