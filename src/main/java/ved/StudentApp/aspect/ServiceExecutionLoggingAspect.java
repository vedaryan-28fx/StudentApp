package ved.StudentApp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(1)
public class ServiceExecutionLoggingAspect {

    @Pointcut("execution(public * ved.StudentApp.services.*.*(..))")
    public void serviceLayerMethods() {
    }

    @Pointcut("@annotation(ved.StudentApp.aspect.LogExecutionTime)")
    public void logExecutionTimeAnnotated() {
    }

    @Pointcut("serviceLayerMethods() || logExecutionTimeAnnotated()")
    public void monitoredMethods() {
    }

    @Around("monitoredMethods()")
    public Object profileAndLogExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodSignature = joinPoint.getSignature().toShortString();
        String arguments = Arrays.toString(joinPoint.getArgs());

        log.info("--> [ENTER] {} | args={}", methodSignature, arguments);

        long startNanos = System.nanoTime();
        boolean succeeded = false;

        try {
            Object result = joinPoint.proceed();
            succeeded = true;
            return result;
        } finally {
            double elapsedMs = (System.nanoTime() - startNanos) / 1_000_000.0;
            String status = succeeded ? "SUCCESS" : "FAILURE";
            log.info("<-- [EXIT]  {} | status={} | latency={} ms",
                    methodSignature, status, String.format("%.2f", elapsedMs));
        }
    }

    @AfterThrowing(pointcut = "monitoredMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String methodSignature = joinPoint.getSignature().toShortString();
        log.error("!!! [EXCEPTION] {} threw {} | message=\"{}\"",
                methodSignature,
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }
}