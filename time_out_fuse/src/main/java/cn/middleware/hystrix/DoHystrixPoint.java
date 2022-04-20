package cn.middleware.hystrix;

import cn.middleware.hystrix.annotation.DoHystrix;
import cn.middleware.hystrix.value.IValueService;
import cn.middleware.hystrix.value.impl.HystrixValueImpl;
import cn.middleware.tools.method.MethodUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class DoHystrixPoint {

    private Logger logger = LoggerFactory.getLogger(DoHystrixPoint.class);

    @Autowired
    MethodUnit methodUnit;

    @Pointcut("@annotation(cn.middleware.hystrix.annotation.DoHystrix)")
    public void aopPoint() {
    }

    @Around("aopPoint()&& @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint pjp, DoHystrix doGovern) throws Throwable{
        IValueService iValueService = new HystrixValueImpl();
        return iValueService.access(pjp,methodUnit.getMethon(pjp),doGovern,pjp.getArgs());
    }


}
