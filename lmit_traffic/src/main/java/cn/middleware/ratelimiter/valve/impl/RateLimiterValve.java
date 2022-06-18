package cn.middleware.ratelimiter.valve.impl;

import cn.middleware.ratelimiter.Constants;
import cn.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.middleware.ratelimiter.valve.IValveService;
import cn.middleware.tools.httpServletRequest.RequestUnit;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Service
public class RateLimiterValve implements IValveService {

    private Logger logger = LoggerFactory.getLogger(RateLimiterValve.class);

    private static final String POINT = ".";

    @Autowired
    HttpServletRequest request;

    @Autowired
    RequestUnit requestUnit;


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
        //获取IP
        String ip = requestUnit.getIpAddr(request);

        //类加方法名 重复不创建对象
        String key = ip + POINT + calssName + POINT + methodName;
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
