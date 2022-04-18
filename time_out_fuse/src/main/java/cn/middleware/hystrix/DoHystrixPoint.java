package cn.middleware.hystrix;

import cn.middleware.hystrix.annotation.DoHystrix;
import cn.middleware.hystrix.value.IValueService;
import cn.middleware.hystrix.value.impl.HystrixValueImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DoHystrixPoint {

    private Logger logger = LoggerFactory.getLogger(DoHystrixPoint.class);

    @Pointcut("@annotation(cn.middleware.hystrix.annotation.DoHystrix)")
    public void aopPoint() {
    }

    @Around("aopPoint()&& @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint pjp, DoHystrix doGovern) throws Throwable{
        IValueService iValueService = new HystrixValueImpl();
        return iValueService.access(pjp,getMethon(pjp),doGovern,pjp.getArgs());
    }

    private Method getMethon(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        logger.info("getMethon->getName:" + methodSignature.getName());
        logger.info("getMethon->getParameterTypes:" + methodSignature.getParameterTypes().toString());
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
