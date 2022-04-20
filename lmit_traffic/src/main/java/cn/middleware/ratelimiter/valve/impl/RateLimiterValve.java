package cn.middleware.ratelimiter.valve.impl;

import cn.middleware.ratelimiter.Constants;
import cn.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.middleware.ratelimiter.valve.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class RateLimiterValve implements IValveService {

    private Logger logger = LoggerFactory.getLogger(RateLimiterValve.class);

    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable {
        //判断是否开启限流
        if(0==doRateLimiter.permitsPerSecond())
            return jp.proceed();

        //获取类名称
        String calssName = jp.getTarget().getClass().getName();
        logger.info(calssName);
        //获取方法名称
        String methodName = method.getName();
        logger.info(methodName);

        //类加方法名 重复不创建对象
        String key = calssName + "." + methodName;
        if(null== Constants.rateLimiterMap.get(key)){
            Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        //判断是否超过限流
        RateLimiter rateLimiter = Constants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            return jp.proceed();
        }

        return JSON.parseObject(doRateLimiter.returnJson(), method.getReturnType());
    }
}
